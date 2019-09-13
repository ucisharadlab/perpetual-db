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
import org.apache.log4j.Logger;

import edu.uci.ics.perpetual.model.Predicate;
import edu.uci.ics.perpetual.model.Query;
import edu.uci.ics.perpetual.predicate.ComparisionOperator;
import edu.uci.ics.perpetual.predicate.Expression;
import edu.uci.ics.perpetual.predicate.ExpressionPredicate;
import edu.uci.ics.perpetual.types.DataObjectType;

public class TestQuery{
	//public StorageManager sm;
	
	
	
	public static void main(String[] args){	
		//Query(int queryID, DataObjectType type, List<String> projections, ExpressionPredicate pPredicate,Expression iPredicate)
		System.out.println("In test query");
		DataObjectType type = new DataObjectType();
		type.setName("TWEETS");
		
		Expression<String> iPredicate = new Expression<String>("sentiment",ComparisionOperator.EQ, "Positive");
		//public Expression(String tag, ComparisionOperator cop, T value)
		Query query = new Query(1, type, null, null, iPredicate);
		
		//public void initializePlanner(Query query, int epochBudget)
		QueryPlanner.getInstance().initializePlanner(query,100000);
		
		
	}
}
