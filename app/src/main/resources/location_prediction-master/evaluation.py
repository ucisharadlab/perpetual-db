import localization, pickle
from datetime import datetime

tablenames = {
	'Abdul Alsaudi': 'abdul_20171120to20180817',
	'Dhrub': 'dhrub_20171120to20180817',
	'Roberto Yus': 'roberto_20171120to20180817',
	'EunJeong Joyce Shin': 'joyce_20171120to20180817',
	'Primal Pappachan': ''
}

start_date = '20180101'
end_date = '20180630'
pos_threshold = 20
neg_threshold = 100
step_size = 4


class Model:
	def __init__(self, clf, ap_list, cnx_density, ap_freq):
		self.clf = clf
		self.ap_list = ap_list
		self.density = cnx_density
		self.ap_freq = ap_freq


def train_model(user_name, table_name, start, end, pos, neg, step):
	# Train the model for a user using the data in %table_name% between %start% and %end%
	# Then store the model and return the model
	# Format of start and end like '20180309'
	clf_name = get_clf_name(user_name, pos, neg, start)
	cnxs = localization.read_local_data(start, end, table_name)
	intervals, ap_list, cnx_density, ap_freq = localization.create_intervals(cnxs)
	data = localization.create_training_data(intervals, ap_list, pos, neg, cnx_density)
	clf = localization.semi_supervised_learning(data, step)
	model = Model(clf, ap_list, cnx_density, ap_freq)
	pickle.dump(model, open(clf_name, 'wb'))
	return model


def answer_query(query_time, user_name, table_name, start=start_date, end=end_date,
				 pos=pos_threshold, neg=neg_threshold, step=step_size):
	# Check whether the model exists for a user, if not generate one, if does, load one
	# query_time: datetime type python
	date_str = str(query_time.date()).replace('-', '')
	all_entries = localization.read_local_data(date_str, date_str, table_name)
	if len(all_entries) == 0 or len(all_entries) == 1:
		print('Query %s for %s: No connection activities on that day.' % (query_time, user_name))
		return 0, ''
	intervals, _, _, _ = localization.create_intervals(all_entries)
	interval = localization.find_interval(query_time, intervals)
	if interval is None:
		print('Query %s for %s: Query time before the first connection or after the last on that day.' % (
			query_time, user_name))
		return 0, ''
	try:
		f = open(get_clf_name(user_name, pos, neg, start), 'rb')
		model = pickle.load(f)
	except FileNotFoundError:
		model = train_model(user_name, table_name, start, end, pos, neg, step)
	state, ap = localization.predict_an_interval(interval, model.ap_list, model.clf, model.density, model.ap_freq)
	return state, ap


def get_clf_name(name, pos, neg, st):
	return './models/' + name.replace(' ', '_').replace('@', '_').replace('.', '_').lower() +\
		str(pos) + str(neg) + str(st) + '.clf'


def parse_query_time(time_str):
	return datetime.strptime(time_str, '%Y-%m-%d %H:%M:%S')


def evaluate(file_name):
	f = open(file_name)
	name = f.readline().strip()
	true_positive = 0
	true_negtive = 0
	ap_correct = 0
	total = 0
	while name != '':
		query_time_str = f.readline().strip()
		truth = f.readline().strip()
		if name not in table_name:
			print('No table found for name: %s' % name)
			name = f.readline()
			continue
		table_name = tablenames[name]
		state, ap = answer_query(parse_query_time(query_time_str), name, table_name)
		total += 1
		if truth == 'out' and state == 'out':
			true_negtive += 1
		if truth != 'out' and state == 'in':
			true_positive += 1
		if ap == truth:
			ap_correct += 1
		print('%s Truth: %s at %s' % (name, truth, query_time_str))
		name = f.readline().strip()
	print('Total: %d, True_pos: %d (%.2f), True_neg: %d (%.2f)' % (
		total, true_positive, true_positive / total, true_negtive, true_negtive / total))
