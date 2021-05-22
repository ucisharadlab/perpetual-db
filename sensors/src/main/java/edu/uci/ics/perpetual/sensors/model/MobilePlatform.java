package edu.uci.ics.perpetual.sensors.model;

import java.time.LocalDateTime;
import java.util.List;

public class MobilePlatform extends Platform implements MobileObject{
    public int locationSource;

    MobilePlatform(String name, List<Sensor> components) {
        super(name, components);
    }

    @Override
    public int getLocationSource() {
        return locationSource;
    }

    @Override
    public Location getLocation(LocalDateTime start, LocalDateTime end) {
        return new Location("");
    }
}
