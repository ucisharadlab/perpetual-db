package edu.uci.ics.perpetual.sensors.model;

import edu.uci.ics.perpetual.sensors.model.Location;

import java.time.LocalDateTime;

public interface MobileObject {
    int getLocationSource();
    Location getLocation(LocalDateTime start, LocalDateTime end);
}
