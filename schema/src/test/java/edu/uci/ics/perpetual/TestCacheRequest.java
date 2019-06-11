package edu.uci.ics.perpetual;

import edu.uci.ics.perpetual.parser.CCJSqlParserManager;
import edu.uci.ics.perpetual.parser.ParseException;
import edu.uci.ics.perpetual.request.AcquisitionRequest;
import edu.uci.ics.perpetual.request.CacheRequest;
import edu.uci.ics.perpetual.request.SqlRequest;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class TestCacheRequest {
    private static SchemaManager schemaManager;
    private static CCJSqlParserManager parser;

    @Test
    public void testGetAll_1_Single_Result() {
        // Without giving a parameter to the constructor,
        // you retrieve all name of Raw Types that available in SchemaManager
        CacheRequest request = new CacheRequest();

        schemaManager.accept(request);

        assertTrue(request.getStatus().isSuccess());
        assertEquals(new HashSet<>(Arrays.asList("wifi_observation")), new HashSet<>(request.getAllRawTypes()));
        assertNull(request.getTagFunctionMapping());
    }

    @Test
    public void testGetAll_2_Many_Result() {
        // testGetAll_1_Single_Result is before testGetAll_2_Many_Result

        execute("CREATE RAW TYPE Tweet(message CHAR, date DATE);");

        CacheRequest request = new CacheRequest();

        schemaManager.accept(request);

        assertTrue(request.getStatus().isSuccess());

        assertEquals(Arrays.asList("wifi_observation", "Tweet"), request.getAllRawTypes());
        assertNull(request.getTagFunctionMapping());
    }

    @Test
    public void testGetOne() {
        // This will retrieve the Map of
        // key:     TagName (String)
        // value:   collection of function name that generate this tag (List<String>)
        CacheRequest request = new CacheRequest("wifi_observation");

        schemaManager.accept(request);

        assertTrue(request.getStatus().isSuccess());

        HashMap<String, ArrayList<String>> tagFunctionMapping = request.getTagFunctionMapping();

        assertEquals(
                new HashSet<>(Arrays.asList("persons", "buildingLocations", "regionLocations", "roomLocations", "office", "affinities")),
                tagFunctionMapping.keySet());
        assertEquals(Arrays.asList("getPersonFromMac"), tagFunctionMapping.get("persons"));
        assertEquals(Arrays.asList("getBuildingLocation"), tagFunctionMapping.get("buildingLocations"));
        assertEquals(Arrays.asList("getRegionLocation"), tagFunctionMapping.get("regionLocations"));
        assertEquals(Arrays.asList("getRoomLocation"), tagFunctionMapping.get("roomLocations"));
        assertEquals(Arrays.asList("getOffice"), tagFunctionMapping.get("office"));
        assertEquals(Arrays.asList("getAffinity"), tagFunctionMapping.get("affinities"));

        assertNull(request.getAllRawTypes());
    }

    @Test
    public void testFailure() {
        // No Raw Type named Image
        CacheRequest request = new CacheRequest("Image");
        schemaManager.accept(request);

        assertFalse(request.getStatus().isSuccess());
        String expectedErrMsg = "Raw Type 'Image' does not exist";
        assertEquals(expectedErrMsg, request.getStatus().getErrMsg());
    }

    @BeforeClass
    public static void setUp(){
        schemaManager = SchemaManager.getInstance();
        parser = new CCJSqlParserManager();

//        /*
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

        // Add Acquisition Function and DataSource
        execute("ADD FOR wifi_sensor ACQUISITION FUNCTION('CameraSource', '.../func.jar');");
        execute("ADD FOR wifi_sensor DATASOURCE(1, 'Hall Camera', 'CameraSource', '{ip:\"127.1.1.1\", port:1111}');");
        execute("ADD FOR wifi_sensor DATASOURCE(2, 'First Floor Camera', 'CameraSource', '{ip:\"127.1.1.2\", port:1234}');");
//        */
    }

    private static void execute(String sql) {
        try {
            SqlRequest request = new SqlRequest(parser.parse(sql));
            schemaManager.accept(request);
            if (!request.getStatus().isSuccess()) {
                System.out.println("Ignore this >> " + request.getStatus().getErrMsg());
            }
        } catch (JSQLParserException | ParseException e) {
            e.printStackTrace();
        }
    }
}
