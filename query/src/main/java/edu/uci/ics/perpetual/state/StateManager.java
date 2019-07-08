package edu.uci.ics.perpetual.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import edu.uci.ics.perpetual.answer_handler.AnswerSetGenerator;
import edu.uci.ics.perpetual.data.DataObject;
import edu.uci.ics.perpetual.model.AnswerSet;
import edu.uci.ics.perpetual.model.EnrichmentFunctionInfo;
import edu.uci.ics.perpetual.model.ObjectState;
//import edu.uci.ics.perpetualdb.common.DataObject;
import edu.uci.ics.perpetual.planner.QueryPlanner;


public class StateManager {

	private static StateManager instance;
	private QueryPlanner queryPlanner;
	private List<Integer> queryIdList = new ArrayList<Integer>();
	private List<DataObject> dataObjectList = new ArrayList<DataObject>();
	private HashMap<Integer,ObjectState> stateManagerHashMap = new HashMap<Integer,ObjectState>();
	private int totalCost;
	private int remainingCost;
	
	private StateManager() {
		// TODO Auto-generated constructor stub
		// Retrieve State Manager Instance
		queryPlanner = QueryPlanner.getInstance();
		
	}
	public static StateManager getInstance(){
        if (instance == null){
        		instance = new StateManager();
        }

        return instance;
    }
	
	
	
	public void initializeObjectState(List<DataObject> objectList, List<EnrichmentFunctionInfo> enrichmentFunctionList) {
		
		//Initialize hashmap with empty bitmap values.
		int len = enrichmentFunctionList.size();
		ArrayList<Integer> functionBitmap = new ArrayList<Integer>();
		for(int i = 0 ; i < len ; i++) {
			functionBitmap.add(i,0);
		}
		this.dataObjectList = objectList;
		int objectListSize = objectList.size();
		for(int i = 0; i< objectListSize; i++) {
			ObjectState objState = new ObjectState();
			objState.setObject(objectList.get(i));
			objState.setFunctionBitmap(functionBitmap);			
			stateManagerHashMap.put(i, objState);
		}
		
	}
	
	public int getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(int totalCost) {
		this.totalCost = totalCost;
	}

	public int getRemainingCost() {
		return remainingCost;
	}

	public void setRemainingCost(int remainingCost) {
		this.remainingCost = remainingCost;
	}
	
	


	public HashMap<Integer, ObjectState> getStateManagerHashMap() {
		return stateManagerHashMap;
	}

	public void setStateManagerHashMap(HashMap<Integer, ObjectState> stateManagerHashMap) {
		this.stateManagerHashMap = stateManagerHashMap;
	}

	public void createObjectState(ObjectState objectState) {
			
	}

	public void updateObjectState(DataObject dataObject, int functionId, String result) {
		try {
			int dataObjectIndex = dataObjectList.indexOf(dataObject);
			ObjectState objectState = stateManagerHashMap.get(dataObjectIndex);			
			List<Integer> functionBitmap = objectState.getFunctionBitmap();
			functionBitmap.set(functionId, 1);	
			objectState.setFunctionBitmap(functionBitmap);
			stateManagerHashMap.put(dataObjectIndex, objectState);
			EnrichmentFunctionInfo tmpFunction = QueryPlanner.getInstance().getFunction(functionId);
			
			if(result != null) {
				/*
				 * Update the tag.
				 */
				String tag = queryPlanner.getQuery().getiPredicate().getTag();
				dataObject.getObject().addProperty(tag, result);
				objectState.setResolved(true);;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void deleteObjectState(ObjectState objectState) {
		
	}
	public void updateStateManagerHashMap() {
		
	}
	
}
