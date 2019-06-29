import random

Q1 = """SELECT * FROM Tweets WHERE interest=1;"""

Q2 = """SELECT * FROM Tweets, Users WHERE interest=1 AND Tweets.user=Users.name;"""

Q3 = """SELECT * FROM Tweets, Users WHERE sensitive=10 and state='CA' AND Tweets.user=Users.name;"""

Q4 = """SELECT * FROM Tweets, Users WHERE accessLevel=100 and state='CA' AND Tweets.user=Users.name;"""

queries = [Q1, Q2, Q3, Q4]

fp = open("twitter_queries.txt", "w")
for i in range(10000):
    fp.write('"2016-11-29 21:13:01.844 EST","3568","2016-11-29 21:13:01 EST","execute <unnamed>:')
    fp.write(queries[random.randint(0, 3)])
    fp.write('"\n')


