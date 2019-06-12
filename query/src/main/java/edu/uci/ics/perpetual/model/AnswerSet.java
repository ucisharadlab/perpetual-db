package edu.uci.ics.perpetual.model;

import java.util.List;

public class AnswerSet {
	
	int queryId;
	String queryType; 
	int epochId;
	List<Integer> objectIdList;
	
	
	public AnswerSet() {
		
	}
	
	public AnswerSet(int queryId, int epochId, List<Integer> objectIdList) {
		super();
		this.queryId = queryId;
		this.epochId = epochId;
		this.objectIdList = objectIdList;
	}
	public int getQueryId() {
		return queryId;
	}
	public void setQueryId(int queryId) {
		this.queryId = queryId;
	}
	public String getQueryType() {
		return queryType;
	}
	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}
	public int getEpochId() {
		return epochId;
	}
	public void setEpochId(int epochId) {
		this.epochId = epochId;
	}
	public List<Integer> getObjectIdList() {
		return objectIdList;
	}
	public void setObjectIdList(List<Integer> objectIdList) {
		this.objectIdList = objectIdList;
	}
}
