import mysql.connector as myc


def load_observations(scenario_name, file_name):
	config = dict()
	with open('log_info.txt') as f:
		info = f.readlines()
		config['user'] = info[0].strip()
		config['password'] = info[1].strip()
		config['database'] = info[2].strip()
	cnx = myc.connect(**config)
	cursor = cnx.cursor()
	table_name = scenario_name + '_observation'
	drop_table = 'DROP TABLE IF EXISTS %s' % table_name
	create_table = 'CREATE TABLE IF NOT EXISTS %s (id int, wifi_ap varchar(31), ' \
				   'timestamp datetime, user int)' % table_name
	cursor.execute(drop_table)
	cursor.execute(create_table)
	f = open(file_name, 'r')
	oid_str = f.readline().strip()
	while oid_str != '':
		ap = f.readline().strip()
		ts = "'" + f.readline().strip() + "'"
		user = int(f.readline().strip())
		oid = int(oid_str[1:])
		insert = 'INSERT INTO %s VALUES (%d,%s,%s,%d)' % (table_name, oid, ap, ts, user)
		print(insert)
		cursor.execute(insert)
		oid_str = f.readline().strip()
	f.close()
	cnx.commit()


if __name__ == '__main__':
	load_observations('mall', 'test_ob.txt')
