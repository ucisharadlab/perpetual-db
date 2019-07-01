package edu.uci.ics.perpetual;

import edu.uci.ics.perpetual.data.DataObject;
import edu.uci.ics.perpetual.predicate.ExpressionPredicate;
import edu.uci.ics.perpetual.types.DataObjectType;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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

    public DataObject getDataObject(DataObjectType type, int id) {
    	// if type is not found, then return null (for future throw an exception)
    	if(!writers.containsKey(type.getName()))
			return null;
    	DataObject tmpObject;
    	try {
			BufferedReader br = new BufferedReader(
					new FileReader(
			                Paths.get(StorageConfig.STORAGE_DIR, type+StorageConfig.FILE_EX ).toString()));
			
			while(br.ready())
			{
				tmpObject = new DataObject(br.readLine(), type);
				if(ObjectChecker.getInstance().checkDataObjectID(tmpObject, id))
					return tmpObject;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }

    public List<DataObject> getDataObjects(DataObjectType type, ExpressionPredicate predicate) {
		// if type is not found, then return null (for future throw an exception)
    	if(!writers.containsKey(type.getName()))
			return null;
    	
    	//if type is found then navigate through objects and return the object that satisfy the predict
		List<DataObject> dataObjectList = new ArrayList<DataObject>();
		DataObject tmpObject;
		try {
			BufferedReader br = new BufferedReader(
					new FileReader(
			                Paths.get(StorageConfig.STORAGE_DIR, type+StorageConfig.FILE_EX ).toString()));
			
			while(br.ready())
			{
				tmpObject = new DataObject(br.readLine(), type);
				if(ObjectChecker.getInstance().doesDataObjectSatisfyPredicate(tmpObject, predicate))
					dataObjectList.add(tmpObject);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataObjectList;
	}
}
