package edu.uci.ics.perpetual.sensors;

import edu.uci.ics.perpetual.sensors.model.*;
import edu.uci.ics.perpetual.sensors.predicate.Predicate;
import edu.uci.ics.perpetual.util.Pair;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface SensorManager {
    void createObservationType(String name, List<Pair<String, String>> attributes) throws Exception;
    ObservationType getObservationType(String name) throws ExecutionException;
    List<String> getObservationTypesByAttribute(String attributeName) throws ExecutionException;
    void createSensorType(String name, String typeName) throws Exception;
    SensorType getSensorType(String name) throws ExecutionException;
    List<SensorType> getSensorTypesByObservation(String name) throws Exception;
    void createSensor(Sensor sensor) throws Exception;
    void createMobileSensor(MobileSensor sensor) throws Exception;
    void createPlatform(Platform platform) throws Exception;
    void createMobilePlatform(MobilePlatform platform) throws Exception;
    Sensor getSensor(String name) throws ExecutionException;
    Sensor getSensor(int id);
    Platform getPlatform(String name) throws ExecutionException;
    List<Sensor> getSensorNamesByObservationType(String typeName) throws Exception;
    List<Sensor> getSensorsByType(String typeName) throws Exception;
    void storeObservation(SensorType type, Observation observation) throws Exception;
    void storeObservation(Observation observation) throws Exception;
    List<Observation> getObservations(String sensorType, List<String> predicates);
    List<Observation> getObservations(SensorType type, List<String> predicates);
    List<Observation> getObservations(String sensorType, Predicate predicate);
    List<Pair<LocalDateTime, Location>> getLocations(int sensorId, LocalDateTime start, LocalDateTime end);
    List<Sensor> getAvailableSensors(Location location, LocalDateTime start, LocalDateTime end);
}
