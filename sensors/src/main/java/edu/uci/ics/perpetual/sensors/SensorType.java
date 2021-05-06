package edu.uci.ics.perpetual.sensors;

public class SensorType {
    String name;
    String observationType;

    public SensorType(String name, String observationType) {
        this.name = name;
        this.observationType = observationType;
    }

    public String getDataTableName() {
        return name + "_data";
    }
}
