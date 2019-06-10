package edu.uci.ics.perpetual.ingestion;

import com.google.gson.JsonElement;
import edu.uci.ics.perpetual.acquisition.AcquisitionManager;
import edu.uci.ics.perpetual.data.DataObject;

import java.util.ArrayList;
import java.util.HashSet;

public class IngestionThread implements Runnable {
    private final int requestId;
    private long checkTimeInterval;
    private AcquisitionManager aquisitionMgr = AcquisitionManager.getInstance();
    private HashSet<JsonElement> seenTimeStamps = new HashSet<>();

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
                Thread.sleep(checkTimeInterval - (endTime - startTime));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void processObjects(ArrayList<DataObject> objects) {
        for (DataObject object : objects) {
            if (!seenTimeStamps.contains(object.getTimeStamp())) {
                tagObject(object);
            }
        }
    }

    private void tagObject(DataObject object) {

    }

    private void clean() {
        seenTimeStamps.clear();
    }

}
