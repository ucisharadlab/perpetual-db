package edu.uci.ics.perpetual.asterixdb;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.uci.ics.perpetual.SchemaManager;
import edu.uci.ics.perpetual.StorageConfig;
import edu.uci.ics.perpetual.StorageManager;
import edu.uci.ics.perpetual.data.DataObject;
import edu.uci.ics.perpetual.predicate.ExpressionPredicate;
import edu.uci.ics.perpetual.types.DataObjectType;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import javax.swing.text.html.parser.Entity;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AsterixDBStorage implements StorageManager {

    private AsterixDBConnectionManager connectionManager;
    private static AsterixDBStorage INSTANCE;
    private static JsonParser parser = new JsonParser();
    private AsterixDataFeed dataFeed;

    private static final String CREATE_FORMAT = "CREATE DATASET %s";
    private static final String INSERT_FORMAT = "INSERT INTO %s(%s)";
    private static final String ID_SELECT_FORMAT = "SELECT * FROM %s where id=%s";
    private static final String RANGE_SELECT_FORMAT = "SELECT * FROM %s where %s";
    private static final String RESULTS_KEY = "results";



    private AsterixDBStorage(SchemaManager schemaManager) {
        connectionManager = AsterixDBConnectionManager.getInstance();

        for (String rawType : schemaManager.getSchema().getRawMap().keySet()) {
            try {
                String create = String.format(
                        CREATE_FORMAT,
                        rawType);
                HttpResponse response = connectionManager.sendQuery(create);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        dataFeed = new AsterixDataFeed("Tweets", connectionManager);

    }

    public static AsterixDBStorage getInstance(SchemaManager schemaManager) {

        if (INSTANCE == null) {
            INSTANCE = new AsterixDBStorage(schemaManager);
        }

        return INSTANCE;

    }

    @Override
    public void addRawObject(DataObject object) {

//        String insert = String.format(
//                INSERT_FORMAT,
//                object.getType().getName(),
//                object.getObject().toString());
//        HttpResponse response = connectionManager.sendQuery(insert);
        dataFeed.sendDataToFeed(object.getObject().toString());

    }


    public void addRawObjectSingle(DataObject object) {

        String insert = String.format(
                INSERT_FORMAT,
                object.getType().getName(),
                object.getObject().toString());
        HttpResponse response = connectionManager.sendQuery(insert);

    }

    @Override
    public DataObject getDataObject(DataObjectType type, int id) {
        String select = String.format(
                ID_SELECT_FORMAT,
                type.getName(),
                id);
        HttpResponse response = connectionManager.sendQuery(select);
        try {
            JsonArray results = parser.parse(
                    EntityUtils.toString(response.getEntity())).getAsJsonObject().getAsJsonArray(RESULTS_KEY);
            if (results.size() != 0) {
                return new DataObject(results.get(0).getAsJsonObject(), type);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<DataObject> getDataObjects(DataObjectType type, ExpressionPredicate predicate) {
        String select = String.format(
                RANGE_SELECT_FORMAT,
                type.getName(),
                predicate.toString());
        HttpResponse response = connectionManager.sendQuery(select);
        try {
            JsonArray results = parser.parse(
                    EntityUtils.toString(response.getEntity())).getAsJsonObject().getAsJsonArray(RESULTS_KEY);
            List<DataObject> objects = new ArrayList<>();
            for( int i=0; i<results.size(); i++) {
                objects.add(new DataObject(results.get(0).getAsJsonObject(), type));
            }
            return objects;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
