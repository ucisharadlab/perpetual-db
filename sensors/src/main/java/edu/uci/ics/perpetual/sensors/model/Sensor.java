package edu.uci.ics.perpetual.sensors.model;

public class Sensor {
    public int id;
    public String name;
    public int typeId;
    public int platformId;
    public boolean mobile;
    public Location location;
    public Location viewArea;
    public String spec;

    public static final int UNSET = -1; // TODO: Use NULL for DB instead?

    public Sensor(int id, String name, int typeId, int platformId, boolean mobile, Location location, Location viewArea, String spec) {
        this.id = id;
        this.name = name;
        this.typeId = typeId;
        this.platformId = platformId;
        this.mobile = mobile;
        this.location = location;
        this.viewArea = viewArea;
        this.spec = spec;
    }

    public Sensor(String name, int typeId, int platformId, boolean mobile, Location location, Location viewArea, String spec) {
        this(UNSET, name, typeId, platformId, mobile, location, viewArea, spec);
    }

    public Sensor(String name, int typeId, Location location, Location viewArea) {
        this(name, typeId, UNSET, false, location, viewArea, "");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (null == obj || obj.getClass() != this.getClass())
            return false;

        Sensor that = (Sensor) obj;
        return this.name.equals(that.name) && this.typeId == that.typeId && this.platformId == that.platformId
                && this.mobile == that.mobile; // TODO: Are things other than id/name comparison required? && this.location.equals(that.location)
    }
}
