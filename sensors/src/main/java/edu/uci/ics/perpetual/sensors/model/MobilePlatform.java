package edu.uci.ics.perpetual.sensors.model;

import java.time.LocalDateTime;
import java.util.List;

public class MobilePlatform extends Platform implements MobileObject{
    public int locationSource;

    public MobilePlatform(String name, List<Sensor> components) {
        super(name, components);
    }

    public MobilePlatform(Platform platform, int locationSource) {
        super(platform.name, platform.components);
        this.mobile = true;
        this.locationSource = locationSource;
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
