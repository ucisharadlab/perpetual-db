package edu.uci.ics.perpetual.planner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import com.google.common.util.concurrent.Service.State;

import edu.uci.ics.perpetual.enrichment.EnrichmentFunction;
import edu.uci.ics.perpetual.FileStorage;
import edu.uci.ics.perpetual.ObjectChecker;
import edu.uci.ics.perpetual.SchemaManager;
import edu.uci.ics.perpetual.data.DataObject;
import edu.uci.ics.perpetual.enrichment.EnrichmentFunction;
import edu.uci.ics.perpetual.epochhandler.EpochHandler;
import edu.uci.ics.perpetual.executer.QueryExecuter;
import edu.uci.ics.perpetual.model.EnrichmentFunctionInfo;
import edu.uci.ics.perpetual.model.ObjectState;
import edu.uci.ics.perpetual.model.PlanPath;
import edu.uci.ics.perpetual.model.Query;
import edu.uci.ics.perpetual.predicate.ExpressionPredicate;
import edu.uci.ics.perpetual.state.StateManager;
import edu.uci.ics.perpetual.types.DataObjectType;
import edu.uci.ics.perpetual.types.TaggingFunction;
import edu.uci.ics.perpetual.model.BlockPath;

public class QueryPlanner {
	private PriorityQueue<PlanPath> planQueue;
	private PriorityQueue<BlockPath> blockPlanQueue;
	private List<EnrichmentFunctionInfo> enrichmentFunctionList;
	private static QueryPlanner instance;
	private PlanGeneration plangen;
	private Query query;
	
	private QueryPlanner()
	{
		planQueue = new PriorityQueue<PlanPath>();
		blockPlanQueue = new PriorityQueue<BlockPath>();
		enrichmentFunctionList = new ArrayList<EnrichmentFunctionInfo>();
		plangen = new PlanGeneration();
	}
	public PlanGeneration getPlangen() {
		return plangen;
	}
	public void setPlangen(PlanGeneration plangen) {
		this.plangen = plangen;
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
	
	// For block plan queue operations
	
	public PriorityQueue<BlockPath> getBlockPlanQueue() {
		return blockPlanQueue;
	}
	public void setBlockPlanQueue(PriorityQueue<BlockPath> blockPlanQueue) {
		this.blockPlanQueue = blockPlanQueue;
	}
	public void addBlockPlanPath(BlockPath bp)
	{
		blockPlanQueue.add(bp);
	}
	public BlockPath peekBlockPlanPath()
	{
		return blockPlanQueue.peek();
	}
	public BlockPath pollBlockPlanPath()
	{
		return blockPlanQueue.poll();
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
		//planQueue = plangen.getInitialPlanPath(enrichmentFunctionList, objectStateList);
		blockPlanQueue = plangen.getInitialPlanPathBlockBased(enrichmentFunctionList, objectStateList);
		System.out.println("blockPlanQueue="+blockPlanQueue);
		System.out.println("size of blockPlanQueue="+blockPlanQueue.size());
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
		int count = 1;

		for (Map.Entry<String, TaggingFunction> entry : SchemaManager.getInstance().getSchema().getEnrichmentFunctions().entrySet()) {
            if(entry.getValue().getReturnTag().equalsIgnoreCase(query.getiPredicate().getTag()) &&
                    entry.getValue().getSourceType().equalsIgnoreCase(query.getType().getName()))
            {
            	enrichmentFunctionList.add(new EnrichmentFunctionInfo(count,
            			EnrichmentFunction.getEnrichmentFunction(entry.getValue().getPath(), query.getiPredicate().getTag()),
            			entry.getValue().getCost(),
            			entry.getValue().getQuality()));
            	
            	count++;
            }
        }
		sortEnrichmentFunctionsBasedOnCostAndSetID();
	}
	private void sortEnrichmentFunctionsBasedOnCostAndSetID() {
		Collections.sort(enrichmentFunctionList);
		for(int i=0;i<enrichmentFunctionList.size();i++)
			enrichmentFunctionList.get(i).setId(i);
	}
	public List<ObjectState> initializeObjectStates(List<DataObject> objectList)
	{
		//retrieve objects from DB then send it to StateManager
		//List<ObjectState> objectStateList = stateManager.initializeObjectStates(objectList);
		StateManager.getInstance().initializeObjectState(objectList, enrichmentFunctionList);
		return new ArrayList<ObjectState>(StateManager.getInstance().getStateManagerHashMap().values());
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
		this.query = query;
		getEnrichmentFunctionsFromMemory();
		List<DataObject> dataObjectsList = objectRetreival(query.getType(), query.getpPredicate());
		
		CheckResolvedDataObjectsAndAddToResult(dataObjectsList);
		pathGenerator(initializeObjectStates(dataObjectsList));
		
		// set the epochBudget in EpochHandler
		try {
			EpochHandler.getInstance().setBudget(epochBudget);
			QueryExecuter.getInstance().execute();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	private void CheckResolvedDataObjectsAndAddToResult(List<DataObject> dataObjectsList) {
		String tag = query.getiPredicate().getTag();
		for(int i=0;i<dataObjectsList.size();i++)
		{
			if(dataObjectsList.get(i).getObject().has(tag))
			{
				if(ObjectChecker.getInstance().checkDataObjectTagSatisfyValue(dataObjectsList.get(i), query.getiPredicate()))
					{
						ObjectState tmpOS = new ObjectState();
						tmpOS.setObject(dataObjectsList.get(i));
						tmpOS.setResolved(true);
						EpochHandler.getInstance().getAnswerObjectStatesList().add(tmpOS);
					}
				dataObjectsList.remove(i--);
			}
		}
	}
	public EnrichmentFunctionInfo getEnrichmentFunctionInfoByID(int id)
	{
		for(EnrichmentFunctionInfo tmp: enrichmentFunctionList)
			if(tmp.getId() == id)
				return tmp;
		return null;
	}
}
