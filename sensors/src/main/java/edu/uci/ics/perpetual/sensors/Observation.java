package edu.uci.ics.perpetual.sensors;

import java.time.LocalDateTime;
import java.util.List;

public class Observation {
    int sensorId;
    LocalDateTime time;
    List<ObservedAttribute> attributes;

    public Observation(int sensorId, LocalDateTime time, List<ObservedAttribute> attributes) {
        this.sensorId = sensorId;
        this.time = time;
        this.attributes = attributes;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj || this.getClass() != obj.getClass())
            return false;

        final Observation that = (Observation) obj;
        if (this.sensorId != that.sensorId
                || !this.time.equals(that.time)
                || this.attributes.size() != that.attributes.size())
            return false;

        for (int i = 0; i < this.attributes.size(); i++)
            if (!this.attributes.get(i).equals(that.attributes.get(i)))
                return false;

        return true;
    }
}
