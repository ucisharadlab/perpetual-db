package edu.uci.ics.perpetual.sensors;

import java.time.LocalDateTime;

public interface MobileObject {
    int getLocationSource();
    Location getLocation(LocalDateTime start, LocalDateTime end);
}
