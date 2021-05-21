package edu.uci.ics.perpetual.sensors;

import edu.uci.ics.perpetual.util.Pair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ObservationType {
    String name;
    List<Pair<String, String>> attributes;

    public ObservationType(String name, List<Pair<String, String>> attributes) {
        this.name = name;
        this.attributes = attributes;
    }
}
