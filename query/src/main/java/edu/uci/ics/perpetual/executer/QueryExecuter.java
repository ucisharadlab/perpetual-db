package edu.uci.ics.perpetual.executer;

import java.util.ArrayList;
import java.util.List;

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

	// add, get and remove for enrichmentFunctionList
	
	public void execute()
	{
		while(!queryPlanner.getPlanQueue().isEmpty())
		{
			executeOneEpoch();
		}
	}
	private void executeOneEpoch() {
		PlanPath pp;
		while(epochHandler.availableBudgetToRunFunction(queryPlanner.peekPlanPath().getCost()))
		{
			pp = queryPlanner.pollPlanPath();
			executeOneObjectFunctionPair(pp);
			pp.calculateCost();
			if(!pp.getObject().isResolved())
			{
				queryPlanner.getPlanQueue().add(pp);
			}
		}
	}
	public void executeOneObjectFunctionPair(PlanPath pp)
	{
		EnrichmentFunctionInfo tmpFunc = pp.removeFunction(0);
		String result = tmpFunc.getFunction().executeAndReturnResult(pp.getObject().getObject());
		stateManager.updateObjectState(pp.getObject());
	}
}
