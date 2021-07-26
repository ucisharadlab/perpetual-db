package edu.uci.ics.perpetual.geo;

import edu.uci.ics.perpetual.geo.model.*;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class SqlAdapter {

    public static Coordinate coordinatesFromRow(ResultSet row) {
        try {
            String coordinateSystem = row.getString(1);
            String lat = row.getString(2);
            String lng = row.getString(3);
            if (coordinateSystem.equalsIgnoreCase("Cartesian2hfd")) {
                String z = row.getString(4);
                return new Coordinate(lat, lng, z);
            }
            else {
                return new Coordinate(lat, lng);
            }
        } catch(SQLException ignored){
            return null;
        }
    }

    public static Space spaceFromRow(ResultSet row) {
        try {
            String space_id = row.getString(1);
            String parent_space_id = row.getString(2);
            String coordinate_system_name = row.getString(3);
            String space_shape = row.getString(4);
            return new Space(space_id, parent_space_id, coordinate_system_name, space_shape);
        } catch(SQLException ignored) {
            return null;
        }
        }
}