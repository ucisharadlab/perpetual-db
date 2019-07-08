package edu.uci.ics.perpetual.executer;

import java.util.ArrayList;
import java.util.List;

import edu.uci.ics.perpetual.ObjectChecker;
import edu.uci.ics.perpetual.answer_handler.AnswerSetGenerator;
import edu.uci.ics.perpetual.epochhandler.EpochHandler;
import edu.uci.ics.perpetual.model.AnswerSet;
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
	
	private QueryExecuter()
	{
		//initialize lists
		epochHandler = EpochHandler.getInstance();
		queryPlanner = QueryPlanner.getInstance();
		stateManager = StateManager.getInstance();
		answerSetGenerator = AnswerSetGenerator.getInstance();
	}
	public static QueryExecuter getInstance(){
        if (instance == null){
        	instance = new QueryExecuter();
        }

        return instance;
    }
	
	public void execute()
	{
		while(!queryPlanner.getPlanQueue().isEmpty())
		{
			executeOneEpoch();
			// create answer set to feed it the objects
			System.out.println("Epoch Number: "+epochHandler.getEpochNumber());
			System.out.println("Results:");
			for(int i=0;i<epochHandler.getAnswerObjectStatesList().size();i++)
				printObjectToSTDout(epochHandler.getAnswerObjectStatesList().get(i));
			
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
	public void executeOneObjectFunctionPair(PlanPath pp)
	{
		EnrichmentFunctionInfo tmpFunc = pp.removeFunction(0);
		//System.out.println("Executing enrichment Functions: "+tmpFunc.getId() +" on object id:" +pp.getObject().getObject().getObject().get("id").getAsString());
		String result = tmpFunc.getFunction().executeAndReturnResult(pp.getObject().getObject());
		epochHandler.deductFromRemainingBudget(tmpFunc.getCost());
		stateManager.updateObjectState(pp.getObject().getObject(), tmpFunc.getId(), result);
	}
}
