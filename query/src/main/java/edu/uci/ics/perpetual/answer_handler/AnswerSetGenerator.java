package edu.uci.ics.perpetual.answer_handler;

import edu.uci.ics.perpetual.model.AnswerSet;
import edu.uci.ics.perpetual.model.ObjectState;
import edu.uci.ics.perpetual.planner.ObjectRetreival;
import edu.uci.ics.perpetual.state.StateManager;

public class AnswerSetGenerator {
	
	
	private static AnswerSetGenerator instance;
	private AnswerSet answerSet ;
	

	private AnswerSetGenerator() {
		// TODO Auto-generated constructor stub
		// Retrieve Storage Manager Instance
		answerSet = new AnswerSet();
		
	}
	public static AnswerSetGenerator getInstance(){
        if (instance == null){
        		instance = new AnswerSetGenerator();
        }

        return instance;
    }
	
	
	public void generateAnswer() {
		AnswerSet answer = new AnswerSet();
		/*
		 * Logic of generating the answer set will be provided here.
		 */
		this.answerSet = answer;
	}
	
	public void addObject(int objectId, AnswerSet ans) {
		
	}
	
	public ObjectState getState(int objectId) {
		/*
		 * Given an object id, it will retrieve the state of the object.
		 */
		
		return null;
	}
	
	public void getAnswerSet(int epochId) {
		
	}
	
	public void setAnswerSet(AnswerSet answerSet) {
		
	}
	
	
}
