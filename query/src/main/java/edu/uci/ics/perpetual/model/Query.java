package edu.uci.ics.perpetual.model;

import java.util.List;

import edu.uci.ics.perpetual.predicate.Expression;
import edu.uci.ics.perpetual.predicate.ExpressionPredicate;
import edu.uci.ics.perpetual.types.DataObjectType;

public class Query {
	private int queryID;
	private DataObjectType type;
	private List<String> projections;
	private ExpressionPredicate pPredicate; // this is for precise predicate
	private Expression iPredicate; // this is for imprecise predicate
	
	public Query(int queryID, DataObjectType type, List<String> projections, ExpressionPredicate pPredicate,
			Expression iPredicate) {
		super();
		this.queryID = queryID;
		this.type = type;
		this.projections = projections;
		this.pPredicate = pPredicate;
		this.iPredicate = iPredicate;
	}
	public DataObjectType getType() {
		return type;
	}
	public void setType(DataObjectType type) {
		this.type = type;
	}
	public List<String> getProjections() {
		return projections;
	}
	public void setProjections(List<String> projections) {
		this.projections = projections;
	}
	public int getQueryID() {
		return queryID;
	}
	public void setQueryID(int queryID) {
		this.queryID = queryID;
	}
	public ExpressionPredicate getpPredicate() {
		return pPredicate;
	}
	public void setpPredicate(ExpressionPredicate pPredicate) {
		this.pPredicate = pPredicate;
	}
	public Expression getiPredicate() {
		return iPredicate;
	}
	public void setiPredicate(Expression iPredicate) {
		this.iPredicate = iPredicate;
	}
}
