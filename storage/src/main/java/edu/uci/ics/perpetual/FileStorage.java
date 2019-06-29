package edu.uci.ics.perpetual;

import edu.uci.ics.perpetual.data.DataObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileStorage implements StorageManager{

    private static FileStorage INSTANCE;
    private SchemaManager schemaManager;
    private Map<String, Writer> writers;

    private FileStorage(SchemaManager schemaManager){

        writers = new HashMap<String, Writer>();
        for (String rawType : schemaManager.getSchema().getRawMap().keySet()) {
            try {
                writers.put(rawType, new BufferedWriter(
                        new FileWriter(
                                Paths.get(StorageConfig.STORAGE_DIR, rawType+StorageConfig.FILE_EX ).toString()))
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static FileStorage getInstance(SchemaManager schemaManager) {

        if (INSTANCE == null) {
            INSTANCE = new FileStorage(schemaManager);
        }

        return INSTANCE;

    }

    public void addRawObject(DataObject object) {

        String rawType = object.getType().getName();

        try {
            if (!writers.containsKey(rawType)) {
                writers.put(rawType, new BufferedWriter(
                        new FileWriter(
                                Paths.get(StorageConfig.STORAGE_DIR, rawType+StorageConfig.FILE_EX ).toString()))
                );
            }
            writers.get(rawType).write(object.toString()+"\n");
            writers.get(rawType).flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public DataObject getDataObject(int id, String type) {
        return null;
    }
}
