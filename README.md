# EnrichDB

A database system that provides progressive query computation, optimization along with smart pre processing of data.
Users only need to provide raw data types, derived data types and functions to transform data from one type to other.
The system figures out which functions to run at the ingestion and query time, based on resource constraints and query workload.

For more information check this [design document](https://docs.google.com/document/d/1MDBhUF5XPnHVvwd7idMyG0AIBErr7SRBIlSNeo70dH8/edit)

Excerpts from a running instance

```
PerperualDB >>> show schema;

Metadata Types
-------------------------------------------
USERS=(followers,INT),(name,char),(description,char),(screenName,char),
-------------------------------------------------

Raw Types
-------------------------------------------
USER_STOP_HISTORY=(image,BLOB),(id,INT),(reqID,INT),
WIFI_OBSERVATION=(device_mac,CHAR),(timestamp,DATE),
IMAGE=(image,BLOB),(id,INT),(reqID,INT),
TWEETS=(id,INT),(text,char),(user,char),(timestamp,INT),
AVL=(image,BLOB),(id,INT),(reqID,INT),
TWEET=(date,DATE),(message,CHAR),
STOP_TIME=(image,BLOB),(id,INT),(reqID,INT),
-------------------------------------------------

Data Source Types
-------------------------------------------
TWITTERFILEFEED=filePath, Acquisition Functions, {TWITTERPRODUCER=/home/peeyush/Downloads/perpetual-db/examples/src/main/java/TwitterProducer.jar}
WIFI_SENSOR=ip, port, Acquisition Functions, {CAMERASOURCE=.../func.jar, TWITTERPRODUCERNEW=/home/peeyush/Downloads/perpetual-db/examples/TwitterProducer.jar, TWITTERPRODUCER=/home/peeyush/Downloads/perpetual-db/examples/ac1.jar}
-------------------------------------------------

Data Sources
-------------------------------------------
1=Data Source Type = wifi_sensor, Function Path = /home/peeyush/Downloads/perpetual-db/examples/func2.jar, Function Params = {port=1111, ip=127.1.1.1}
2=Data Source Type = wifi_sensor, Function Path = /home/peeyush/Downloads/perpetual-db/examples/func2.jar, Function Params = {port=1234, ip=127.1.1.2}
3=Data Source Type = wifi_sensor, Function Path = /home/peeyush/Downloads/perpetual-db/examples/ac1.jar, Function Params = {port=1111, ip=.1.1.1}
4=Data Source Type = wifi_sensor, Function Path = /home/peeyush/Downloads/perpetual-db/examples/TwitterProducer.jar, Function Params = {port=1111, ip=127.1.1.1}
5=Data Source Type = TwitterFileFeed, Function Path = /home/peeyush/Downloads/perpetual-db/examples/src/main/java/TwitterProducer.jar, Function Params = {filePath=/home/peeyush/Downloads/perpetual-db/data/tweets.json}
-------------------------------------------------

Tags
-------------------------------------------
SENSITIVE=Tweets
ROUTE_SHORT_NAME=user_stop_history
DEPARTURE_TIME_HOUR=stop_time
ESTIMATE_SOURCE=user_stop_history
STATE=Tweets
STOP_SEQUENCE=stop_time
ACCESSLEVEL=Tweets
INTEREST=Tweets
-------------------------------------------------

Enrichment Functions
-------------------------------------------
GETPERSONFROMMAC=Raw Type=wifi_observation, Tag=persons, Dependencies=[device_mac], Cost=50, Path=null
GETOFFICE=Raw Type=wifi_observation, Tag=office, Dependencies=null, Cost=50, Path=null
GETREGIONLOCATION=Raw Type=wifi_observation, Tag=regionLocations, Dependencies=null, Cost=50, Path=null
GETAFFINITY=Raw Type=wifi_observation, Tag=affinities, Dependencies=[device_mac], Cost=50, Path=null
TWEET_TO_INTEREST=Raw Type=Tweets, Tag=interest, Dependencies=null, Cost=50, Path='/home/peeyush/Downloads/perpetual-db/examples/src/main/java/tweet_to_interest.jar'
GETROOMLOCATION=Raw Type=wifi_observation, Tag=roomLocations, Dependencies=null, Cost=50, Path=null
GETBUILDINGLOCATION=Raw Type=wifi_observation, Tag=buildingLocations, Dependencies=null, Cost=50, Path=null
-------------------------------------------------

PerperualDB >>> ADD Request(1, 5, '06/18/2019 11:19:00', '06/19/2019 19:21:00', 20, 'pull');

Request Added

PerperualDB >>> show requests;

Acquisition Requests
-------------------------------------------
1=RequestId = 1, DataSourceId = 5, Status = INPROGRESS, FunctionPath = /home/peeyush/Downloads/perpetual-db/examples/src/main/java/TwitterProducer.jar
-------------------------------------------------
```
