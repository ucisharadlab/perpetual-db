package edu.uci.ics.perpetual.sensors.model;

import java.time.LocalDateTime;

public class MobileSensor extends Sensor implements MobileObject{
    private String locationSourceId;

    public MobileSensor(int id, String name, int typeId, int platformId, Location location, Location viewArea, String spec, String locationSourceId) {
        super(id, name, typeId, platformId, true, location, viewArea, spec);
        this.locationSourceId = locationSourceId;
    }

    public MobileSensor(String name, int typeId, int platformId, Location location, Location viewArea, String spec, String locationSourceId) {
        this(Sensor.UNSET, name, typeId, platformId, location, viewArea, spec, locationSourceId);
    }

    public MobileSensor(String name, int typeId, Location viewArea, String locationSourceId) {
        this(name, typeId, UNSET, new Location(""), viewArea, "", locationSourceId);
    }

    public MobileSensor(Sensor sensor, String locationSourceId) {
        super(sensor.id, sensor.name, sensor.typeId, sensor.platformId, true, sensor.location, sensor. viewArea, sensor.spec);
        this.locationSourceId = locationSourceId;
    }

    @Override
    public String getLocationSource() {
        return this.locationSourceId;
    }

    @Override
    public Location getLocation(LocalDateTime start, LocalDateTime end) {
        return new Location("");
    }
}
