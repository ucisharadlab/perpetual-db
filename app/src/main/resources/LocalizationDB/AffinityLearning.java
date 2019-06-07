/*
 * This code first read observations from local database and then learn affinity;
 */
package LocalizationDB;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class AffinityLearning {

	public static List<Person> persons = new ArrayList<>();
	public static List<TempInfo> tempInfos = new ArrayList<>();

	static Connect connectlocal = new Connect("local","Observations");
	static Connect connectserver = new Connect("server","null");
	static Connection localCon;
	static Connection serverCon;
	static Map<String, String> pair = new HashMap<String, String>(); 
	static int tempStart, tempEnd;
	
	static int length = 14;

	static class Person {// store raw data
		List<String> time = new ArrayList<>();
		List<String> room = new ArrayList<>();
		List<Double> confidence = new ArrayList<>();
	}
	
	static class TempInfo {
		int flag;
		List<String> room = new ArrayList<>();
		List<Double> confidence = new ArrayList<>();
	}

	public static void connect() {
		localCon = connectlocal.getConnection();
		serverCon = connectserver.getConnection();
	}

	public static void close() {
		connectlocal.close();
		connectserver.close();
	}

	public static double deviceAffinity(String emailA, String emailB, String time) {
		// return the affinity of emailA and emailB
		connect();
		String beginTime = getMonth(-length/7, time);
		String endTime = time;
		// for now, read all data into memory, not clock
		ReadLocalObservation(emailA, beginTime, endTime);
		ReadLocalObservation(emailB, beginTime, endTime);
		close();
		return LearnAffinity(beginTime);
	}
	
	public static LocationModel roomAffinity(String email, List<String> locations) {
		String office = pair.get(email);
		String room;
		LocationModel affinity = new LocationModel();
		for(int i=0;i<locations.size();i++) {
			room = locations.get(i);
			if(room.equals(office)) {
				affinity.roomaffinity.add(0.6);
			}
			else if(pair.containsValue(room)) {//others' private room
				affinity.roomaffinity.add(0.1);
			}
			else {//public room
				affinity.roomaffinity.add(0.3);
			}
		}
		return affinity;
	}
	
	public static void loadOfficeMetadata() {
		connect();
		ResultSet rs;
		try {
			Statement stmtlocal = localCon.createStatement();
			rs = stmtlocal.executeQuery("select * from office");
			while(rs.next()) {
				pair.put(rs.getString(1), rs.getString(2));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		close();
	}

	public static double LearnAffinity(String beginTime) {
		// learn affinity only for email A and B
		String time;
		double affinity=0;
		for(int i=0;i<length;i++) {
			time = getDay(beginTime, i);
			LoadTempdata(time, 0);
			LoadTempdata(time, 1);
			affinity += PPonce(0, 1);
			tempInfos.clear();
		}
		affinity /= length;
		return affinity;
	}
	
	public static double PPonce(int p,int q) {
		double value = 0.0;
		//System.out.println(personName.get(q)+" "+persons.size());
		for(int i=0;i<tempInfos.get(p).room.size();i++) {
			String location = tempInfos.get(p).room.get(i);
			int index = tempInfos.get(q).room.indexOf(location);
			if(index!=-1) {
				value += tempInfos.get(p).confidence.get(i) + tempInfos.get(q).confidence.get(index);
			}
		}
		return value;
	}
	
	public static void LoadTempdata(String time, int id) {
		int flag = NearestTime(time, id);
		TempInfo tempInfo = new TempInfo();
		if (flag == -1) {
			tempInfo.flag = -1;
		} else {
			tempInfo.flag = 0;
			for (int i = tempStart; i <= tempEnd; i++) {
				tempInfo.room.add(persons.get(id).room.get(i));
				tempInfo.confidence.add(persons.get(id).confidence.get(i));
			}
		}
		tempInfos.add(tempInfo);
	}

	public static void ReadLocalObservation(String email, String beginTime, String endTime) {// read from local database
																								// and add into person
		ResultSet rs;
		connect();
		try {
			Statement stmtlocal = localCon.createStatement();
			String sql = LocalDataMaintenance.readObservationFromLocal(email, beginTime, endTime);
			rs = stmtlocal.executeQuery(sql);
			while (rs.next()) {
				Person person = new Person();
				person.time.add(String.valueOf(rs.getDate(1)));
				person.room.add(rs.getString(2));
				person.confidence.add(rs.getDouble(3));
				persons.add(person);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		close();
	}

	public static String getMonth(int id, String time) {// get week period based on id, id starts from 0
		SimpleDateFormat dataformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date clock = new Date();
		try {
			clock = dataformat.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(clock);
		calendar.add(Calendar.DATE, id * 7);
		Date m = calendar.getTime();
		String month = dataformat.format(m);

		return month;
	}

	public static String getClock(String day, int id) {// get the time point for one day, 6*12, starts from 08:00 AM, id
		// from 0
		Date clock = new Date();
		SimpleDateFormat dataformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			clock = dataformat.parse(day);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(clock);
		calendar.add(Calendar.MINUTE, id * 10);
		Date m = calendar.getTime();
		String minute = dataformat.format(m);

		return minute;
	}

	public static String getDay(String Day, int id) {// generate day in each period, id from 0

		Date clock = new Date();
		SimpleDateFormat dataformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			clock = dataformat.parse(Day);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(clock);
		calendar.add(Calendar.DATE, id);
		Date m = calendar.getTime();
		String day = dataformat.format(m);

		return day;
	}
	
	public static String getMonth(String Day, int id) {

		Date clock = new Date();
		SimpleDateFormat dataformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			clock = dataformat.parse(Day);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(clock);
		calendar.add(Calendar.MONTH, id);
		Date m = calendar.getTime();
		String month = dataformat.format(m);

		return month;
	}

	public static int Difference(String fromDate, String toDate) {
		SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int seconds = 0;
		try {
			long from = simpleFormat.parse(fromDate).getTime();
			long to = simpleFormat.parse(toDate).getTime();
			seconds = (int) ((to - from) / 1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return seconds;
	}

	public static Integer NearestTime(String time, int id) {// find the nearest time to given time, id is person index
		int flag = -1;// return -1 means no effective time found; o.w, is the index in PP list
		int size = persons.get(id).time.size();
		if (size == 0) {
			return -1;
		}
		if (time.compareTo(persons.get(id).time.get(0)) < 0) {
			int s = Difference(time, persons.get(id).time.get(0));
			if (s > 10 * 60) {
				return -1;
			} else {
				tempStart = 0;
				String temp = persons.get(id).time.get(0);
				int i;
				for (i = 0; i < size; i++) {
					if (temp.compareTo(persons.get(id).time.get(i)) != 0) {
						tempEnd = i - 1;
						return 0;
					}
				}
			}
		} else if (time.compareTo(persons.get(id).time.get(size - 1)) > 0) {
			int s = Difference(persons.get(id).time.get(size - 1), time);
			if (s > 10 * 60) {
				return -1;
			} else {
				tempEnd = size - 1;
				String temp = persons.get(id).time.get(size - 1);
				for (int i = size - 1; i >= 0; i--) {
					if (temp.compareTo(persons.get(id).time.get(i)) != 0) {
						tempStart = i + 1;
						return 0;
					}
				}
			}
		} else {
			int p = 0;
			for (int i = 0; i < size; i++) {
				if (time.compareTo(persons.get(id).time.get(i)) < 0) {
					p = i;
					break;
				}
			}
			// System.out.println(p);
			int difa = Difference(time, persons.get(id).time.get(p));
			int difb = Difference(persons.get(id).time.get(p - 1), time);

			if (difa > difb) {
				if (difb > 10 * 60) {
					return -1;
				}
				tempEnd = p - 1;
				String temp = persons.get(id).time.get(p - 1);
				for (int i = p - 1; i >= 0; i--) {
					if (temp.compareTo(persons.get(id).time.get(i)) != 0) {
						tempStart = i + 1;
						return 0;
					}
				}
			} else {
				if (difa > 10 * 60) {
					return -1;
				}
				tempStart = p;
				String temp = persons.get(id).time.get(p);
				for (int i = p; i < size; i++) {
					if (temp.compareTo(persons.get(id).time.get(i)) != 0) {
						tempEnd = i - 1;
						return 0;
					}
				}
			}

		}

		return flag;
	}
}
