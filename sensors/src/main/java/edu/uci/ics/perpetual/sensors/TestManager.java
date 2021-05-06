package edu.uci.ics.perpetual.sensors;

import java.util.LinkedList;

public class TestManager {
    public static void main() throws Exception {
        SensorManager manager = new SensorManager();
        // create macId (string) observation type
        manager.createObservationType("MAC Address", new LinkedList<>());
        // create image (blob) observation type
        manager.createObservationType("Image", new LinkedList<>());

        // create camera sensor type, they generate image observation type
        manager.createSensorType("Camera", "Image");
        // create wifi ap sensor type, generates macID observations
        manager.createSensorType("WIFI AP", "MAC Address");

        // create 5 camera sensors: location, view frustrum, etc
        // create 5 wifi ap sensors

        // store 100 rows of data for these sensors
        // fetch some rows
    }
}
