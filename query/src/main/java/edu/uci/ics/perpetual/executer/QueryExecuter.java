package edu.uci.ics.perpetual.executer;

import java.util.ArrayList;
import java.util.List;

import edu.uci.ics.perpetual.ObjectChecker;
import edu.uci.ics.perpetual.answer_handler.AnswerSetGenerator;
import edu.uci.ics.perpetual.data.DataObject;
import edu.uci.ics.perpetual.epochhandler.EpochHandler;
import edu.uci.ics.perpetual.model.AnswerSet;
import edu.uci.ics.perpetual.model.BlockPath;
import edu.uci.ics.perpetual.model.BlockState;
import edu.uci.ics.perpetual.model.EnrichmentFunctionInfo;
import edu.uci.ics.perpetual.model.ObjectState;
import edu.uci.ics.perpetual.model.PlanPath;
import edu.uci.ics.perpetual.planner.QueryPlanner;
import edu.uci.ics.perpetual.state.StateManager;
import edu.uci.ics.perpetual.planner.PlanGeneration;

public class QueryExecuter{
	private static QueryExecuter instance;
	private EpochHandler epochHandler;
	private QueryPlanner queryPlanner;
	private StateManager stateManager;
	private AnswerSetGenerator answerSetGenerator;
	
	private int cumulativeLenAnswer;
	private ArrayList<Integer> cumulativeAnswerList;
	
	private QueryExecuter()
	{
		//initialize lists
		epochHandler = EpochHandler.getInstance();
		queryPlanner = QueryPlanner.getInstance();
		stateManager = StateManager.getInstance();
		answerSetGenerator = AnswerSetGenerator.getInstance();
		cumulativeAnswerList = new ArrayList<Integer>();
		cumulativeLenAnswer = 0;
	}
	public static QueryExecuter getInstance(){
        if (instance == null){
        	instance = new QueryExecuter();
        }

        return instance;
    }
	
	public void execute()
	{
		while(!queryPlanner.getBlockPlanQueue().isEmpty())
		{
			//executeOneEpochProgressive();
			executeOneEpochProgressiveBlock();
			// create answer set to feed it the objects
			System.out.println("Epoch Number: "+epochHandler.getEpochNumber());
			System.out.println("Results:");
			int answerLen =  epochHandler.getAnswerObjectStatesList().size();
			//for(int i=0;i< answerLen ;i++)
				//printObjectToSTDout(epochHandler.getAnswerObjectStatesList().get(i));
			
			cumulativeLenAnswer+= answerLen;
			cumulativeAnswerList.add(cumulativeLenAnswer);
			System.out.println("cumulative answer length = "+ cumulativeLenAnswer);
			System.out.println("cumulative answer list = "+ cumulativeAnswerList.toString());
			
			/* call answer set genrator and take the answerObjectStatesList
			 *  and deal with the answer set generator
			 */
			epochHandler.AddNewEpoch();
		}
	}
	private void printObjectToSTDout(ObjectState objectState) {
		// TODO Auto-generated method stub
		System.out.println(objectState.getObject().getObject().toString());
	}
	private void executeOneEpoch() {
		PlanPath pp;
		while(!queryPlanner.getPlanQueue().isEmpty() && epochHandler.availableBudgetToRunFunction(queryPlanner.peekPlanPath().getCost()))
		{
			pp = queryPlanner.pollPlanPath();
			executeOneObjectFunctionPair(pp);
			if(!pp.getObject().isResolved())
			{
				pp.calculateCost();
				queryPlanner.getPlanQueue().add(pp);
			}
			else
			{
				if(ObjectChecker.getInstance().checkDataObjectTagSatisfyValue(pp.getObject().getObject(), QueryPlanner.getInstance().getQuery().getiPredicate()))
					epochHandler.getAnswerObjectStatesList().add(pp.getObject());
			}
		}
	}
	
	private void executeOneEpochProgressive() {
		PlanPath pp;
		while(!queryPlanner.getPlanQueue().isEmpty() && epochHandler.availableBudgetToRunFunction(queryPlanner.peekPlanPath().getCost()))
		{
			pp = queryPlanner.pollPlanPath();
			executeOneObjectFunctionPair(pp);
			if(!pp.getObject().isResolved())
			{
				//pp.calculateCost();
				pp.calculateBenefit();
				queryPlanner.getPlanQueue().add(pp);
			}
			else
			{
				if(ObjectChecker.getInstance().checkDataObjectTagSatisfyValue(pp.getObject().getObject(), QueryPlanner.getInstance().getQuery().getiPredicate()))
					epochHandler.getAnswerObjectStatesList().add(pp.getObject());
			}
		}
	}
	
	
	private void executeOneEpochProgressiveBlock() {
		BlockPath blockPath;
		BlockState blockState;
		DataObject object;
		
		//System.out.println("BlockPlanQueue size = "+ queryPlanner.getBlockPlanQueue().size());
		
		while(!queryPlanner.getBlockPlanQueue().isEmpty() && epochHandler.availableBudgetToRunFunction(queryPlanner.peekBlockPlanPath().getCost()))
		{
			blockPath = queryPlanner.pollBlockPlanPath();
			executeOneBlockFunctionPair(blockPath);
			blockState = blockPath.getBlockState();
						
			if(blockPath.getEnrichmentFunctionInfoSize()>0)
			{
				//pp.calculateCost();
				blockPath.calculateCost();
				queryPlanner.getBlockPlanQueue().add(queryPlanner.getPlangen().planOneBlock(blockPath.getEnrichmentFunctionList(), blockState));
				//queryPlanner.getBlockPlanQueue().add(blockPath);
			}
			
			
			for(ObjectState objState: blockState.getObjectStateList()) {
				if(ObjectChecker.getInstance().checkDataObjectTagSatisfyValue(objState.getObject(), QueryPlanner.getInstance().getQuery().getiPredicate())) {
					ObjectState objectState = new ObjectState();
					objectState.setObject(objState.getObject());
					objectState.setFunctionBitmap(blockState.getFunctionBitmap());
					objectState.setFunctionResultList(blockState.getFunctionResultList());
					epochHandler.getAnswerObjectStatesList().add(objectState);
				}
				
				//epochHandler.getAnswerObjectStatesList().add(objectState.get);
			}					
				
			 
					
		}
	}
	
	
	
	
	
	private void executeOneBlockFunctionPair(BlockPath blockPath) {
		// TODO Auto-generated method stub
		try {
			System.out.println("Befor execution, number of objects in the block path: "+blockPath.getBlockState().getObjectStateList().size());	
			if(blockPath.getEnrichmentFunctionInfoSize() > 0) {
				EnrichmentFunctionInfo tmpFunc = blockPath.removeFunction(0);
				BlockState blockState = blockPath.getBlockState();
				List<ObjectState> objectStateList = blockState.getObjectStateList();
				List<ObjectState> newObjectList = new ArrayList<ObjectState>();
				String result = null;
				
				for(ObjectState objectState:objectStateList) {
					//System.out.println("Executing enrichment Functions: "+tmpFunc.getId() +" on object id:" +object);
					//if(!objectState.isResolved())
					result = tmpFunc.getFunction().executeAndReturnResult(objectState.getObject());
					//System.out.println("result = "+result);  // result = Positive, null, Negative	
					if(result!= null && result.equalsIgnoreCase("Positive")) {
						int numPositive = blockState.getNumPositive();
						blockState.setNumPositive(numPositive++);
					}else if(result!= null && result.equalsIgnoreCase("Negative")) {
						int numNegative = blockState.getNumNegative();
						blockState.setNumPositive(numNegative++);
					}else {
						int numMaybe = blockState.getNumMayBe();
						blockState.setNumPositive(numMaybe++);
						newObjectList.add(objectState);
					}
					
					epochHandler.deductFromRemainingBudget(tmpFunc.getCost());
					stateManager.updateObjectState(objectState.getObject(), tmpFunc.getId(), result);
				}	
				//blockState.setObjectList(newObjectList);
				//blockState.setBlocksize(newObjectList.size());
				
			}
			//System.out.println("After Execution, number of objects in the block path: "+blockPath.getBlockState().getObjectStateList().size());	
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
	public void executeOneObjectFunctionPair(PlanPath pp)
	{
		EnrichmentFunctionInfo tmpFunc = pp.removeFunction(0);
		//System.out.println("Executing enrichment Functions: "+tmpFunc.getId() +" on object id:" +pp.getObject().getObject().getObject().get("id").getAsString());
		String result = tmpFunc.getFunction().executeAndReturnResult(pp.getObject().getObject());
		epochHandler.deductFromRemainingBudget(tmpFunc.getCost());
		stateManager.updateObjectState(pp.getObject().getObject(), tmpFunc.getId(), result);
	}
}
