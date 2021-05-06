package edu.uci.ics.perpetual.sensors;

import edu.uci.ics.perpetual.util.Pair;

import java.util.List;

public class SensorManager {
    private SensorRepository repo = SensorRepository.getInstance();

    public void createObservationType(String name, List<Pair<String, String>> attributes) throws Exception {
        ObservationType type = new ObservationType(name, attributes);
        repo.insertObservationType(type);
    }

    public void createSensorType(String name, String observationType) throws Exception {
        SensorType type = new SensorType(name, observationType);
        repo.insertSensorType(type);
        repo.createTable(type.getDataTableName());
    }

    public void createSensor() {}

    public void storeObservation() {}

    public void fetchObservations() {}

}
