package edu.uci.ics.perpetual.sensors;

import edu.uci.ics.perpetual.util.Pair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

public class SqlAdapter {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

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
                    LocalDateTime.from(formatter.parse(row.getString("time"))),
                    attributes);
        } catch (SQLException ignored) {
            return null;
        }
    }
}
