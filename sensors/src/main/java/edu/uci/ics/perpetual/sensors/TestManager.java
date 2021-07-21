package edu.uci.ics.perpetual.sensors;

import edu.uci.ics.perpetual.sensors.model.*;
import edu.uci.ics.perpetual.sensors.predicate.*;
import edu.uci.ics.perpetual.util.Pair;
import org.apache.log4j.BasicConfigurator;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class TestManager {
    static DbSensorManager manager = new DbSensorManager();

    public static void main(String[] args) throws Exception {
        BasicConfigurator.configure();
//        testCreateTypes();
//        testCreateSensors();
//        testMobileSensor();
//        testFetchSensor();
//        testStoreData();
//        testFetchData();
//        testPlatformCreateAndFetch();
//        testPredicates();
//        testPredicateQuery();
//        testCreate50Sensors();
//        storeMillionRows();
//        testPredicate_OccupancyOverlap();
        TestCacheManager.testCachedSensorType();
    }

    public static void testCreateTypes() throws Exception {
        // create macId (string) observation type
        List<Pair<String, String>> attributes = new LinkedList<>();
        attributes.add(new Pair<>("macAddress", "VARCHAR(50)"));
        manager.createObservationType("MAC_Address", attributes);
        // create image (blob) observation type
        attributes.clear();
        attributes.add(new Pair<>("picture", "bytea"));
        manager.createObservationType("Image", attributes);

        manager.createSensorType("Camera", "Image");
        manager.createSensorType("WIFI_AP", "MAC_Address");
    }

    private static void testCreateSensors() throws Exception {
        // create 5 camera sensors: location, view frustum, etc
        for (int i = 1; i <= 5; i++) {
            manager.createSensor(new Sensor("camera" + i, 7,
                    new Location(String.format("a%d,b%d,c%d", i, i, i)), new Location(String.format("d%d,e%d,f%d", i, i, i))));
        }

        // create 5 wifi ap sensors
        for (int i = 1; i <= 5; i++) {
            manager.createSensor(new Sensor("wifi" + i, 8,
                    new Location(String.format("a%d,b%d,c%d", i, i, i)), new Location(String.format("d%d,e%d,f%d", i, i, i))));
        }
    }

    private static void testMobileSensor() throws Exception {
        manager.createMobileSensor(new MobileSensor("mobileCamera1", 1, new Location(""), "camera1"));
        MobileSensor sensor = (MobileSensor) manager.getSensor("mobileCamera1");
        if (!sensor.mobile || !"1".equals(sensor.getLocationSource()))
            throw new Exception("Error in mobile sensor flow");
    }

    private static void testFetchSensor() throws Exception {
        Sensor sensorFromId = manager.getSensor(1);
        Sensor sensorFromName = manager.getSensor("camera1");

        if (!sensorFromName.equals(sensorFromId))
            throw new Exception("Error fetching sensor");
    }

    private static void testStoreData() throws Exception {
        // store 100 rows of data for each sensor type
        for (int i = 0; i < 10; i++) {
            List<ObservedAttribute> values = new LinkedList<>();
            values.add(new ObservedAttribute("picture", "bytea", String.format("\\xDEAD%02d", i)));
            manager.storeObservation(new Observation(i % 5 + 1, LocalDateTime.now(), values));
        }
        for (int i = 0; i < 100; i++) {
            List<ObservedAttribute> values = new LinkedList<>();
            values.add(new ObservedAttribute("macAddress", "VARCHAR(50)", String.format("01:23:45:%02d:%02d", i, i)));
            manager.storeObservation(new Observation(i % 5 + 6, LocalDateTime.now(), values));
        }
    }

    private static void testFetchData() throws Exception {
        List<Observation> testData = getTestData();
        // fetch some rows
        List<String> predicates = new LinkedList<>();
        predicates.add("time > '2021-05-21 18:52:26.932'");
        List<Observation> observations = manager.getObservations("WIFI_AP", predicates);

        for (int i = 0; i < testData.size(); i++) {
            if (!testData.get(i).equals(observations.get(i)))
                throw new AssertionError("Predicates are probably not what we expected");
        }
    }

    private static List<Observation> getTestData() {
        List<Observation> testAttributes = new LinkedList<>();
        testAttributes.add(createWifiObservation(7, "2021-05-21T18:52:26.933", "01:23:45:96:96"));
        testAttributes.add(createWifiObservation(8, "2021-05-21T18:52:26.934", "01:23:45:97:97"));
        testAttributes.add(createWifiObservation(9, "2021-05-21T18:52:26.934", "01:23:45:98:98"));
        testAttributes.add(createWifiObservation(10, "2021-05-21T18:52:26.935", "01:23:45:99:99"));
        return testAttributes;
    }

    private static Observation createWifiObservation(int sensorId, String timeString, String macAddress) {
        return createWifiObservation(sensorId, LocalDateTime.parse(timeString), macAddress);
    }

    private static Observation createWifiObservation(int sensorId, LocalDateTime time, String macAddress) {
        List<ObservedAttribute> attributes = new LinkedList<>();
        attributes.add(new ObservedAttribute("macAddress", "VARCHAR(50)", macAddress));
        return new Observation(sensorId, time, attributes);
    }

    private static void testPlatformCreateAndFetch() throws Exception {
        List<Sensor> sensors = new LinkedList<>();
        sensors.add(new Sensor("platformCamera1", 7, new Location(""), new Location("")));
        sensors.add(new Sensor("platformCamera2", 7, new Location(""), new Location("")));
        sensors.add(new Sensor("platformWifi1", 8, new Location(""), new Location("")));
        MobilePlatform platform = new MobilePlatform("platform1", sensors, "platformCamera1");
        manager.createMobilePlatform(platform);

        MobilePlatform newPlatform = (MobilePlatform) manager.getPlatform("platform1");
        if (!newPlatform.equals(platform)
                || !(newPlatform.mobile == platform.mobile)
                || !(newPlatform.components.size() == platform.components.size()))
            throw new Exception("Error creating platform");
    }

    private static void testPredicates() throws Exception {
        Predicate filter = new Predicate("field1", Condition.EQUAL, "value1");
        filter.initialiseOperator(RelationalOperator.AND);
        filter.addChild(new Predicate("level1_field2", Condition.GREATER_EQ, "level1_value2"));
        filter.addChild(new Predicate("level1_field1", Condition.GREATER_EQ, "level1_value1"));

        Predicate child3 = new Predicate("level1_field3", Condition.LESSER, "level1_value3");
        child3.initialiseOperator(RelationalOperator.OR);
        child3.addChild(new Predicate("level2_field1", Condition.LESSER, "level2_value1"));
        child3.addChild(new Predicate("level2_field2", Condition.NOT_EQUAL, "level2_value2"));

        filter.addChild(child3);

        Predicate notWrapper = Predicate.getWrapper(RelationalOperator.NOT);
        notWrapper.addChild(filter);

        String resultFilter = notWrapper.toSql();
        String expectedFilter = "( NOT ((field1 = value1 AND (" +
                "(level1_field1 >= level1_value1) AND (level1_field2 >= level1_value2) AND (" +
                    "level1_field3 < level1_value3 OR ((level2_field1 < level2_value1) OR (level2_field2 <> level2_value2))" +
                ")))))";

        if (!expectedFilter.equals(resultFilter))
            throw new Exception("Error converting predicates to string");
    }

    private static void testPredicateQuery() throws Exception {
        Predicate filter = Predicate.getWrapper(RelationalOperator.AND);
        filter.addChild(new Predicate("time", Condition.GREATER, "'2021-05-21 18:52:26.922'"));
        filter.addChild(new Predicate("sensor", Condition.EQUAL, "10"));

        List<Observation> result = manager.getObservations("WIFI_AP", filter);
        if (result.size() != 4)
            throw new Exception("Incorrect count, predicate query not working as expected");
    }

    private static void testCreate50Sensors() throws Exception {
        for (int i = 5; i <= 55; i++) {
            manager.createSensor(new Sensor("wifi" + i, 8,
                    new Location(String.format("a%d,b%d,c%d", i, i, i)), new Location(String.format("d%d,e%d,f%d", i, i, i))));
        }
    }

    private static void storeMillionRows() throws Exception {
        for (int i = 0; i < 1000000; i++)
            manager.storeObservation(createWifiObservation(i % 50 + 44, getDate(i), getMacAddress(i)));
    }

    private static void testPredicateQuery2() throws Exception {
        Predicate filter = new Predicate("macaddress", Condition.EQUAL, "'87:87:87:87'");
        filter.initialiseOperator(RelationalOperator.AND);
        Predicate orWrapper = Predicate.getWrapper(RelationalOperator.OR);
        filter.addChild(orWrapper);

        Predicate sensor1 = Predicate.getWrapper(RelationalOperator.AND);
        sensor1.addChild(new Predicate("sensor", Condition.EQUAL, "64"));
        sensor1.addChild(new Predicate("time", Condition.GREATER_EQ, "'2021-06-15'"));
        orWrapper.addChild(sensor1);

        Predicate sensor2 = Predicate.getWrapper(RelationalOperator.AND);
        sensor2.addChild(new Predicate("sensor", Condition.EQUAL, "49"));
        sensor2.addChild(new Predicate("time", Condition.GREATER_EQ, "'2021-06-30'"));
        orWrapper.addChild(sensor2);

        List<Observation> result = manager.getObservations("WIFI_AP", filter);
        if (!(result.size() == 434))
            throw new Exception("Incorrect count, predicate query not working as expected");
    }

    private static void testPredicateQuery3() throws Exception {
        Predicate filter = new Predicate("time", Condition.GREATER, "'2021-06-30'");
        filter.initialiseOperator(RelationalOperator.AND);
        Predicate orWrapper = Predicate.getWrapper(RelationalOperator.OR);
        filter.addChild(orWrapper);

        Predicate person1 = Predicate.getWrapper(RelationalOperator.AND);
        person1.addChild(new Predicate("sensor", Condition.EQUAL, "64"));
        person1.addChild(new Predicate("macaddress", Condition.EQUAL, "'14:14:14:14'"));
        orWrapper.addChild(person1);

        Predicate person2 = Predicate.getWrapper(RelationalOperator.AND);
        person2.addChild(new Predicate("sensor", Condition.EQUAL, "49"));
        person2.addChild(new Predicate("macaddress", Condition.EQUAL, "'C3:C3:C3:C3'"));
        orWrapper.addChild(person2);

        List<Observation> result = manager.getObservations("WIFI_AP", filter);
        if (!(result.size() == 84))
            throw new Exception("Incorrect count, predicate query not working as expected");
    }

    private static void testPredicate_OccupancyOverlap() throws Exception {
        Predicate filter = new Predicate("sensor", Condition.EQUAL, "50");
        filter.initialiseOperator(RelationalOperator.AND);

        Predicate timeFilter = Predicate.getWrapper(RelationalOperator.AND);
        timeFilter.addChild(new Predicate("time", Condition.GREATER_EQ, "'2021-06-30'"));
        timeFilter.addChild(new Predicate("time", Condition.LESSER, "'2021-06-30 00:30'"));

        Predicate persons = Predicate.getWrapper(RelationalOperator.OR);
        persons.addChild(new Predicate("macaddress", Condition.EQUAL, "'92:92:92:92'"));
        persons.addChild(new Predicate("macaddress", Condition.EQUAL, "'60:60:60:60'"));

        filter.addChild(timeFilter);
        filter.addChild(persons);

        List<Observation> result = manager.getObservations("WIFI_AP", filter);
        if (!(result.size() == 2))
            throw new Exception("Incorrect count, predicate query not working as expected");
    }

    private static String getMacAddress(int number) {
        number = number % 255;
        return String.format("%02X:%02X:%02X:%02X", number, number, number, number);
    }

    private static LocalDateTime getDate(int number) {
        return LocalDateTime.now().minusDays(20).plusSeconds(number);
    }
}
