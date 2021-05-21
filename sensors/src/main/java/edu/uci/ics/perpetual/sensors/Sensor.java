package edu.uci.ics.perpetual.sensors;

public class Sensor {
    int id;
    String name;
    int typeId;
    String platformName;
    int locationSourceId;
    Location location;
    Location viewArea;
    String spec;

    public static final int UNSET = -1;

    public Sensor(int id, String name, int typeId, String platformName, int locationSourceId, Location location, Location viewArea, String spec) {
        this.name = name;
        this.typeId = typeId;
        this.platformName = platformName;
        this.locationSourceId = locationSourceId;
        this.location = location;
        this.viewArea = viewArea;
        this.spec = spec;
    }

    public Sensor(String name, int typeId, String platformName, int locationSourceId, Location location, Location viewArea, String spec) {
        this(UNSET, name, typeId, platformName, locationSourceId, location, viewArea, spec);
    }
}
