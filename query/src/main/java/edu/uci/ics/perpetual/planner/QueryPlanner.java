package edu.uci.ics.perpetual.planner;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import edu.uci.ics.perpetual.model.EnrichmentFunction;
import edu.uci.ics.perpetual.model.PlanPath;
import edu.uci.ics.perpetual.model.Predicate;

public class QueryPlanner {
	private PriorityQueue<PlanPath> planQueue;
	private List<Predicate> predicateList;
	private List<EnrichmentFunction> enrichmentFunctionList;
	
	public QueryPlanner()
	{
		//initiliazing lists and queues
		planQueue = new PriorityQueue<PlanPath>();
		predicateList = new ArrayList<Predicate>();
		enrichmentFunctionList = new ArrayList<EnrichmentFunction>();
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
		//estimate the benifit of the plan
		return 0;
	}
}
