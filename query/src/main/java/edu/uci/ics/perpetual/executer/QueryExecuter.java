package edu.uci.ics.perpetual.executer;

import java.util.ArrayList;
import java.util.List;

import edu.uci.ics.perpetual.epochhandler.EpochHandler;
import edu.uci.ics.perpetual.model.EnrichmentFunction;
import edu.uci.ics.perpetual.model.ObjectState;

public class QueryExecuter{
	private static QueryExecuter instance;
	private EpochHandler epochHandler;
	
	private List<EnrichmentFunction> enrichmentFunctionList;
	private List<ObjectState> objectStateList;
	
	private QueryExecuter()
	{
		//initialize lists
		enrichmentFunctionList = new ArrayList<EnrichmentFunction>();
		objectStateList = new ArrayList<ObjectState>();
	}
	public static QueryExecuter getInstance(){
        if (instance == null){
        	instance = new QueryExecuter();
        }

        return instance;
    }
	public EpochHandler getEpochHandler() {
		return epochHandler;
	}
	public void setEpochHandler(EpochHandler epochHandler) {
		this.epochHandler = epochHandler;
	}

	// add, get and remove for enrichmentFunctionList
	public void addEnrichmentFunction(EnrichmentFunction f)
	{
		enrichmentFunctionList.add(f);
	}
	public void addEnrichmentFunction(EnrichmentFunction f, int index)
	{
		enrichmentFunctionList.add(index, f);
	}
	public EnrichmentFunction getFunction(int index)
	{
		return enrichmentFunctionList.get(index);
	}
	public EnrichmentFunction removeFunction(int index)
	{
		return enrichmentFunctionList.remove(index);
	}
	
	// add, get and remove for parameters
	public void addObjectState(ObjectState o)
	{
		objectStateList.add(o);
	}
	public void addObjectState(ObjectState o, int index)
	{
		objectStateList.add(index, o);
	}
	public Object getObjectState(int index)
	{
		return objectStateList.get(index);
	}
	public Object removeObjectState(int index)
	{
		return objectStateList.remove(index);
	}
	
	public boolean execute()
	{
		return false;
	}
	
	public boolean updateState()
	{
		return false;
	}
}
