package edu.uci.ics.perpetual.sensors;

import edu.uci.ics.perpetual.sensors.model.*;
import edu.uci.ics.perpetual.sensors.predicate.Predicate;
import edu.uci.ics.perpetual.util.Pair;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class DbSensorManager implements SensorManager {
    private final SensorRepository repo = SensorRepository.getInstance();

    public void createObservationType(String name, List<Pair<String, String>> attributes) throws Exception {
        ObservationType type = new ObservationType(name, attributes);
        repo.insertObservationType(type);
    }

    public ObservationType getObservationType(String name) {
        return repo.fetchObservationType(name);
    }

    public List<String> getObservationTypesByAttribute(String attributeName) {
        return repo.fetchObservationTypeNames(attributeName);
    }

    public void createSensorType(String name, String typeName) throws Exception {
        SensorType type = new SensorType(name, typeName);
        repo.insertSensorType(type);
        repo.createObservationsTable(type.getDataTableName(), getObservationType(typeName));
    }

    public List<SensorType> getSensorTypesByObservation(String name) throws Exception {
        return repo.fetchSensorTypesByObservation(name);
    }

    public SensorType getSensorType(String name) {
        return repo.getSensorType(name);
    }

    public void createSensor(Sensor sensor) throws Exception {
        repo.insertSensor(sensor);
    }

    public void createMobileSensor(MobileSensor sensor) throws Exception {
        repo.insertSensor(sensor);
        int newId = repo.getNewSensorId(sensor.name);
        repo.insertMobileObject(newId, "Sensor", sensor.getLocationSource());
    }

    public void createPlatform(Platform platform) throws Exception {
        repo.insertPlatform(platform);
        platform.id = repo.getNewPlatformId(platform.name);
        for (Sensor sensor : platform.components) {
            sensor.platformId = platform.id;
            repo.insertSensor(sensor);
        }
    }

    public void createMobilePlatform(MobilePlatform platform) throws Exception {
        for (Sensor sensor : platform.components) {
            sensor.mobile = true;
        }
        createPlatform(platform);
        repo.insertMobileObject(platform.id, "Platform", platform.getLocationSource());
    }

    public Sensor getSensor(String name) {
        return repo.getSensor(name);
    }

    public Sensor getSensor(int id) {
        return repo.getSensor(id);
    }

    public Platform getPlatform(String name) {
        return repo.getPlatform(name);
    }

    public List<Sensor> getSensorNamesByObservationType(String typeName) throws Exception {
        List<SensorType> sensorTypes = getSensorTypesByObservation(typeName);
        List<Sensor> sensors = new LinkedList<>();
        for (SensorType sensorType : sensorTypes)
            sensors.addAll(getSensorsByType(sensorType.name));
        return sensors;
    }

    public List<Sensor> getSensorsByType(String typeName) throws Exception {
        return repo.fetchSensorNamesByType(typeName);
    }

    public void storeObservation(SensorType type, Observation observation) throws Exception {
        repo.insertObservation(type.getDataTableName(), observation.sensorId, observation.time, observation.attributes);
    }

    public void storeObservation(Observation observation) throws Exception {
        storeObservation(repo.getSensorTypeFromSensor(observation.sensorId), observation);
    }

    public List<Observation> getObservations(String sensorType, List<String> predicates) {
        return getObservations(getSensorType(sensorType), predicates);
    }

    public List<Observation> getObservations(SensorType type, List<String> predicates) {
        return repo.getObservations(type.getDataTableName(), predicates, getObservationType(type.observationType));
    }

    public List<Observation> getObservations(String sensorType, Predicate predicate) {
        SensorType type = getSensorType(sensorType);
        return repo.getObservations(type.getDataTableName(), predicate, getObservationType(type.observationType));
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
