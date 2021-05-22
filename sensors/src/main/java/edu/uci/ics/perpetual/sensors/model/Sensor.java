package edu.uci.ics.perpetual.sensors.model;

import edu.uci.ics.perpetual.sensors.model.Location;

public class Sensor {
    int id;
    public String name;
    public int typeId;
    public String platformName;
    public int locationSourceId;
    public Location location;
    public Location viewArea;
    public String spec;

    public static final int UNSET = -1;

    public Sensor(int id, String name, int typeId, String platformName, int locationSourceId, Location location, Location viewArea, String spec) {
        this.id = id;
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
