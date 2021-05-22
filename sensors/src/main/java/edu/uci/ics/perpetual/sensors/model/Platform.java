package edu.uci.ics.perpetual.sensors.model;

import java.util.List;

public class Platform {
    public String name;
    public List<Sensor> components;

    Platform(String name, List<Sensor> components) {
        this.name = name;
        this.components = components;
    }
}
