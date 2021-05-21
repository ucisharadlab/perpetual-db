package edu.uci.ics.perpetual.sensors;

import edu.uci.ics.perpetual.util.Pair;
import org.apache.log4j.BasicConfigurator;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class TestManager {
    static SensorManager manager = new SensorManager();

    public static void main(String[] args) throws Exception {
        BasicConfigurator.configure();
//        testCreateTypes();
//        testCreateSensors();
        testStoreAndFetchData();
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

    private static void testStoreAndFetchData() throws Exception {
        // store 100 rows of data for each sensor type
        for (int i = 0; i < 100; i++) {
            List<ObservedAttribute> values = new LinkedList<>(); // Todo: fill in default values
            manager.storeObservation(i % 5 + 1, LocalDateTime.now(), values);
        }
        // fetch some rows
        manager.fetchObservations();
    }
}
