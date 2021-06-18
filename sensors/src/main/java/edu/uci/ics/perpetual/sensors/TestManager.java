package edu.uci.ics.perpetual.sensors;

import edu.uci.ics.perpetual.sensors.model.*;
import edu.uci.ics.perpetual.sensors.predicate.*;
import edu.uci.ics.perpetual.util.Pair;
import org.apache.log4j.BasicConfigurator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

public class TestManager {
    static SensorManager manager = new SensorManager();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public static void main(String[] args) throws Exception {
        BasicConfigurator.configure();
//        testCreateTypes();
//        testCreateSensors();
//        testMobileSensor();
//        testFetchSensor();
//        testStoreData();
//        testFetchData();
//        testPlatformCreateAndFetch();
        testPredicates();
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
            manager.createSensor(new Sensor("camera" + i, 1,
                    new Location(String.format("a%d,b%d,c%d", i, i, i)), new Location(String.format("d%d,e%d,f%d", i, i, i))));
        }

        // create 5 wifi ap sensors
        for (int i = 1; i <= 5; i++) {
            manager.createSensor(new Sensor("wifi" + i, 2,
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
        testAttributes.add(createWifiObservation(7, "2021-05-21 18:52:26.933", "01:23:45:96:96"));
        testAttributes.add(createWifiObservation(8, "2021-05-21 18:52:26.934", "01:23:45:97:97"));
        testAttributes.add(createWifiObservation(9, "2021-05-21 18:52:26.934", "01:23:45:98:98"));
        testAttributes.add(createWifiObservation(10, "2021-05-21 18:52:26.935", "01:23:45:99:99"));
        return testAttributes;
    }

    private static Observation createWifiObservation(int sensorId, String timeString, String macAddress) {
        List<ObservedAttribute> attributes = new LinkedList<>();
        attributes.add(new ObservedAttribute("macAddress", "VARCHAR(50)", macAddress));
        return new Observation(sensorId, LocalDateTime.from(formatter.parse(timeString)), attributes);
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

        filter.children = new LinkedList<>();
        filter.childOperator = new RelationalAnd();
        filter.children.add(new Predicate("level1_field1", Condition.GREATER_EQ, "level1_value1"));
        filter.children.add(new Predicate("level1_field2", Condition.GREATER_EQ, "level1_value2"));

        Predicate child3 = new Predicate("level1_field3", Condition.LESSER, "level1_value3");
        child3.children = new LinkedList<>();
        child3.childOperator = new RelationalOr();
        child3.children.add(new Predicate("level2_field1", Condition.LESSER, "level2_value1"));
        child3.children.add(new Predicate("level2_field2", Condition.NOT_EQUAL, "level2_value2"));

        filter.children.add(child3);

        Predicate notWrapper = new Predicate();
        notWrapper.children = new LinkedList<>();
        notWrapper.children.add(filter);
        notWrapper.childOperator = new RelationalNot();

        String resultFilter = notWrapper.toSql();
        String expectedFilter = "( NOT ((field1 = value1 AND (" +
                "(level1_field1 >= level1_value1) AND (level1_field2 >= level1_value2) AND (" +
                    "level1_field3 < level1_value3 OR ((level2_field1 < level2_value1) OR (level2_field2 <> level2_value2))" +
                ")))))";

        if (!expectedFilter.equals(resultFilter))
            throw new Exception("Error converting predicates to string");
    }
}
