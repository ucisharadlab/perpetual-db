package edu.uci.ics.perpetual.sensors;

import edu.uci.ics.perpetual.sensors.model.SensorType;

public class TestCacheManager {
    static SensorManager manager = new CachedSensorManager();

    public static void testCachedSensorType() throws Exception {
        SensorType wifi = manager.getSensorType("WIFI_AP");
        SensorType wifi_copy = manager.getSensorType("WIFI_AP");
        manager.createSensorType("WIFI_AP1", "MAC_Address1");
        SensorType wifi1 = manager.getSensorType("WIFI_AP1");
        SensorType wifi1_copy = manager.getSensorType("WIFI_AP1");
    }
}
