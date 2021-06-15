package edu.uci.ics.perpetual.sensors.model;

import java.time.LocalDateTime;

public class MobileSensor extends Sensor implements MobileObject{
    private int locationSourceId;

    public MobileSensor(int id, String name, int typeId, int platformId, boolean mobile, Location location, Location viewArea, String spec) {
        super(id, name, typeId, platformId, true, location, viewArea, spec);
    }

    public MobileSensor(String name, int typeId, int platformId, boolean mobile, Location location, Location viewArea, String spec) {
        super(name, typeId, platformId, true, location, viewArea, spec);
    }

    public MobileSensor(Sensor sensor, int locationSourceId) {
        super(sensor.id, sensor.name, sensor.typeId, sensor.platformId, true, sensor.location, sensor. viewArea, sensor.spec);
        this.locationSourceId = locationSourceId;
    }

    @Override
    public int getLocationSource() {
        return this.locationSourceId;
    }

    @Override
    public Location getLocation(LocalDateTime start, LocalDateTime end) {
        return new Location("");
    }
}
