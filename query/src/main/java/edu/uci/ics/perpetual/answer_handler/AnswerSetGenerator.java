package edu.uci.ics.perpetual.answer_handler;

import edu.uci.ics.perpetual.model.AnswerSet;
import edu.uci.ics.perpetual.model.ObjectState;
import edu.uci.ics.perpetual.state.StateManager;

public class AnswerSetGenerator {
	
	private static StateManager instance;
	AnswerSet answerSet = new AnswerSet();
	
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
		ObjectState objectState = instance.getStateManagerHashMap().get(objectId);
		return objectState;
	}
	
	public void getAnswerSet(int epochId) {
		
	}
	
	public void setAnswerSet(AnswerSet answerSet) {
		
	}
	
	
}
