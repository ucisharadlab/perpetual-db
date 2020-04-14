package edu.uci.ics.perpetual.asterixdb.mysql;

import com.zaxxer.hikari.HikariDataSource;
import edu.uci.ics.perpetual.Relation;
import edu.uci.ics.perpetual.Schema;
import edu.uci.ics.perpetual.SchemaManager;
import edu.uci.ics.perpetual.StorageManager;
import edu.uci.ics.perpetual.data.DataObject;
import edu.uci.ics.perpetual.predicate.ExpressionPredicate;
import edu.uci.ics.perpetual.request.LoadRequest;
import edu.uci.ics.perpetual.request.RequestStatus;
import edu.uci.ics.perpetual.request.StorageRequest;
import edu.uci.ics.perpetual.storage.MysqlStorage;
import edu.uci.ics.perpetual.storage.Storage;
import edu.uci.ics.perpetual.types.*;
import edu.uci.ics.perpetual.util.StringUtils;
import javafx.util.Pair;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Properties;

public class MySQLStorage implements StorageManager {

    private final String resourceName = "database.properties";

    private static final String CREATE_FORMAT = "CREATE DATASET %s";
    private static final String INSERT_FORMAT = "INSERT INTO %s VALUES(?)";
    private static final String ID_SELECT_FORMAT = "SELECT * FROM %s where id=%s";
    private static final String RANGE_SELECT_FORMAT = "SELECT * FROM %s where %s";
    private static final String RESULTS_KEY = "results";

    private static MySQLStorage storage;

    private static boolean initialized = false;

    private Connection conn;
    private PreparedStatement ps = null;

    private int count = 0;
    private Instant start;

    public static MySQLStorage getInstance(SchemaManager schemaManager) {
        if (storage == null) {
            storage = new MySQLStorage();
            storage.init();
        }
        return storage;
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

        try {
            HikariDataSource dataSource = new HikariDataSource();
            dataSource.setJdbcUrl(prop.getProperty("url"));
            dataSource.setUsername(prop.getProperty("user"));
            dataSource.setPassword(prop.getProperty("password"));

            conn = dataSource.getConnection();
            conn.setAutoCommit(true);
            initialized = true;
        } catch (SQLException e) {
            throw new UnsupportedOperationException("Unable to connect to mysql database");
        }
    }

    public Connection getConn() {
        return conn;
    }

    @Override
    public void addRawObject(DataObject object) {
        String insert = String.format(
                INSERT_FORMAT,
                object.getType().getName());

        if (count == 0) start = Instant.now();
        try {
            ps = conn.prepareStatement(insert);
            ps.setString(1, object.getObject().toString());
            ps.execute();

//            if (count % 1000 ==0) { ps.executeBatch(); conn.commit();  ps = conn.prepareStatement(insert);
//            }

            count += 1;
            if (count%1000 ==0 ) System.out.println(count + " IN " + Duration.between(start, Instant.now()));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public DataObject getDataObject(DataObjectType type, int id) {
        return null;
    }

    @Override
    public List<DataObject> getDataObjects(DataObjectType type, ExpressionPredicate predicate) {
        return null;
    }
}

