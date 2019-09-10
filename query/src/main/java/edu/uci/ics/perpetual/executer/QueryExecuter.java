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
			for(int i=0;i< answerLen ;i++)
				printObjectToSTDout(epochHandler.getAnswerObjectStatesList().get(i));
			
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
		System.out.println("In executeOneEpochProgressiveBlock method BlockPlanQueue = " + queryPlanner.getBlockPlanQueue());
		System.out.println("In executeOneEpochProgressiveBlock method Top Plan path = " + queryPlanner.peekBlockPlanPath().toString());
		System.out.println("In executeOneEpochProgressiveBlock method Top Plan Cost = " + queryPlanner.peekBlockPlanPath().getCost());
		
		System.out.println("BlockPlanQueue size = "+ queryPlanner.getBlockPlanQueue().size());
		
		while(!queryPlanner.getBlockPlanQueue().isEmpty() && epochHandler.availableBudgetToRunFunction(queryPlanner.peekBlockPlanPath().getCost()))
		{
			blockPath = queryPlanner.pollBlockPlanPath();
			executeOneBlockFunctionPair(blockPath);
			//System.out.println("BlockPlanQueue size updated = "+ queryPlanner.getBlockPlanQueue().size());
			
			
			if(!blockPath.getBlockState().isResolved() && blockPath.getEnrichmentFunctionInfoSize()>0)
			{
				//pp.calculateCost();
				blockPath.calculateCost();
				queryPlanner.getBlockPlanQueue().add(blockPath);
			}
			else
			{
				blockState = blockPath.getBlockState();
				for(DataObject obj: blockState.getObjectList()) {
					if(ObjectChecker.getInstance().checkDataObjectTagSatisfyValue(obj, QueryPlanner.getInstance().getQuery().getiPredicate())) {
						ObjectState objectState = new ObjectState();
						objectState.setObject(obj);
						objectState.setFunctionBitmap(blockState.getFunctionBitmap());
						objectState.setFunctionResultList(blockState.getFunctionResultList());
						epochHandler.getAnswerObjectStatesList().add(objectState);
					}
					
					//epochHandler.getAnswerObjectStatesList().add(objectState.get);
				}					
					
			 }
					
			}
	}
	
	
	
	
	
	private void executeOneBlockFunctionPair(BlockPath blockPath) {
		// TODO Auto-generated method stub
		try {
			System.out.println("Number of objects in the block path: "+blockPath.getBlockState().getObjectList().size());	
			if(blockPath.getEnrichmentFunctionInfoSize() > 0) {
				EnrichmentFunctionInfo tmpFunc = blockPath.removeFunction(0);
				List<DataObject> objectList = blockPath.getBlockState().getObjectList();
				for(int i = 0;i<objectList.size();i++) {
					//System.out.println("Executing enrichment Functions: "+tmpFunc.getId() +" on object id:" +objectList.get(i));					
					String result = tmpFunc.getFunction().executeAndReturnResult(objectList.get(i));
					epochHandler.deductFromRemainingBudget(tmpFunc.getCost());
					stateManager.updateObjectState(objectList.get(i), tmpFunc.getId(), result);
				}	
			}
			
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
