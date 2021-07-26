package edu.uci.ics.perpetual.geo;

import com.zaxxer.hikari.HikariDataSource;
import edu.uci.ics.perpetual.geo.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;

public class SpaceRepository {
    private final String resourceName;

    private HikariDataSource dataSource;

    public SpaceRepository() {
        resourceName = "database.properties";
        this.init();
    }

    public Space fetchSpace(String spaceName) {
        Space space = fetchEntities(String.format("SELECT space_id AS sid, parent_space_id AS psid, coordinate_system_name AS csn, space_shape AS shape from entity WHERE space_name='%s';", spaceName), SqlAdapter::spaceFromRow).get(0);
        space.vertices = getSpaceCoordinates(spaceName);
        return space;
    }

    public void insertSpace(Space space) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO entity (space_name, parent_space_id, coordinate_system_name, space_shape, vertices)" +
                     "VALUES ( ?, ?, ?, ?, ?::coordinate[]);")) {
            statement.setString(1, space.space_name);
            statement.setString(2, space.parent_space_id);
            statement.setString(3, space.coordinate_system_name);
            statement.setString(4, space.space_shape);
            statement.setArray(5, connection.createArrayOf("coordinate", space.vertices.toArray()));

            if (1 != statement.executeUpdate())
                System.out.println("Error while adding sensor");
        } catch (SQLException ignored) {
            String message = ignored.getMessage();
        }
    }

    public void deleteSpace(String spaceName) throws Exception {
        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM entity WHERE space_name = '%s'")) {
            statement.setString(1,spaceName);

            if (1 != statement.executeUpdate())
                throw new Exception("Error deleting space");
        }
        catch (SQLException e) {
            System.out.print(e.getMessage());
        }
    }

    public List<Coordinate> getSpaceCoordinates(String spaceName) { //need to refine the query to check for null vertices column.
        return fetchEntities(String.format("SELECT csn, (r).* FROM (SELECT coordinate_system_name AS csn, unnest(vertices) AS r from entity WHERE space_name='%s') AS vs", spaceName), SqlAdapter::coordinatesFromRow);
    }

    public String getCoordSys(String spaceName) {
        return fetchEntities(String.format("SELECT coordinate_system_name FROM entity WHERE space_name='%s'", spaceName), (row) -> {
            try {
                return row.getString("coordinate_system_name");
            } catch (SQLException e) {
                return null;
            }
        }).get(0);
    }

    public String getParentSpace(String spaceName) {

        return fetchEntities(String.format("SELECT space_name FROM entity WHERE space_id" +
                "= (SELECT parent_space_id FROM entity WHERE space_name = '%s')", spaceName), (row) -> {
            try {
                return row.getString("space_name");
            } catch (SQLException e) {
                return null;
            }
        }).get(0);

    }
    public String getShape(String spaceName) {
        return fetchEntities(String.format("SELECT space_shape FROM entity WHERE space_name='%s'", spaceName), (row) -> {
            try {
                return row.getString("space_shape");
            } catch (SQLException e) {
                return null;
            }
        }).get(0);
    }
    public double getDistance(String spaceName1, String spaceName2) {
        return fetchEntities(String.format("SELECT ST_Distance((SELECT geog FROM geo WHERE space_name = '%'), (SELECT geog FROM geo WHERE space_name = '%'))", spaceName1, spaceName2), (row) -> {
            try {
                return row.getDouble(1);
            } catch (SQLException e) {
                return null;
            }
        }).get(0);
    }
    public Boolean intersect(String spaceName1, String spaceName2) {
        return fetchEntities(String.format("SELECT ST_Intersects((SELECT geog FROM geo WHERE space_name = '%'), (SELECT geog FROM geo WHERE space_name = '%'))", spaceName1, spaceName2), (row) -> {
            try {
                return row.getBoolean(1);
            } catch (SQLException e) {
                return null;
            }
        }).get(0);

    }

    public double getArea(String spaceName) {
        return fetchEntities(String.format("SELECT ST_Area((SELECT geog FROM geo WHERE space_name = '%'))", spaceName), (row) -> {
            try {
                return row.getDouble(1);
            } catch (SQLException e) {
                return null;
            }
        }).get(0);
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