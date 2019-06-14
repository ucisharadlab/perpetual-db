package edu.uci.ics.perpetual.planner;

import java.util.List;

import edu.uci.ics.perpetual.data.DataObject;
import edu.uci.ics.perpetual.executer.QueryExecuter;
import edu.uci.ics.perpetual.model.Predicate;
import edu.uci.ics.perpetual.predicate.ExpressionPredicate;

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
	public List<DataObject> getObjectsFromFile(List<ExpressionPredicate> predicates) {
		// TODO Auto-generated method stub
		return null;
	}
}
