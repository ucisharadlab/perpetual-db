/*
 * This code is used to define data structures for localization
 */
package LocalizationDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationModel {
	public List<String> Users = new ArrayList<>();
	public Map<String, String> emailOfficePair = new HashMap<String, String>(); 
	public List<Double> roomaffinity = new ArrayList<>();
}
