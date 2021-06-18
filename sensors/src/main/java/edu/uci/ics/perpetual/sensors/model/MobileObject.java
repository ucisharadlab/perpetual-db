package edu.uci.ics.perpetual.sensors.model;

import edu.uci.ics.perpetual.sensors.model.Location;

import java.time.LocalDateTime;

public interface MobileObject {
    String getLocationSource();
    Location getLocation(LocalDateTime start, LocalDateTime end);
}
