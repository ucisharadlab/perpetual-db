package edu.uci.ics.perpetual;

import edu.uci.ics.perpetual.app.PerpetualCMDClient;
import edu.uci.ics.perpetual.parser.CCJSqlParserManager;
import edu.uci.ics.perpetual.parser.ParseException;
import edu.uci.ics.perpetual.statement.Statement;
import edu.uci.ics.perpetual.statement.select.Select;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class TIPPERSTestCase {

    private static PerpetualCMDClient pClient;

    @BeforeClass
    public static void setUp(){
        pClient = new PerpetualCMDClient();
    }

    @Test
    public void test() throws ParseException, JSQLParserException {

        // Create Types
//        pClient.runStatement("Create Metadata Type")
        pClient.runStatement("CREATE DATA SOURCE TYPE wifi_sensor('{id:int, name:string, ip:string, port:int)}'");
        pClient.runStatement("CREATE DATA SOURCE TYPE wifi_sensor_file({path: string})");
        pClient.runStatement("CREATE RAW TYPE wifi_observation(time_stamp, device_mac, wifi_ap)");
        pClient.runStatement("ADD FOR wifi_sensor ACQUISITION FUNCTION('wifi_source_func', '.../acqFunc.jar')");
        pClient.runStatement("ADD FOR wifi_sensor DATASOURCE(1, 'wifiap_2065', 'wifi_source_func', '{ip:\"127.1.1.1\", port:1111}')");

        // Add Values
        pClient.runStatement("ADD FOR wifi_observation TAG Machine(char)");
        pClient.runStatement("ADD FOR wifi_observation TAG Person(char)");
        pClient.runStatement("ADD FOR wifi_observation TAG BuildingLocation(char)");
        pClient.runStatement("ADD FOR wifi_observation TAG RegionLocation(char)");

        // Create Functions
        pClient.runStatement("CREATE FUNCTION getPersonFromWifi(wifi_observation, ../func1.jar) RETURNS Person COST 20");
        pClient.runStatement("CREATE FUNCTION getBuildingLocationFromWifi(wifi_observation, ../func2.jar) " +
                "RETURNS BuildingLocation COST 40");
        pClient.runStatement("CREATE FUNCTION getRegionLocationFromWifi(wifi_observation, BuildingLocation, ../func3.jar) " +
                "RETURN RegionLocation COST 30");

        // Create Requests
        pClient.runStatement("ADD Request(1, 1, '04/08/2019 12:00:00', '04/08/2019 14:00', 20, 'pull')");


        // Queries
        List<String> results1 = pClient.runQuery("SELECT wf.Person FROM wifi_observation as wf WHERE wf.Machine='xyz'");
        List<String> results2 = pClient.runQuery("SELECT wf.Person FROM wifi_observation as wf " +
                "WHERE date(wf.time_stamp)=20180310 AND wf.BuildingLocation='in'");
        List<String> results3 = pClient.runQuery("SELECT wf.Person FROM wifi_observation as wf " +
                "WHERE date(wf.time_stamp)=20180310 AND wf.RegionLocation='3142-clwa-2051'");

    }
}
