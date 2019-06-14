package edu.uci.ics.perpetual.planner;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import edu.uci.ics.perpetual.data.DataObject;
import edu.uci.ics.perpetual.enrichment.EnrichmentFunction;
import edu.uci.ics.perpetual.epochhandler.EpochHandler;
import edu.uci.ics.perpetual.executer.QueryExecuter;
import edu.uci.ics.perpetual.model.EnrichmentFunctionInfo;
import edu.uci.ics.perpetual.model.ObjectState;
import edu.uci.ics.perpetual.model.PlanPath;
import edu.uci.ics.perpetual.predicate.ExpressionPredicate;
import edu.uci.ics.perpetual.state.StateManager;

public class QueryPlanner {
	private StateManager stateManager;
	private PriorityQueue<PlanPath> planQueue;
	private List<ExpressionPredicate> predicateList;
	private List<EnrichmentFunctionInfo> enrichmentFunctionList;
	private static QueryPlanner instance;
	private EpochHandler epochHandler;
	private PlanGeneration plangen;
	private QueryExecuter queryExecuter;
	
	private QueryPlanner()
	{
		//initiliazing lists and queues
		stateManager = StateManager.getInstance();
		planQueue = new PriorityQueue<PlanPath>();
		predicateList = new ArrayList<ExpressionPredicate>();
		enrichmentFunctionList = new ArrayList<EnrichmentFunctionInfo>();
		plangen = new PlanGeneration();
	}
	public static QueryPlanner getInstance(){
        if (instance == null){
        	instance = new QueryPlanner();
        }

        return instance;
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
	// add, get and remove for predicateList
	public void addPredicate(ExpressionPredicate p)
	{
		predicateList.add(p);
	}
	public void addPredicate(ExpressionPredicate p, int index)
	{
		predicateList.add(index, p);
	}
	public ExpressionPredicate getPredicate(int index)
	{
		return predicateList.get(index);
	}
	public ExpressionPredicate removePredicate(int index)
	{
		return predicateList.remove(index);
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
		String functionOne = "Enrichment1.jar";
		String functionTwo = "Enrichment2.jar";
		this.enrichmentFunctionList.add(new EnrichmentFunctionInfo());
		this.enrichmentFunctionList.add(new EnrichmentFunctionInfo());
		this.enrichmentFunctionList.get(0).setFunction(EnrichmentFunction.getEnrichmentFunction(functionOne));
		this.enrichmentFunctionList.get(1).setFunction(EnrichmentFunction.getEnrichmentFunction(functionTwo));
		this.enrichmentFunctionList.get(0).setCost(20);
		this.enrichmentFunctionList.get(1).setCost(80);
		this.enrichmentFunctionList.get(0).setQuality(0.5);
		this.enrichmentFunctionList.get(1).setQuality(1);
		this.enrichmentFunctionList.get(0).setId(1);
		this.enrichmentFunctionList.get(0).setId(2);
	}
	public List<ObjectState> initializeObjectStates(List<DataObject> objectList)
	{
		//retrieve objects from DB then send it to StateManager
		//List<ObjectState> objectStateList = stateManager.initializeObjectStates(objectList);
		return null;
	}
	public List<DataObject> objectRetreival(List<ExpressionPredicate> predicates)
	{
		//retrieve objects with passed predicates and add it to the object state list
		ObjectRetreival  objret = ObjectRetreival.getInstance();
		return objret.getObjectsFromFile(predicates);
	}
	public void initializePlanner(List<ExpressionPredicate> predicates, int epochBudget)
	{
		// initialize the planner and call appropiate methods to retrieve objects and initialize the object state.
		getEnrichmentFunctionsFromMemory();
		pathGenerator(initializeObjectStates(objectRetreival(predicates)));
		epochHandler = EpochHandler.getInstance();
		queryExecuter = QueryExecuter.getInstance();
	}
}
