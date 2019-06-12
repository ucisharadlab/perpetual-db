package edu.uci.ics.perpetual;

import edu.uci.ics.perpetual.parser.CCJSqlParserManager;
import edu.uci.ics.perpetual.parser.ParseException;
import edu.uci.ics.perpetual.request.AcquisitionRequest;
import edu.uci.ics.perpetual.request.SqlRequest;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class TestAcquisitionRequest {

    private static SchemaManager schemaManager;
    private static CCJSqlParserManager parser;

    @Test
    public void testSuccess() {
        /** Below is your query.
         * The format follows (requestId, dataSourceId, startDate, endDate, resolution, method)
         * Example: ADD Request(1, 1, '04/08/2019 12:00:00', '04/08/2019 14:00', 20, 'pull');
         */

        /** You are expecting to retrieve result based on
         * ADD FOR wifi_sensor DATASOURCE(1, 'Hall Camera', 'CameraSource', '{ip:\"127.1.1.1\", port:1111}');
         */

        // You can use either one of constructor in below, but I recommend 1st one
        // because SchemaManager technically does not need to know the requestId

        AcquisitionRequest request = new AcquisitionRequest(1);
        // AcquisitionRequest request = new AcquisitionRequest(1, 1);

        schemaManager.accept(request);

        assertTrue(request.getStatus().isSuccess());
        assertEquals(".../func.jar", request.getAcquisitionFunctionPath());

        HashMap<String, String> expectedAcquisitionFunctionParameters = new HashMap<>();
        expectedAcquisitionFunctionParameters.put("ip", "127.1.1.1");
        expectedAcquisitionFunctionParameters.put("port", "1111");
        assertEquals(expectedAcquisitionFunctionParameters, request.getAcquisitionFunctionParameters());

        HashMap<String, String> expectedRawTypeScheme = new HashMap<>();
        expectedRawTypeScheme.put("device_mac", "CHAR");
        expectedRawTypeScheme.put("timestamp", "DATE");
        assertEquals(expectedRawTypeScheme, request.getRawTypeScheme());

    }

    @Test
    public void testManySuccess() {
        AcquisitionRequest request1 = new AcquisitionRequest(1);

        schemaManager.accept(request1);
        assertTrue(request1.getStatus().isSuccess());
        assertEquals(".../func.jar", request1.getAcquisitionFunctionPath());

        AcquisitionRequest request2 = new AcquisitionRequest(2);

        schemaManager.accept(request2);
        assertTrue(request2.getStatus().isSuccess());
        assertEquals(".../func.jar", request2.getAcquisitionFunctionPath());


    }

    @Test
    public void testFailure() {
        // No dataSource with Id: 0
        AcquisitionRequest request = new AcquisitionRequest(0);
        schemaManager.accept(request);

        assertFalse(request.getStatus().isSuccess());

        String expectedErrMsg = "DataSource with id '0' does not exist";
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
