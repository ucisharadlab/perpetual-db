package edu.uci.ics.perpetual.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.uci.ics.perpetual.model.ObjectState;

public class StateManager {

	private List<Integer> queryIdList = new ArrayList<Integer>();
	private HashMap<Integer,ObjectState> stateManagerHashMap = new HashMap<Integer,ObjectState>();
	private int totalCost;
	private int remainingCost;
	
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

	public void updateObjectState(int objectId, int functionId) {
		ObjectState objectState = stateManagerHashMap.get(objectId);
		
		List<Integer> functionBitmap = objectState.getFunctionBitmap();
		if(!functionBitmap.contains(functionId))
			functionBitmap.add(functionId);
		
		objectState.setFunctionBitmap(functionBitmap);
		stateManagerHashMap.put(objectId, objectState);
	}
	public void deleteObjectState(ObjectState objectState) {
		
	}
	public void updateStateManagerHashMap() {
		
	}
	
}
