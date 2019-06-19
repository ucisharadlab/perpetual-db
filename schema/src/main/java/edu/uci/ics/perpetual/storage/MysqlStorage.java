package edu.uci.ics.perpetual.storage;

import com.zaxxer.hikari.HikariDataSource;
import edu.uci.ics.perpetual.Relation;
import edu.uci.ics.perpetual.Schema;
import edu.uci.ics.perpetual.request.LoadRequest;
import edu.uci.ics.perpetual.request.RequestStatus;
import edu.uci.ics.perpetual.request.StorageRequest;
import edu.uci.ics.perpetual.types.*;
import edu.uci.ics.perpetual.util.StringUtils;
import javafx.util.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class MysqlStorage implements Storage {

    private final String resourceName = "database.properties";

    private static MysqlStorage storage;

    private static boolean initialized = false;

    private Connection conn;

    public static MysqlStorage getInstance() {
        if (storage == null) {
            storage = new MysqlStorage();
            storage.init();
        }
        return storage;
    }

    // region load
    @Override
    public void load(LoadRequest request) {
        RequestStatus status = new RequestStatus();
        if (!initialized) {
            status.setErrMsg("Cannot get connection from Mysql");
            request.setStatus(status);
            return;
        }

        try {

            ResultSet rs;
            if (request.getOption() == LoadRequest.LoadOption.SCHEMA) {
                Schema schema = new Schema();

                rs = conn.prepareStatement("SELECT name, attributes FROM MetadataType;").executeQuery();
                while (rs.next()) {
                    MetadataType metadataType = new MetadataType(rs.getString("name"),
                            StringUtils.toMap(rs.getString("attributes")));
                    schema.addMetadataType(metadataType);
                }

                rs = conn.prepareStatement("SELECT name, attributes FROM RawType;").executeQuery();

                while (rs.next()) {
                    RawType rawType = new RawType(rs.getString("name"),
                            StringUtils.toMap(rs.getString("attributes")));
                    schema.addRawType(rawType);
                }

                rs = conn.prepareStatement("SELECT name, paramList, returnType, sourceFunctions FROM DataSourceType;").executeQuery();

                while (rs.next()) {
                    DataSourceType dataSourceType = new DataSourceType(
                            rs.getString("name"),
                            StringUtils.toList(rs.getString("paramList")),
                            schema.getRawType(rs.getString("returnType")),
                            StringUtils.toMap(rs.getString("sourceFunctions")));
                    schema.addDataSourceType(dataSourceType);
                }

                rs = conn.prepareStatement("SELECT name, type, rawType FROM EnrichmentTag;").executeQuery();

                while (rs.next()) {
                    EnrichmentTag tag = new EnrichmentTag(
                            rs.getString("name"), rs.getString("type"),
                            rs.getString("rawType"));
                    schema.addTag(tag);
                }

                rs = conn.prepareStatement("SELECT id, sourceDescription, typeName, functionPath, functionParams FROM DataSource;").executeQuery();

                while (rs.next()) {
                    DataSource dataSource = new DataSource(
                            rs.getInt("id"),
                            rs.getString("sourceDescription"),
                            schema.getDataSourceType(rs.getString("typeName")),
                            rs.getString("functionPath"),
                            StringUtils.toMap(rs.getString("functionParams")));
                    schema.addDataSource(dataSource);
                }

                rs = conn.prepareStatement("SELECT functionName, sourceType, paramList, returnTag, cost, path FROM TaggingFunction;").executeQuery();

                while (rs.next()) {
                    TaggingFunction function = new TaggingFunction(rs.getString("functionName"),
                            rs.getString("sourceType"),
                            StringUtils.toList(rs.getString("paramList")),
                            rs.getString("returnTag"),
                            rs.getInt("cost"));
                    function.setPath(rs.getString("path"));
                    schema.addFunction(function);
                }

                rs.close();
                request.setResult(schema);
                request.setStatus(RequestStatus.success());
            }

            else if (request.getOption() == LoadRequest.LoadOption.RELATION) {
                Relation relation = new Relation();

                rs = conn.prepareStatement("SELECT parent, child FROM Relation;").executeQuery();
                while (rs.next()) {
                    relation.connect(rs.getString("parent"), rs.getString("child"));
                }
                rs.close();
                request.setResult(relation);
                request.setStatus(RequestStatus.success());
            }
        } catch (SQLException e) {
            status.setErrMsg("Unable to retrieve information from Mysql.");
            request.setStatus(status);
        }
    }

    // endregion

    // region save
    @Override
    public void persist(StorageRequest request) {
        RequestStatus status = new RequestStatus();

        if (!initialized) {
            status.setErrMsg("Cannot get connection from Mysql");
            request.setStatus(status);
            return;
        }
        try {
            PreparedStatement ps = null;

            if (request.isType()) {
                Object object = request.getObject();
                if (object instanceof RawType) {
                    RawType type = (RawType) object;
                    ps = conn.prepareStatement("INSERT INTO RawType(name, attributes) VALUE (?, ?);");
                    ps.setString(1, type.getName());
                    ps.setString(2, StringUtils.fromMap(type.getAttributes()));
                } else if (object instanceof MetadataType) {
                    MetadataType type = (MetadataType) object;
                    ps = conn.prepareStatement("INSERT INTO MetadataType(name, attributes) VALUE (?, ?);");
                    ps.setString(1, type.getName());
                    ps.setString(2, StringUtils.fromMap(type.getAttributes()));
                } else if (object instanceof DataSourceType) {
                    DataSourceType type = (DataSourceType) object;
                    ps = conn.prepareStatement("INSERT INTO DataSourceType (name, paramlist, returntype, sourcefunctions) VALUE (?, ?, ?, ?);");
                    ps.setString(1, type.getName());
                    ps.setString(2, StringUtils.fromList(type.getParamList()));
                    ps.setString(3, type.getReturnType().getName());
                    ps.setString(4, StringUtils.fromMap(type.getSourceFunctions()));
                } else if (object instanceof DataSource) {
                    DataSource dataSource = (DataSource) object;
                    ps = conn.prepareStatement("INSERT INTO DataSource (id, sourceDescription, typeName, functionPath, functionParams) VALUE (?, ?, ?, ?, ?);");
                    ps.setInt(1, dataSource.getId());
                    ps.setString(2, dataSource.getSourceDescription());
                    ps.setString(3, dataSource.getSourceType().getName());
                    ps.setString(4, dataSource.getFunctionPath());
                    ps.setString(5, StringUtils.fromMap(dataSource.getFunctionParams()));
                } else if (object instanceof EnrichmentTag) {
                    EnrichmentTag tag = (EnrichmentTag) object;
                    ps = conn.prepareStatement("INSERT INTO EnrichmentTag(name, type, rawType) VALUE (?,?, ?);");
                    ps.setString(1, tag.getName());
                    ps.setString(2, tag.getType());
                    ps.setString(3, tag.getRawType());
                } else if (object instanceof TaggingFunction) {
                    TaggingFunction function = (TaggingFunction) object;
                    ps = conn.prepareStatement("INSERT INTO TaggingFunction(functionName, sourceType, paramList, returnTag, cost, path) VALUE (?, ?, ?, ?, ?, ?);");
                    ps.setString(1, function.getFunctionName());
                    ps.setString(2, function.getSourceType());
                    ps.setString(3, StringUtils.fromList(function.getParamList()));
                    ps.setString(4,function.getReturnTag());
                    ps.setInt(5, function.getCost());
                    ps.setString(6, function.getPath());
                }
            } else {
                Pair<String, String> relation = request.getRelation();
                ps = conn.prepareStatement("INSERT INTO Relation(parent, child) VALUE (?, ?);");
                ps.setString(1, relation.getKey());
                ps.setString(2, relation.getValue());
            }

            if (ps != null) {
                ps.execute();
            }
            request.setStatus(RequestStatus.success());
        } catch (SQLException e) {
//            e.printStackTrace();
            status.setErrMsg("Unable to save information to Mysql.");
            request.setStatus(status);
        }
    }

    @Override
    public void update(StorageRequest request) {
        RequestStatus status = new RequestStatus();

        if (!initialized) {
            status.setErrMsg("Cannot get connection from Mysql");
            request.setStatus(status);
            return;
        }
        try {
            DataSourceType type = (DataSourceType) request.getObject();
            PreparedStatement ps = conn.prepareStatement("UPDATE DataSourceType SET sourceFunctions = ? WHERE name = ?;");
            ps.setString(1, StringUtils.fromMap(type.getSourceFunctions()));
            ps.setString(2, type.getName());

            ps.execute();
            ps.close();
            request.setStatus(RequestStatus.success());
        } catch (SQLException e) {
            status.setErrMsg("Unable to save updated information to Mysql.");
            request.setStatus(status);
        }
    }
    // endregion

    // region inti
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

            initialized = true;
        } catch (SQLException e) {
            throw new UnsupportedOperationException("Unable to connect to mysql database");
        }
    }

    // endregion
}
