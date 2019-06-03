// This code is used for testing 
/*
 * 1. for the format of email, deal with it as a black box: differential them inside functions
 */

package LocalizationDB;


public class LocalizationDB {
	public static void main(String args[]) {
		Initialization.Initialize();
		String email = "yiminl18@uci.edu";
		String time = "2019-05-30 11:43:10";
		if(!LocalDataMaintenance.isValidEmail(email)) {
			System.out.println("Email format is not valid.");
			return ;
		}
		LocationSet roomLocation = LocationPrediction.getRoomLocation(email, time);
		System.out.println(roomLocation.buildingLocation + " " + roomLocation.regionLocation + " " + roomLocation.roomLocation);
	}
}

