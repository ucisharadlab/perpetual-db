package LocalizationDB;

import service.LocationService;
import service.LocationState;

public class LocationPrediction {
	static int length = 14;

	public static LocationSet getRoomLocation(String email, String time) {
		LocationSet locations = new LocationSet();
		AffinityLearning.loadOfficeMetadata();

		if (!LocalDataMaintenance.TableExists("OBSERVATION" + LocalDataMaintenance.ID(email))) {// this user is not cached before
			LocalDataMaintenance.creatUserObservation(LocalDataMaintenance.ID(email));
			LocalDataMaintenance.insertUser(email);
		}
		if (!LocalDataMaintenance.CheckCachedObservationList(email, time)) {
			LocalDataMaintenance.captureObservation(email, LocalDataMaintenance.beginTime(time),
					LocalDataMaintenance.endTime(time));
			LocalDataMaintenance.UpdateCachedObservationList(email, time);
		}
		String preMonth = AffinityLearning.getMonth(time, -1);
		if (!LocalDataMaintenance.CheckCachedObservationList(email, preMonth)) {
			LocalDataMaintenance.captureObservation(email, LocalDataMaintenance.beginTime(preMonth),
					LocalDataMaintenance.endTime(preMonth));
			LocalDataMaintenance.UpdateCachedObservationList(email, preMonth);
		}

		LocationState result = new LocationState();
		result = LocationService.newQueryTimestampByEmail(email, time);
		if (!result.inside) {
			locations.buildingLocation = "out";
			locations.regionLocation = "null";
			locations.roomLocation = "null";
		} else {
			locations.buildingLocation = "in";
			locations.regionLocation = result.accessPoint;
			locations.regionProbability = result.insideProb;
			double prob = 0.0;
			String room = "";
			for (int i = 0; i < result.possibleRooms.size(); i++) {
				double probability = getRoomProbability(result.possibleRooms.get(i), time, email);
				if (probability > prob) {
					prob = probability;
					room = result.possibleRooms.get(i);
				}
			}
			locations.roomLocation = room;
			locations.roomProbability = prob;
		}
		return locations;
	}

	public static double getRoomProbability(String room, String time, String email) {// mainAffinity is for (room,target
																						// email)
		double prob = 0;
		LocationModel Users = LocalDataMaintenance.getUsers("Observations");
		LocationModel roomAffinty = new LocationModel();
		LocationState result = new LocationState();
		double rAffinity, dAffinity;
		String user;
		double onlineUser = 0;
		for (int i = 0; i < Users.Users.size(); i++) {
			user = Users.Users.get(i);
			result = LocationService.newQueryTimestampByEmail(user, time);
			if (!result.inside)
				continue;// offline
			if (!result.possibleRooms.contains(room))
				continue;// candidate rooms do not contain "room"
			onlineUser++;
			roomAffinty = AffinityLearning.roomAffinity(email, result.possibleRooms);
			rAffinity = roomAffinty.roomaffinity.get(result.possibleRooms.indexOf(room));
			dAffinity = AffinityLearning.deviceAffinity(email, user, time);
			prob += (rAffinity * dAffinity);
		}
		if (onlineUser == 0)
			return 0;
		return prob / onlineUser;
	}
}
