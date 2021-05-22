package edu.uci.ics.perpetual.sensors;

import java.time.LocalDateTime;

public class MobileSensor extends Sensor implements MobileObject{
    public MobileSensor(int id, String name, int typeId, String platformName, int locationSourceId, Location location, Location viewArea, String spec) {
        super(id, name, typeId, platformName, locationSourceId, location, viewArea, spec);
    }

    public MobileSensor(String name, int typeId, String platformName, int locationSourceId, Location location, Location viewArea, String spec) {
        super(name, typeId, platformName, locationSourceId, location, viewArea, spec);
    }

    @Override
    public int getLocationSource() {
        return locationSourceId;
    }

    @Override
    public Location getLocation(LocalDateTime start, LocalDateTime end) {
        return new Location("");
    }
}
