package edu.uci.ics.perpetual;

import edu.uci.ics.perpetual.parser.CCJSqlParserManager;
import edu.uci.ics.perpetual.parser.ParseException;
import edu.uci.ics.perpetual.request.SqlRequest;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestAll {
    private static SchemaManager schemaManager;
    private static CCJSqlParserManager parser;

    @BeforeClass
    public static void setUp(){
        schemaManager = SchemaManager.getInstance();
        parser = new CCJSqlParserManager();
    }

    @Test
    public void testCreateAll() {
        schemaManager = SchemaManager.getInstance();

        // CREATE
        execute("CREATE RAW TYPE wifi_observation(device_mac CHAR, timestamp DATE);");
        execute("CREATE DATASOURCE TYPE wifi_sensor('{ip:\"1.1.1.1\", port:2000}') GENERATES wifi_observation;");
        execute("ADD FOR wifi_observation TAG(persons char);");
        execute("ADD FOR wifi_observation TAG(device_mac char);");
        execute("ADD FOR wifi_observation TAG(buildingLocations char);");
        execute("ADD FOR wifi_observation TAG(regionLocations char);");
        execute("ADD FOR wifi_observation TAG(roomLocations char);");
        execute("ADD FOR wifi_observation TAG(office char);");
        execute("ADD FOR wifi_observation TAG(affinities char);");

        // Function UDF
        execute("CREATE FUNCTION getPersonFromMac(wifi_observation, device_mac) RETURNS persons Cost 50");
        execute("CREATE FUNCTION getBuildingLocation(wifi_observation) RETURNS buildingLocations Cost 50");
        execute("CREATE FUNCTION getRegionLocation(wifi_observation) RETURNS regionLocations Cost 50");
        execute("CREATE FUNCTION getRoomLocation(wifi_observation) RETURNS roomLocations Cost 50");
        execute("CREATE FUNCTION getOffice(wifi_observation) RETURNS office Cost 50");
        execute("CREATE FUNCTION getAffinity(wifi_observation, device_mac) RETURNS affinities Cost 50");

        // Add DataSource
        System.out.println(1);
    }

    @Test
    public void testAdd() {
        testCreateAll();

        execute("ADD FOR wifi_sensor ACQUISITION FUNCTION('CameraSource', '.../func.jar');");
        execute("ADD FOR wifi_sensor DATASOURCE(1, 'Hall Camera', 'CameraSource', '{ip:\"127.1.1.1\", port:1111}');");

        System.out.println(1);
    }

    @Test
    public void testRetrieveSaved() {
        schemaManager = SchemaManager.getInstance();

        System.out.println(1);
    }


    private void execute(String sql) {
        try {
            SqlRequest request = new SqlRequest(parser.parse(sql));
            schemaManager.accept(request);
            if (!request.getStatus().isSuccess()) {
                System.out.println(request.getStatus().getErrMsg());
            }
        } catch (JSQLParserException | ParseException e) {
            e.printStackTrace();
        }
    }
}