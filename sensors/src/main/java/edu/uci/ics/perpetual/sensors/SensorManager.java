package edu.uci.ics.perpetual.sensors;

import edu.uci.ics.perpetual.util.Pair;

import java.time.LocalDateTime;
import java.util.List;

public class SensorManager {
    private final SensorRepository repo = SensorRepository.getInstance();

    public void createObservationType(String name, List<Pair<String, String>> attributes) throws Exception {
        ObservationType type = new ObservationType(name, attributes);
        repo.insertObservationType(type);
    }

    public ObservationType fetchObservationType(String name) {
        return repo.fetchObservationType(name);
    }

    public void createSensorType(String name, String typeName) throws Exception {
        SensorType type = new SensorType(name, typeName);
//        repo.insertSensorType(type);
        ObservationType observationType = fetchObservationType(typeName);
        repo.createObservationsTable(type.getDataTableName(), observationType);
    }

    public void createSensor(Sensor sensor) throws Exception {
        repo.insertSensor(sensor);
    }

    public void storeObservation(int sensorId, LocalDateTime time, List<ObservedAttribute> values) throws Exception {
        SensorType type = repo.getSensorTypeFromSensor(sensorId);
        repo.insertObservation(type.getDataTableName(), sensorId, time, values);
    }

    public void fetchObservations() {}

}
