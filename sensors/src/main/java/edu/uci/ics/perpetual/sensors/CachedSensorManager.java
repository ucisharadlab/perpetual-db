package edu.uci.ics.perpetual.sensors;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import edu.uci.ics.perpetual.sensors.model.*;
import edu.uci.ics.perpetual.sensors.predicate.Predicate;
import edu.uci.ics.perpetual.util.Pair;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CachedSensorManager implements SensorManager {

    static SensorManager dbManager = new DbSensorManager(); // TODO: change to singleton

    @Override
    public void createObservationType(String name, List<Pair<String, String>> attributes) throws Exception {
        dbManager.createObservationType(name, attributes);
        cachedObservationTypes.refresh(name);
    }

    @Override
    public ObservationType getObservationType(String name) throws ExecutionException {
        return cachedObservationTypes.get(name);
    }

    @Override
    public List<String> getObservationTypesByAttribute(String attributeName) throws ExecutionException {
        return cachedObservationsByAttribute.get(attributeName);
    }

    @Override
    public void createSensorType(String name, String typeName) throws Exception {
        dbManager.createSensorType(name, typeName);
        cachedSensorTypes.refresh(name);
    }

    @Override
    public SensorType getSensorType(String name) throws ExecutionException {
        return cachedSensorTypes.get(name);
    }

    @Override
    public List<SensorType> getSensorTypesByObservation(String name) throws Exception {
        return cachedSensorTypesByObservation.get(name);
    }

    @Override
    public void createSensor(Sensor sensor) throws Exception {
        dbManager.createSensor(sensor);
        cachedSensors.put(sensor.name, sensor);
    }

    @Override
    public void createMobileSensor(MobileSensor sensor) throws Exception {
        dbManager.createMobileSensor(sensor);
        cachedSensors.put(sensor.name, sensor);
    }

    @Override
    public void createPlatform(Platform platform) throws Exception {
        dbManager.createPlatform(platform);
        cachedPlatforms.refresh(platform.name);
    }

    @Override
    public void createMobilePlatform(MobilePlatform platform) throws Exception {
        dbManager.createMobilePlatform(platform);
        cachedPlatforms.refresh(platform.name);
    }

    @Override
    public Sensor getSensor(String name) throws ExecutionException {
        return cachedSensors.get(name);
    }

    @Override
    public Sensor getSensor(int id) {
        return dbManager.getSensor(id);
    }

    @Override
    public Platform getPlatform(String name) throws ExecutionException {
        return cachedPlatforms.get(name);
    }

    @Override
    public List<Sensor> getSensorNamesByObservationType(String typeName) throws Exception {
        List<SensorType> sensorTypeNames = cachedSensorTypesByObservation.get(typeName);
        List<Sensor> sensors = new LinkedList<>();
        for (SensorType type : sensorTypeNames)
            sensors.addAll(cachedSensorsByType.get(type.name));
        return sensors;
    }

    @Override
    public List<Sensor> getSensorsByType(String typeName) throws Exception {
        return cachedSensorsByType.get(typeName);
    }

    @Override
    public void storeObservation(SensorType type, Observation observation) throws Exception {
        dbManager.storeObservation(type, observation);
    }

    @Override
    public void storeObservation(Observation observation) throws Exception {
        dbManager.storeObservation(observation);
    }

    @Override
    public List<Observation> getObservations(String sensorType, List<String> predicates) {
        return dbManager.getObservations(sensorType, predicates);
    }

    @Override
    public List<Observation> getObservations(SensorType type, List<String> predicates) {
        return dbManager.getObservations(type, predicates);
    }

    @Override
    public List<Observation> getObservations(String sensorType, Predicate predicate) {
        return dbManager.getObservations(sensorType, predicate);
    }

    @Override
    public List<Pair<LocalDateTime, Location>> getLocations(int sensorId, LocalDateTime start, LocalDateTime end) {
        return dbManager.getLocations(sensorId, start, end);
    }

    @Override
    public List<Sensor> getAvailableSensors(Location location, LocalDateTime start, LocalDateTime end) {
        return dbManager.getAvailableSensors(location, start, end);
    }

    static LoadingCache<String, ObservationType> cachedObservationTypes = CacheBuilder.newBuilder()
            .maximumSize(10000)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build(new CacheLoader<String, ObservationType>() {
                @Override
                public ObservationType load(String name) throws Exception {
                    return dbManager.getObservationType(name);
                }
            });

    static LoadingCache<String, List<String>> cachedObservationsByAttribute = CacheBuilder.newBuilder()
            .maximumSize(10000)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build(new CacheLoader<String, List<String>>() {
                @Override
                public List<String> load(String attr) throws Exception {
                    return dbManager.getObservationTypesByAttribute(attr);
                }
            });

    static LoadingCache<String, SensorType> cachedSensorTypes = CacheBuilder.newBuilder()
            .maximumSize(10000)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build(new CacheLoader<String, SensorType>() {
                @Override
                public SensorType load(String name) throws Exception {
                    return dbManager.getSensorType(name);
                }
            });

    static LoadingCache<String, List<SensorType>> cachedSensorTypesByObservation = CacheBuilder.newBuilder()
            .maximumSize(10000)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build(new CacheLoader<String, List<SensorType>>() {
                @Override
                public List<SensorType> load(String name) throws Exception {
                    return dbManager.getSensorTypesByObservation(name);
                }
            });

    static LoadingCache<String, Sensor> cachedSensors = CacheBuilder.newBuilder()
            .maximumSize(1000000)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build(new CacheLoader<String, Sensor>() {
                @Override
                public Sensor load(String name) throws ExecutionException {
                    return dbManager.getSensor(name);
                }
            });

    static LoadingCache<String, Platform> cachedPlatforms = CacheBuilder.newBuilder()
            .maximumSize(1000000)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build(new CacheLoader<String, Platform>() {
                @Override
                public Platform load(String name) throws ExecutionException {
                    return dbManager.getPlatform(name);
                }
            });

    static LoadingCache<String, List<Sensor>> cachedSensorsByType = CacheBuilder.newBuilder()
            .maximumSize(1000000)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build(new CacheLoader<String, List<Sensor>>() {
                @Override
                public List<Sensor> load(String name) throws Exception {
                    return dbManager.getSensorsByType(name);
                }
            });
}
