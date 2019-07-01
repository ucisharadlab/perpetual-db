package edu.uci.ics.perpetual.planner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import edu.uci.ics.perpetual.enrichment.EnrichmentFunction;
import edu.uci.ics.perpetual.FileStorage;
import edu.uci.ics.perpetual.SchemaManager;
import edu.uci.ics.perpetual.data.DataObject;
import edu.uci.ics.perpetual.enrichment.EnrichmentFunction;
import edu.uci.ics.perpetual.epochhandler.EpochHandler;
import edu.uci.ics.perpetual.model.EnrichmentFunctionInfo;
import edu.uci.ics.perpetual.model.ObjectState;
import edu.uci.ics.perpetual.model.PlanPath;
import edu.uci.ics.perpetual.model.Query;
import edu.uci.ics.perpetual.predicate.ExpressionPredicate;
import edu.uci.ics.perpetual.state.StateManager;
import edu.uci.ics.perpetual.types.DataObjectType;
import edu.uci.ics.perpetual.types.TaggingFunction;

public class QueryPlanner {
	private PriorityQueue<PlanPath> planQueue;
	private List<EnrichmentFunctionInfo> enrichmentFunctionList;
	private static QueryPlanner instance;
	private PlanGeneration plangen;
	private Query query;
	
	private QueryPlanner()
	{
		StateManager.getInstance();
		planQueue = new PriorityQueue<PlanPath>();
		enrichmentFunctionList = new ArrayList<EnrichmentFunctionInfo>();
		plangen = new PlanGeneration();
	}
	public static QueryPlanner getInstance(){
        if (instance == null){
        	instance = new QueryPlanner();
        }

        return instance;
    }
	
	
	public Query getQuery() {
		return query;
	}
	public void setQuery(Query query) {
		this.query = query;
	}
	//add, seek and poll for planQueue
	public void addPlanPath(PlanPath pp)
	{
		planQueue.add(pp);
	}
	public PlanPath peekPlanPath()
	{
		return planQueue.peek();
	}
	public PlanPath pollPlanPath()
	{
		return planQueue.poll();
	}
	
	public PriorityQueue<PlanPath> getPlanQueue() {
		return planQueue;
	}
	public void setPlanQueue(PriorityQueue<PlanPath> planQueue) {
		this.planQueue = planQueue;
	}
	
	// add, get and remove for enrichmentFunctionList
	public void addEnrichmentFunction(EnrichmentFunctionInfo f)
	{
		enrichmentFunctionList.add(f);
	}
	public void addEnrichmentFunction(EnrichmentFunctionInfo f, int index)
	{
		enrichmentFunctionList.add(index, f);
	}
	public EnrichmentFunctionInfo getFunction(int index)
	{
		return enrichmentFunctionList.get(index);
	}
	public EnrichmentFunctionInfo removeFunction(int index)
	{
		return enrichmentFunctionList.remove(index);
	}
	
	public void pathGenerator(List<ObjectState> objectStateList)
	{
		//generation of paths
		plangen.getInitialPlanPath(enrichmentFunctionList, objectStateList);
	}
	
	public double costEstimator()
	{
		//estimate the cost of the paths
		return 0;
		
	}
	public double benifitEstimator()
	{
		//estimate the benefit of the pair of object function
		return 0;
	}
	public void getEnrichmentFunctionsFromMemory()
	{
		//query the in-memory db to get all enrichment functions and add them to the list
		int count = 0;

		for (Map.Entry<String, TaggingFunction> entry : SchemaManager.getInstance().getSchema().getEnrichmentFunctions().entrySet()) {
            if(entry.getValue().getReturnTag().equalsIgnoreCase(query.getiPredicate().getTag()) &&
                    entry.getValue().getSourceType().equalsIgnoreCase(query.getType().getName()))
            {
            	enrichmentFunctionList.add(new EnrichmentFunctionInfo(count,
            			EnrichmentFunction.getEnrichmentFunction(entry.getValue().getPath(), query.getiPredicate().getTag()),
            			entry.getValue().getCost(),
            			0));
            }
        }
	}
	public List<ObjectState> initializeObjectStates(List<DataObject> objectList)
	{
		//retrieve objects from DB then send it to StateManager
		//List<ObjectState> objectStateList = stateManager.initializeObjectStates(objectList);
		
		
		return null;
	}
	public List<DataObject> objectRetreival(DataObjectType type, ExpressionPredicate predicate)
	{
		//retrieve objects with passed predicates and add it to the object state list
		
		return FileStorage.getInstance(SchemaManager.getInstance()).getDataObjects(type, predicate);
		
		
		
//		ObjectRetreival  objret = ObjectRetreival.getInstance();
//		try {
//			return objret.getObjectsFromFile(predicates);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
	}
	public void initializePlanner(Query query, int epochBudget)
	{
		// initialize the planner and call appropiate methods to retrieve objects and initialize the object state.
		
		getEnrichmentFunctionsFromMemory();
		pathGenerator(initializeObjectStates(objectRetreival(query.getType(), query.getpPredicate())));
		
		// set the epochBudget in EpochHandler
		EpochHandler.getInstance().setBudget(epochBudget);
	}
	public EnrichmentFunctionInfo getEnrichmentFunctionInfoByID(int id)
	{
		for(EnrichmentFunctionInfo tmp: enrichmentFunctionList)
			if(tmp.getId() == id)
				return tmp;
		return null;
	}
}
