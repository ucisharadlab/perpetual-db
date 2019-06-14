package edu.uci.ics.perpetual.planner;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import edu.uci.ics.perpetual.epochhandler.EpochHandler;
import edu.uci.ics.perpetual.model.EnrichmentFunction;
import edu.uci.ics.perpetual.model.PlanPath;
import edu.uci.ics.perpetual.model.Predicate;

public class QueryPlanner {
	private PriorityQueue<PlanPath> planQueue;
	private List<Predicate> predicateList;
	private List<EnrichmentFunction> enrichmentFunctionList;
	private static QueryPlanner instance;
	private EpochHandler epochHandler;
	
	private QueryPlanner()
	{
		//initiliazing lists and queues
		planQueue = new PriorityQueue<PlanPath>();
		predicateList = new ArrayList<Predicate>();
		enrichmentFunctionList = new ArrayList<EnrichmentFunction>();
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
	
	// add, get and remove for predicateList
	public void addPredicate(Predicate p)
	{
		predicateList.add(p);
	}
	public void addPredicate(Predicate p, int index)
	{
		predicateList.add(index, p);
	}
	public Predicate getPredicate(int index)
	{
		return predicateList.get(index);
	}
	public Predicate removePredicate(int index)
	{
		return predicateList.remove(index);
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
	
	public void pathGenerator()
	{
		//generation of paths
		
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
		
	}
	public void initializeObjectStates()
	{
		//retrieve objects from DB then send it to StateManager
		
	}
	public void objectRetreival(List<Predicate> predicates)
	{
		//retrieve objects with passed predicates and add it to the object state list
		
	}
	public void initializePlanner(List<Predicate> predicates, int epochBudget)
	{
		// initialize the planner and call appropriate methods to retrieve objects and initialize the object state.
		
	}
}
