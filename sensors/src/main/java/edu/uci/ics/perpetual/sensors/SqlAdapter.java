package edu.uci.ics.perpetual.sensors;

import edu.uci.ics.perpetual.util.Pair;

import java.sql.ResultSet;
import java.sql.SQLException;

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
}
