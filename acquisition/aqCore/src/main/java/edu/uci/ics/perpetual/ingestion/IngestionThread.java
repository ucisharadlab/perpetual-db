package edu.uci.ics.perpetual.ingestion;

import com.google.gson.JsonElement;
import edu.uci.ics.perpetual.*;
import edu.uci.ics.perpetual.acquisition.AcquisitionManager;
import edu.uci.ics.perpetual.asterixdb.AsterixDBStorage;
import edu.uci.ics.perpetual.asterixdb.mysql.MySQLStorage;
import edu.uci.ics.perpetual.data.DataObject;
import edu.uci.ics.perpetual.enrichment.EnrichmentFunction;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class IngestionThread implements Runnable {
    private final int requestId;
    private long checkTimeInterval;
    private AcquisitionManager aquisitionMgr = AcquisitionManager.getInstance();
    private HashSet<JsonElement> seenTimeStamps = new HashSet<>();
    private CachingManager cachingManager = CachingManagerFactory.getCachingManager();
    private SchemaManager schemaManager = SchemaManager.getInstance();
//    private StorageManager storageManager = FileStorage.getInstance(schemaManager);
    private StorageManager storageManager = MySQLStorage.getInstance(schemaManager);

    Logger LOGGER = Logger.getLogger(IngestionThread.class);

    public IngestionThread(int requestId, long checkTimeInterval) {
        this.requestId = requestId;
        this.checkTimeInterval = checkTimeInterval;
    }

    public void run() {
        try {
            while (true) {
                long startTime = System.currentTimeMillis();
                ArrayList<DataObject> objects = aquisitionMgr.getData(this.requestId);
                if (objects == null) {
                    break;
                }
                processObjects(objects);
                clean();
                long endTime = System.currentTimeMillis();
                Thread.sleep(Math.max(0, checkTimeInterval - (endTime - startTime)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void processObjects(ArrayList<DataObject> objects) {
        for (DataObject object : objects) {
            if (!seenTimeStamps.contains(object.getTimeStamp())) {
                tagObject(object);
                LOGGER.info(object.getObject().toString());
                storageManager.addRawObject(object);
            }
        }
    }

    private void tagObject(DataObject object) {
        Iterator<EnrichmentFunction> functions = cachingManager.match(object).iterator();
        while (functions.hasNext()) {
            functions.next().execute(object);
        }
    }

    private void clean() {
        seenTimeStamps.clear();
    }

}
