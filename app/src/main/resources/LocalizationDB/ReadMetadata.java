/*
 * This code is used to read metadata
 */
package LocalizationDB;

import java.sql.Connection;

public class ReadMetadata {
	static Connect connectlocal = new Connect("local","Observations");
	static Connect connectserver = new Connect("server","null");
	static Connection localCon;
	static Connection serverCon;
	
	public static void loadOfficeMetadata() {
		
	}
}
