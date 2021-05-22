package edu.uci.ics.perpetual.sensors;

import edu.uci.ics.perpetual.sensors.model.Location;
import edu.uci.ics.perpetual.sensors.model.Observation;
import edu.uci.ics.perpetual.sensors.model.ObservedAttribute;
import edu.uci.ics.perpetual.sensors.model.Sensor;
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
//        testStoreData();
        testFetchData();
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
        manager.createSensor(new Sensor("camera1", 1, "", Sensor.UNSET,
                new Location("a1,b1,c1"), new Location("d1,e1,f1"), "{}"));
        manager.createSensor(new Sensor("camera2", 1, "", Sensor.UNSET,
                new Location("a2,b2,c2"), new Location("d2,e2,f2"), "{}"));
        manager.createSensor(new Sensor("camera3", 1, "", Sensor.UNSET,
                new Location("a3,b3,c3"), new Location("d3,e3,f3"), "{}"));
        manager.createSensor(new Sensor("camera4", 1, "", Sensor.UNSET,
                new Location("a4,b4,c4"), new Location("d4,e4,f4"), "{}"));
        manager.createSensor(new Sensor("camera5", 1, "", Sensor.UNSET,
                new Location("a5,b5,c5"), new Location("d5,e5,f5"), "{}"));

        // create 5 wifi ap sensors
        manager.createSensor(new Sensor("wifi1", 2, "", Sensor.UNSET,
                new Location("a1,b1,c1"), new Location("d1,e1,f1"), "{}"));
        manager.createSensor(new Sensor("wifi2", 2, "", Sensor.UNSET,
                new Location("a2,b2,c2"), new Location("d2,e2,f2"), "{}"));
        manager.createSensor(new Sensor("wifi3", 2, "", Sensor.UNSET,
                new Location("a3,b3,c3"), new Location("d3,e3,f3"), "{}"));
        manager.createSensor(new Sensor("wifi4", 2, "", Sensor.UNSET,
                new Location("a4,b4,c4"), new Location("d4,e4,f4"), "{}"));
        manager.createSensor(new Sensor("wifi5", 2, "", Sensor.UNSET,
                new Location("a5,b5,c5"), new Location("d5,e5,f5"), "{}"));
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
        List<Observation> observations = manager.fetchObservations("WIFI_AP", predicates);

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
}
