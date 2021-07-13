package edu.uci.ics.perpetual.sensors;

import edu.uci.ics.perpetual.sensors.model.*;
import edu.uci.ics.perpetual.util.Pair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class SqlAdapter {

    public static Pair<String, String> convertToAttribute(ResultSet row) {
        try {
            return new Pair<>(row.getString("attribute"), row.getString("valueType"));
        } catch (SQLException ignored) {
            return null; // Todo: Change to log the error
        }
    }

    public static SensorType sensorTypeFromRow(ResultSet row) {
        try {
            return new SensorType(row.getInt("id"),
                    row.getString("name"),
                    row.getString("observationType"));
        } catch (SQLException ignored) {
            return null;
        }
    }

    public static Observation convertToObservation(ResultSet row, ObservationType type) {
        try {
            List<ObservedAttribute> attributes = new LinkedList<>();
            for (Pair<String, String> attribute : type.attributes)
                attributes.add(new ObservedAttribute(attribute.getKey(), attribute.getValue(), row.getString(attribute.getKey())));

            return new Observation(row.getInt("sensor"),
                    LocalDateTime.parse(row.getString("time").replace( " " , "T" )),
                    attributes);
        } catch (SQLException ignored) {
            return null;
        }
    }

    public static Sensor sensorFromRow(ResultSet row) {
        try {
            boolean mobile = row.getBoolean("mobile");
            Sensor sensor = new Sensor(row.getInt("id"), row.getString("name"), row.getInt("type"),
                    row.getInt("platformId"), mobile,
                    new Location(row.getString("location")), new Location(row.getString("viewArea")),
                    row.getString("spec"));
            return mobile ? new MobileSensor(sensor, row.getString("locationSource")) : sensor;
        } catch (SQLException ignored) {
            return null;
        }
    }

    public static Platform platformFromRow(ResultSet row) {
        try {
            boolean mobile = row.getBoolean("mobile");
            Platform platform = new Platform(row.getInt("id"), row.getString("name"), mobile, null);
            return mobile ? new MobilePlatform(platform, row.getString("locationSource")) : platform;
        } catch (SQLException ignored) {
            return null; // TODO: Change all catch blocks to log the exception before returning null
        }
    }
}
