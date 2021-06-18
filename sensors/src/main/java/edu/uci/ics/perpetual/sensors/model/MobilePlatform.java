package edu.uci.ics.perpetual.sensors.model;

import java.time.LocalDateTime;
import java.util.List;

public class MobilePlatform extends Platform implements MobileObject{
    public String locationSource;

    public MobilePlatform(String name, List<Sensor> components, String locationSource) {
        super(name, components);
        this.mobile = true;
        this.locationSource = locationSource;
    }

    public MobilePlatform(Platform platform, String locationSource) {
        super(platform.id, platform.name, true, platform.components);
        this.locationSource = locationSource;
    }

    @Override
    public String getLocationSource() {
        return locationSource;
    }

    @Override
    public Location getLocation(LocalDateTime start, LocalDateTime end) {
        return new Location("");
    }
}
