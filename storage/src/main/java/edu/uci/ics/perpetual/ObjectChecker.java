package edu.uci.ics.perpetual;

import java.util.List;

import edu.uci.ics.perpetual.data.DataObject;
import edu.uci.ics.perpetual.predicate.Expression;
import edu.uci.ics.perpetual.predicate.ExpressionPredicate;
import edu.uci.ics.perpetual.predicate.LogicalOperator;
import edu.uci.ics.perpetual.predicate.ComparisionOperator;

public class ObjectChecker {
	private static ObjectChecker INSTANCE;
	
	private ObjectChecker(){}
	
	public static ObjectChecker getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ObjectChecker();
        }
        return INSTANCE;
    }
	
	public boolean checkDataObjectID(DataObject dataObject, int id)
	{
		// check if null then false, (throw an exception in the future)
		if(dataObject == null)
			return false;
		String value = dataObject.getObject().get("ID").getAsString();
		if(value.equals(String.valueOf(id)))
			return true;
		return false;
	}
	
	public boolean doesDataObjectSatisfyPredicate(DataObject dataObject, ExpressionPredicate predicate)
	{
		// check if null then false, (throw an exception in the future)
		if(dataObject == null)
			return false;
		if(predicate == null || predicate.getExpressions() == null || predicate.getExpressions().isEmpty())
			return true;
		if(predicate.getLop().equals(LogicalOperator.AND))
			return doesDataObjectSatisfyANDPredicate(dataObject, predicate);
		else if(predicate.getLop().equals(LogicalOperator.OR))
			return doesDataObjectSatisfyORPredicate(dataObject, predicate);
		else if(predicate.getLop().equals(LogicalOperator.XOR))
			return doesDataObjectSatisfyXORPredicate(dataObject, predicate);
		return false;
	}
	
	private boolean doesDataObjectSatisfyXORPredicate(DataObject dataObject, ExpressionPredicate predicate) {
		// TODO 
		return false;
	}

	public boolean doesDataObjectSatisfyANDPredicate(DataObject dataObject, ExpressionPredicate predicate)
	{
		for(Expression exp: predicate.getExpressions())
		{
			if(!checkDataObjectTagSatisfyValue(dataObject, exp))
				return false;
		}
		return true;
	}
	
	public boolean doesDataObjectSatisfyORPredicate(DataObject dataObject, ExpressionPredicate predicate)
	{
		for(Expression exp: predicate.getExpressions())
		{
			if(checkDataObjectTagSatisfyValue(dataObject, exp))
				return true;
		}
		return false;
	}
	
	public boolean checkDataObjectTagSatisfyValue(DataObject dataObject, Expression exp)
	{
		// check of the tag of data object satisfies the expression
		// if the tag is not there return true because it might be imprecise tag
		if(!dataObject.getObject().has(exp.getTag()))
			return true;
		String value = dataObject.getObject().get(exp.getTag()).getAsString();
		switch(exp.getCop())
		{
		case EQ:
			if(value.equals(exp.getValue()))
				return true;
			else
				return false;
		default:
			return false;
		}
		// TODO for the rest of the operators and the rest of primitive data types
	}
}
