package edu.uci.ics.perpetual.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.uci.ics.perpetual.answer_handler.AnswerSetGenerator;
import edu.uci.ics.perpetual.data.DataObject;
import edu.uci.ics.perpetual.model.AnswerSet;
import edu.uci.ics.perpetual.model.EnrichmentFunction;
import edu.uci.ics.perpetual.model.ObjectState;
import java.lang.Object.*;
//import edu.uci.ics.perpetualdb.common.DataObject;


public class StateManager {

	private static StateManager instance;
	private List<Integer> queryIdList = new ArrayList<Integer>();
	private List<DataObject> dataObjectList = new ArrayList<DataObject>();
	private HashMap<Integer,ObjectState> stateManagerHashMap = new HashMap<Integer,ObjectState>();
	private int totalCost;
	private int remainingCost;
	
	private StateManager() {
		// TODO Auto-generated constructor stub
		// Retrieve State Manager Instance
		
	}
	public static StateManager getInstance(){
        if (instance == null){
        		instance = new StateManager();
        }

        return instance;
    }
	
	
	
	public void initializeObjectState(List<DataObject> objectList, List<EnrichmentFunction> enrichmentFunctionList) {
		
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

	public void updateObjectState(DataObject dataObject, int functionId) {
		try {
			int dataObjectIndex = dataObjectList.indexOf(dataObject);
			ObjectState objectState = stateManagerHashMap.get(dataObjectIndex);			
			List<Integer> functionBitmap = objectState.getFunctionBitmap();
			functionBitmap.set(functionId, 1);	
			objectState.setFunctionBitmap(functionBitmap);
			stateManagerHashMap.put(dataObjectIndex, objectState);			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void deleteObjectState(ObjectState objectState) {
		
	}
	public void updateStateManagerHashMap() {
		
	}
	
}
