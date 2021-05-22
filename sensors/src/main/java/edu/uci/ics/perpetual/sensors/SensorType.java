package edu.uci.ics.perpetual.sensors;

public class SensorType {
    int id;
    public String name;
    public String observationType;

    public SensorType(int id, String name, String observationType) {
        this.id = id;
        this.name = name;
        this.observationType = observationType;
    }

    public SensorType(String name, String observationType) {
        this(-1, name, observationType);
    }

    public String getDataTableName() {
        return name + "_data";
    }
}
