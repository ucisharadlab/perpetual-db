package edu.uci.ics.perpetual.sensors;

import edu.uci.ics.perpetual.sensors.model.*;
import edu.uci.ics.perpetual.util.Pair;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class SensorManager {
    private final SensorRepository repo = SensorRepository.getInstance();

    public void createObservationType(String name, List<Pair<String, String>> attributes) throws Exception {
        ObservationType type = new ObservationType(name, attributes);
        repo.insertObservationType(type);
    }

    public ObservationType getObservationType(String name) {
        return repo.fetchObservationType(name);
    }

    public void createSensorType(String name, String typeName) throws Exception {
        SensorType type = new SensorType(name, typeName);
        repo.insertSensorType(type);
        repo.createObservationsTable(type.getDataTableName(), getObservationType(typeName));
    }

    public void createSensor(Sensor sensor) throws Exception {
        repo.insertSensor(sensor);
    }

    public void createMobileSensor(MobileSensor sensor) throws Exception {
        repo.insertSensor(sensor);
    }

    public void createPlatform(Platform platform) throws Exception {
        for (Sensor sensor : platform.components) {
            sensor.platformName = platform.name;
            repo.insertSensor(sensor);
        }
    }

    public void createMobilePlatform(MobilePlatform platform) throws Exception {
        for (Sensor sensor : platform.components) {
            sensor.locationSourceId = platform.locationSource;
        }
        createPlatform(platform);
    }

    public Sensor getSensor(String name) {
        Sensor sensor = repo.getSensor(name);
        if (Sensor.UNSET != sensor.locationSourceId)
            return new MobileSensor(sensor);
        return sensor;
    }

    public Sensor getSensor(int id) {
        Sensor sensor = repo.getSensor(id);
        if (Sensor.UNSET != sensor.locationSourceId)
            return new MobileSensor(sensor);
        return sensor;
    }

    public Platform getPlatform(String name) {
        List<Sensor> components = repo.getPlatformComponents(name);
        if (Sensor.UNSET != components.get(0).locationSourceId)
            return new MobilePlatform(name, components);
        return new Platform(name, components);
    }

    public void storeObservation(SensorType type, Observation observation) throws Exception {
        repo.insertObservation(type.getDataTableName(), observation.sensorId, observation.time, observation.attributes);
    }

    public void storeObservation(Observation observation) throws Exception {
        storeObservation(repo.getSensorTypeFromSensor(observation.sensorId), observation);
    }

    public List<Observation> getObservations(String sensorType, List<String> predicates) {
        return getObservations(repo.getSensorType(sensorType), predicates);
    }

    public List<Observation> getObservations(SensorType type, List<String> predicates) {
        return repo.getObservations(type.getDataTableName(), predicates, getObservationType(type.observationType));
    }

    public List<Pair<LocalDateTime, Location>> getLocations(int sensorId, LocalDateTime start, LocalDateTime end) {
        // return temporal map of locations where the sensor was situated in the time range
        return null;
    }

    public List<Sensor> getAvailableSensors(Location location, LocalDateTime start, LocalDateTime end) {
        // return available sensors at that location in the time range
        return new LinkedList<>();
    }
}
