package edu.uci.ics.perpetual.sensors;

import com.zaxxer.hikari.HikariDataSource;
import edu.uci.ics.perpetual.util.Pair;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;

public class SensorRepository {

    private final String resourceName = "database.properties";

    private static SensorRepository storage;

    private HikariDataSource dataSource;

    public static SensorRepository getInstance() {
        if (storage == null) {
            storage = new SensorRepository();
            storage.init();
        }
        return storage;
    }

    public void insertObservationType(ObservationType type) throws Exception {
        String sql = String.format("INSERT INTO ObservationType (name, attribute, valueType) " +
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


    public void insertSensorType(SensorType type) throws Exception {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
             "INSERT INTO SensorType (name, observationType) VALUES (?, ?);")) {
            statement.setString(1, type.name);
            statement.setString(1, type.observationType);

            if (1 != statement.executeUpdate())
                throw new Exception("Error while adding sensor type");
        } catch (SQLException ignored) {
            String message = ignored.getMessage();
        }
    }

    public void createTable(String tableName) {
        String sql = String.format("CREATE TABLE %s (" +
                "sensorName VARCHAR(50) NOT NULL, " +
                "attribute VARCHAR(50) NOT NULL, " +
                "value float NOT NULL);", tableName);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.execute();
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

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(prop.getProperty("url"));
        dataSource.setUsername(prop.getProperty("user"));
        dataSource.setPassword(prop.getProperty("password"));
    }
}
