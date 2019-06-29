package edu.uci.ics.perpetual.planner;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import edu.uci.ics.perpetual.data.DataObject;
import edu.uci.ics.perpetual.executer.QueryExecuter;
import edu.uci.ics.perpetual.model.Predicate;
import edu.uci.ics.perpetual.predicate.ExpressionPredicate;
import edu.uci.ics.perpetual.types.DataObjectType;

public class ObjectRetreival{
	//public StorageManager sm;
	private static ObjectRetreival instance;

	private ObjectRetreival() {
		// TODO Auto-generated constructor stub
		// Retrieve Storage Manager Instance
		
	}
	public static ObjectRetreival getInstance(){
        if (instance == null){
        	instance = new ObjectRetreival();
        }

        return instance;
    }
//	public List<DataObject> getObjects(List<Predicate> predicates, String relationName)
//	{
//		List<DataObject> objectList = new ArrayList<DataObject>();
//		sm.newQuery(predicates, relationName);
//		DataObject tmp = sm.getNextObject();
//		while(tmp != null)
//		{
//			objectList.add(tmp);
//			tmp = sm.getNextObject();
//		}
//		return objectList;
//	}
	public List<DataObject> getObjectsFromFile(List<ExpressionPredicate> predicates) throws IOException {
		// TODO Auto-generated method stub
		List<DataObject> tmpList = new ArrayList<DataObject>();
		String fileName = "dataset.txt";
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String type;
		
		DataObjectType tmpType = new DataObjectType();
		if(br.ready())
		{
			type = br.readLine();
			tmpType.setName(type);
		}
		while(br.ready())
		{
			DataObject tmpObject = new DataObject(br.readLine(), tmpType);
			tmpList.add(tmpObject);
		}
		return tmpList;
	}
}
