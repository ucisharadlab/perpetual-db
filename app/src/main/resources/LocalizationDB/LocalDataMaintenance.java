/*
 * This code is used to:
 * 1. generate local data: observations and affinities
 * 2. maintain local data: add affinity and observations
 */
package LocalizationDB;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class LocalDataMaintenance {
	static Connect connectlocal;
	static Connect connectserver;
	static Connection localCon;
	static Connection serverCon;
    static String serverDatabase = "null";
    static String localDatabase = "Observations"; 
	
	public static void connect(String type, String database) {
		if(type.equals("local")) {
			connectlocal = new Connect("local",database);//Observations
			localCon = connectlocal.getConnection();
		}
		else {
			connectserver = new Connect("server",database);//OBSERVATION
			serverCon = connectserver.getConnection();
		}
	}
	
	public static void closelocal() {
		connectlocal.close();
	}
	
	public static void closeserver() {
		connectserver.close();
	}
	
	public static String createStatementDB(String dababase) {
		return String.format("create database IF NOT EXISTS %s;",dababase);
	}
	
	
	public static String readObservationFromTipper(String email, String beginTime, String endTime) {
		return String.format("select  distinct SEMANTIC_OBSERVATION.payload, SEMANTIC_OBSERVATION.confidence, SEMANTIC_OBSERVATION.timeStamp"
				+ " from USER, SEMANTIC_OBSERVATION where USER.email='%s' and SEMANTIC_OBSERVATION.semantic_entity_id=USER.SEMANTIC_ENTITY_ID"
				+ " and SEMANTIC_OBSERVATION.timeStamp>='%s' and SEMANTIC_OBSERVATION.timeStamp<'%s' ORDER BY SEMANTIC_OBSERVATION.timeStamp",email,beginTime,endTime );
	}
	
	public static String readObservationFromLocal(String email, String beginTime, String endTime) {
		return String.format("select * from %s where time>='%s'  and time<='%s';",email,beginTime,endTime);
	}
	
	public static String createstatementObservationTable(String email) {//create observation table for each user
		return String.format("create table IF NOT EXISTS OBSERVATION%s (timestamp varchar(255), location varchar(255),confidence double)", email);
	}
	
	public static String createstatementAffintyTable(String email) {
		return String.format("create table IF NOT EXISTS AFFINITY%s (email varchar(255), affinity double, time varchar(255))", email);
	}
	
	public static String createstatementCatcheObservationList() {
		return String.format("create table IF NOT EXISTS CatcheObservationList (email varchar(255), time varchar(255), primary key (email,time));");
	}
	
	public static String createstatementUserTable() {
		return String.format("create table IF NOT EXISTS Users (email varchar(255))");
	}
	
	public static String createstatementOfficeTable() {
		return String.format("create table IF NOT EXISTS office(email varchar(255), office varchar(255))");
	}
	
	public static String createstatementInsertUser(String email) {
		return String.format("insert into Users (email)value('%s');", email);
	}
	
	public static String readUser() {
		return String.format("select * from Users;");
	}
	
	public static String createStatementTableExists(String tableName) {
		return String.format("select t.table_name from information_schema.TABLES t where t.TABLE_SCHEMA ='%s' and t.TABLE_NAME ='%s';",localDatabase, tableName);
	}
	
	public static String insertObservation(String tablename,String timestamp,String location,double confidence) {
		return String.format("insert into %s(timestamp,location,confidence)value('%s','%s',%s);",tablename,timestamp,location,confidence);
	}
	
	public static String insertAffinity(String tablename,String email,Double affinity,String datetime) {
		return String.format("insert into %s(email,affinity,time)value('%s',%s,'%s');",tablename,email,affinity,datetime);
	}
	
	public static void captureObservation(String email, String beginTime, String endTime) {
		//load observation from tipper to local database
		ResultSet rs;
		connect("server",serverDatabase);//Observations is database
		connect("local", localDatabase);
		try {
			Statement stmtserver = serverCon.createStatement();
			Statement stmtlocal = localCon.createStatement();
			String sql = readObservationFromTipper(email, beginTime, endTime);
			rs = stmtserver.executeQuery(sql);
			String location;
			double confidence;
			String time;
			while (rs.next()) {
				location = rs.getString(1);
				location = location.substring(13, location.length()-1);
				confidence = rs.getDouble(2);
				time = rs.getString(3);
				//System.out.println(insertObservation("OBSERVATION"+ID(email), time, location, confidence));
				stmtlocal.executeUpdate(insertObservation("OBSERVATION"+ID(email), time, location, confidence));
			}
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeserver();
		closelocal();
	}
	
	public static void creatUserObservation(String email) {
		connect("local", localDatabase);
		try {
			Statement stmtlocal = localCon.createStatement();
			System.out.println(createstatementObservationTable(email));
			stmtlocal.executeUpdate(createstatementObservationTable(email));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closelocal();
	}
	
	public static void insertUser(String email) {
		connect("local", localDatabase);
		try {
			Statement stmtlocal = localCon.createStatement();
			stmtlocal.executeUpdate(createstatementInsertUser(email));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closelocal();
	}
	
	public static void UpdateCachedObservationList(String email, String time) {
		connect("local", localDatabase);
		try {
			Statement stmtlocal = localCon.createStatement();
			stmtlocal.executeUpdate(String.format("insert into CatcheObservationList (email,time)value('%s','%s');", email,time.substring(0, 7)));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closelocal();
	}
	
	public static Boolean CheckCachedObservationList(String email, String time) {
		connect("local", localDatabase);
		ResultSet rs;
		int count=0;
		try {
			Statement stmtlocal = localCon.createStatement();
			rs = stmtlocal.executeQuery(String.format("select count(*) from CatcheObservationList where email = '%s' and time ='%s';", email,time.substring(0, 7)));
			while(rs.next()) {
				count = rs.getInt(1);
			}
			if(count>=1) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closelocal();
		return false;
	}
	
	public static void createDB(String database) {
		connect("local", localDatabase);
		try {
			Statement stmtlocal = localCon.createStatement();
			stmtlocal.executeUpdate(createStatementDB(database));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closelocal();
	}
	
	public static void createOfficeTable() {
		connect("local", localDatabase);
		try {
			Statement stmtlocal = localCon.createStatement();
			stmtlocal.executeUpdate(createstatementOfficeTable());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closelocal();
	}
	
	public static void createCatcheObservationListTable() {
		connect("local", localDatabase);
		try {
			Statement stmtlocal = localCon.createStatement();
			stmtlocal.executeUpdate(createstatementCatcheObservationList());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closelocal();
	}
	
	public static void createObservationTable(String email) {
		connect("local", localDatabase);
		try {
			Statement stmtlocal = localCon.createStatement();
			stmtlocal.executeQuery(createstatementObservationTable(email));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closelocal();
	}
	
	public static void buildOfficeTable() {
		if(TableExists("office")) return ;//if office table has been built, no need to build again
		connect("local", localDatabase);
		connect("server", serverDatabase);
		ResultSet rs;
		List<String> emails = new ArrayList<>();
		try {
			Statement stmtserver = serverCon.createStatement();
			rs = stmtserver.executeQuery(String.format("select email from USER where office is not null and email is not null"));
			while(rs.next()) {
				emails.add(rs.getString(1));
			}
			Statement stmtlocal = localCon.createStatement();
			for(int i=0;i<emails.size();i++) {
				rs = stmtserver.executeQuery(String.format("select INFRASTRUCTURE.name from USER, INFRASTRUCTURE"
						+ " where USER.office=INFRASTRUCTURE.SEMANTIC_ENTITY_ID and USER.email='%s'", emails.get(i)));
				while(rs.next()) {
					stmtlocal.executeUpdate(String.format("insert into office(email,office) value('%s','%s')", emails.get(i),rs.getString(1)));
				}
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closelocal();
		closeserver();
	}
	
	
	public static void createUserTable() {
		connect("local", localDatabase);
		try {
			Statement stmtlocal = localCon.createStatement();
			stmtlocal.executeUpdate(createstatementUserTable());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closelocal();
	}
	
	public static boolean TableExists(String tableName) {
		connect("local", localDatabase);
		ResultSet rs;
		String table = "";
		try {
            Statement statement = localCon.createStatement();
            rs = statement.executeQuery(createStatementTableExists(tableName));
            while(rs.next()) {
            		table = rs.getString(1);
            }
            if(table.equals("")) {
            		return false;
            }
        } catch (SQLException e) {
        }
		closelocal();
        return true;
	}
	
	public static LocationModel getUsers(String database) {//
		connect("local", localDatabase);
		LocationModel Users = new LocationModel();
		ResultSet rs;
		try {
			Statement stmtlocal = localCon.createStatement();
			rs = stmtlocal.executeQuery(createStatementDB(database));
			while(rs.next()) {
				Users.Users.add(rs.getString(1));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closelocal();
		return Users;
	}
	
	public static String ID(String email) {
		int l = email.indexOf("@");
		return email.substring(0, l);
	}
	
	public static Boolean isValidEmail(String email) {
		if(email.indexOf("@")==-1) {
			return false;
		}
		return true;
	}
	
	public static Boolean isObservationExist(String email, String time) {
		connect("local", localDatabase);
		ResultSet rs;
		int count=0;
		try {
			Statement stmtlocal = localCon.createStatement();
			rs = stmtlocal.executeQuery(String.format("select count(*) from CatcheObservationList where email='%s' and time = '%s'", email,time));
			while(rs.next()) {
				count = rs.getInt(1);
			}
			if(count>=1) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closelocal();
		return false;
	}
	
	public static String beginTime(String time) {
		return time.substring(0, 8)+"01 00:00:00";
	}
	
	public static String endTime(String time) {
		return AffinityLearning.getMonth(beginTime(time), 1);
	}
}
