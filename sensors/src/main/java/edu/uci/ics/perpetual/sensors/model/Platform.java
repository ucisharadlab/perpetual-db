package edu.uci.ics.perpetual.sensors.model;

import java.util.List;

public class Platform {
    public int id;
    public String name;
    public boolean mobile;
    public List<Sensor> components;

    public Platform(int id, String name, boolean mobile, List<Sensor> components) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.components = components;
    }

    public Platform(String name, List<Sensor> components) {
        this(-1, name, false, components);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (null == obj || obj.getClass() != this.getClass())
            return false;

        Platform that = (Platform) obj;
        return this.name.equals(that.name); // TODO: Is the rest required? && this.mobile == that.mobile;
    }
}
