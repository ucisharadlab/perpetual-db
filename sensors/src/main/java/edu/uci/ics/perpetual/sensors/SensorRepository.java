package edu.uci.ics.perpetual.sensors;

import com.zaxxer.hikari.HikariDataSource;
import edu.uci.ics.perpetual.sensors.model.*;
import edu.uci.ics.perpetual.sensors.predicate.Predicate;
import edu.uci.ics.perpetual.util.Pair;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SensorRepository {

    private final String resourceName = "database.properties";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private static SensorRepository storage;

    private HikariDataSource dataSource;

    public static SensorRepository getInstance() {
        if (storage == null) {
            storage = new SensorRepository();
            storage.init();
        }
        return storage;
    }

    public ObservationType fetchObservationType(String name) {
        List<Pair<String, String>> attributes = fetchEntities(
                String.format("SELECT attribute, valueType FROM ObservationTypes WHERE name = '%s'", name),
                SqlAdapter::convertToAttribute);
        return new ObservationType(name, attributes);
    }

    public List<String> fetchObservationTypeNames(String attribute) {
        return fetchEntities(
            String.format("SELECT name FROM ObservationTypes WHERE attribute = '%s'", attribute),
            (row) -> {
                try {
                    return row.getString("name");
                } catch (SQLException ignored) {
                    return ""; // Default
                }
            }
        );
    }

    public void insertObservationType(ObservationType type) throws Exception {
        String sql = String.format("INSERT INTO ObservationTypes (name, attribute, valueType) " +
                "VALUES %s", StringUtils.repeat("(?, ?, ?),", type.attributes.size()));
        sql = sql.substring(0, sql.length() - 1) + ";";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            int index = 1;
            for (Pair<String, String> attribute: type.attributes){
                statement.setString(index + 0, type.name);
                statement.setString(index + 1, attribute.getKey());
                statement.setString(index + 2, attribute.getValue());
                index += 3;
            }

            if (type.attributes.size() != statement.executeUpdate())
                throw new Exception("Error while adding observation type");
        } catch (SQLException ignored) {
            String message = ignored.getMessage();
        }
    }

    public SensorType getSensorType(String sensorTypeName) {
        return fetchEntities(String.format("SELECT id, name, observationType FROM sensorTypes WHERE name = '%s';", sensorTypeName),
                SqlAdapter::sensorTypeFromRow).get(0);
    }

    public SensorType getSensorTypeFromSensor(int sensorId) {
        return fetchEntities(String.format("SELECT id, name, observationType FROM sensorTypes WHERE id IN " +
                        "(SELECT type FROM sensors WHERE id = %d LIMIT 1);", sensorId),
                SqlAdapter::sensorTypeFromRow).get(0);
    }

    public void insertSensorType(SensorType type) throws Exception {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
             "INSERT INTO SensorTypes (name, observationType) VALUES (?, ?);")) {
            statement.setString(1, type.name);
            statement.setString(2, type.observationType);

            if (1 != statement.executeUpdate())
                throw new Exception("Error while adding sensor type");
        } catch (SQLException ignored) {
            String message = ignored.getMessage();
        }
    }

    public List<SensorType> fetchSensorTypesByObservation(String type) throws Exception {
        return fetchEntities(
                String.format("SELECT * FROM SensorTypes WHERE observationType = '%s'", type),
                SqlAdapter::sensorTypeFromRow
        );
    }

    public List<Sensor> fetchSensorNamesByType(String type) throws Exception {
        return fetchEntities(
                String.format("SELECT * FROM Sensors WHERE type IN (SELECT id FROM SensorTypes WHERE name = '%s')", type),
                SqlAdapter::sensorFromRow
        );
    }

    public Sensor getSensor(String name) {
        return fetchEntities(
                String.format("SELECT S.id, name, S.type, platformId, mobile, location, viewArea, spec," +
                        "CASE WHEN M.locationSource IS NULL THEN '' WHEN M.locationSource IS NOT NULL THEN M.locationSource END locationSource " +
                        "FROM Sensors S LEFT OUTER JOIN MobileObjects M ON S.id = M.id WHERE S.name = '%s' AND (M.type IS NULL OR M.type = '%s')", name, "Sensor"),
                SqlAdapter::sensorFromRow).get(0);
    }

    public Sensor getSensor(int id) {
        return fetchEntities(
                String.format("SELECT S.id, name, S.type, platformId, mobile, location, viewArea, spec," +
                        "CASE WHEN M.locationSource IS NULL THEN '' WHEN M.locationSource IS NOT NULL THEN M.locationSource END locationSource " +
                        "FROM Sensors S LEFT OUTER JOIN MobileObjects M ON S.id = M.id WHERE S.id = %d AND (M.type IS NULL OR M.type = '%s')", id, "Sensor"),
                SqlAdapter::sensorFromRow).get(0);
    }

    public int getNewSensorId(String name) {
        return fetchEntities(String.format("SELECT id FROM Sensors WHERE name = '%s'", name), (row) -> {
            try {
                return row.getInt("id");
            } catch (SQLException ignored) {
                return -1; // Default
            }
        }).get(0);
    }

    public String getLocationSource(int id, String type) {
        return fetchEntities(String.format("SELECT locationSource FROM MobileObjects WHERE id = %d AND type = '%s'", id, type), (row) -> {
            try {
                return row.getString("locationSource");
            } catch (SQLException ignored) {
                return ""; // Default
            }
        }).get(0);
    }

    public void insertSensor(Sensor sensor) throws Exception {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO Sensors (name, type, platformId, mobile, location, viewArea, spec) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?);")) {
            statement.setString(1, sensor.name);
            statement.setInt(2, sensor.typeId);
            statement.setInt(3, sensor.platformId);
            statement.setBoolean(4, sensor.mobile);
            statement.setString(5, sensor.location.coordinates);
            statement.setString(6, sensor.viewArea.coordinates);
            statement.setString(7, sensor.spec);

            if (1 != statement.executeUpdate())
                throw new Exception("Error while adding sensor");
        } catch (SQLException ignored) {
            String message = ignored.getMessage();
        }
    }

    public Platform getPlatform(String platformName) {
        Platform platform = fetchEntities(String.format("SELECT P.id, name, mobile," +
                        "CASE WHEN M.locationSource IS NULL THEN '' WHEN M.locationSource IS NOT NULL THEN M.locationSource END locationSource " +
                        " FROM Platforms P LEFT OUTER JOIN MobileObjects M ON P.id = M.id WHERE name = '%s' AND (M.type IS NULL OR M.type = '%s')", platformName, "Platform"),
                SqlAdapter::platformFromRow).get(0);
        platform.components = getPlatformComponents(platform.id);
        return platform;
    }

    public int getNewPlatformId(String name) {
        return fetchEntities(String.format("SELECT id FROM Platforms WHERE name = '%s'", name), (row) -> {
            try {
                return row.getInt("id");
            } catch (SQLException ignored) {
                return -1; // Default
            }
        }).get(0);
    }

    public List<Sensor> getPlatformComponents(int platformId) {
        return fetchEntities(
                String.format("SELECT S.id, name, S.type, platformId, mobile, location, viewArea, spec, " +
                        "M.locationSource " +
                        "FROM Sensors S LEFT OUTER JOIN MobileObjects M ON S.platformId = M.id WHERE platformId = %d AND (M.type IS NULL OR M.type = '%s')", platformId, "Platform"),
                SqlAdapter::sensorFromRow);
    }

    public void insertPlatform(Platform platform) throws Exception {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO Platforms (name, mobile) VALUES (?, ?);")) {
            statement.setString(1, platform.name);
            statement.setBoolean(2, platform.mobile);

            if (1 != statement.executeUpdate())
                throw new Exception("Error while adding platform");
        } catch (SQLException ignored) {
            String message = ignored.getMessage();
        }
    }

    public void createObservationsTable(String tableName, ObservationType type) {
        String sql = String.format(
                "CREATE TABLE IF NOT EXISTS %s (" + // Todo: Remove "IF NOT EXISTS". It is only added to make testing easy
                "sensor int NOT NULL, time timestamp, %s);",
                tableName,
                type.attributes.stream().map(a -> a.getKey() + " " + a.getValue())
                    .collect(Collectors.joining(", "))
                );
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.execute();
        } catch (SQLException ignored) {
            String message = ignored.getMessage();
        }
    }

    public List<Observation> getObservations(String dataTableName, List<String> predicates, ObservationType observationType) {
        return getObservations(dataTableName, String.join(" AND ", predicates), observationType);
    }

    public List<Observation> getObservations(String dataTableName, Predicate predicate, ObservationType observationType) {
        return getObservations(dataTableName, predicate.toSql(), observationType);
    }

    public List<Observation> getObservations(String dataTableName, String whereClause, ObservationType observationType) {

        List<Observation> observations = new LinkedList<>();

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(String.format("SELECT * FROM %s WHERE %s ORDER BY time, sensor;", dataTableName, whereClause));
            while (resultSet.next()) {
                Observation observation = SqlAdapter.convertToObservation(resultSet, observationType);
                if (null != observation)
                    observations.add(observation);
            }
        } catch (SQLException ignored) {
            String message = ignored.getMessage();
        }

        return observations;
    }

    public void insertObservation(String table, int sensorId, LocalDateTime time, List<ObservedAttribute> values) throws Exception {
        String timeString = time.format(formatter);
        List<String> columns = new LinkedList<>();
        List<String> insertionValues = new LinkedList<>();

        for (ObservedAttribute value : values) {
            columns.add(value.name);
            insertionValues.add(String.format("CAST('%s' AS %s)", value.value, value.valueType));
        }

        String sql = String.format("INSERT INTO %s (sensor, time, %s) " +
                "VALUES (%d, '%s', %s);", table, String.join(", ", columns),
                sensorId, timeString, String.join(", ", insertionValues));

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            if (1 != statement.executeUpdate())
                throw new Exception("Error while adding sensor type");
        } catch (SQLException ignored) {
            String message = ignored.getMessage();
        }
    }

    public void insertMobileObject(int id, String type, String locationSource) throws Exception {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO MobileObjects (id, type, locationSource) VALUES (?, ?, ?);")) {
            statement.setInt(1, id);
            statement.setString(2, type);
            statement.setString(3, locationSource);

            if (1 != statement.executeUpdate())
                throw new Exception("Error while adding mobile object");
        } catch (SQLException ignored) {
            String message = ignored.getMessage();
        }
    }

    public <T> List<T> fetchEntities(String sql, Function<ResultSet, T> convertFromRow) {
        List<T> entities = new LinkedList<T>();

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                T entity = convertFromRow.apply(resultSet);
                if (null != entity)
                    entities.add(entity);
            }
        } catch (SQLException ignored) {
            String message = ignored.getMessage();
        }

        return entities;
    }

    private void init() {
        Properties prop = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceName);

        try {
            if (inputStream != null) {
                prop.load(inputStream);
            }
        } catch (IOException e) {
            throw new UnsupportedOperationException("property file '" + resourceName + "' not found in the classpath");
        }

        this.dataSource = new HikariDataSource();
//        dataSource.setDriverClassName("org.postgresql.Driver");
        this.dataSource.setJdbcUrl(prop.getProperty("url"));
        this.dataSource.setUsername(prop.getProperty("user"));
        this.dataSource.setPassword(prop.getProperty("password"));

        // also create the schema tables if not present yet
    }
}
