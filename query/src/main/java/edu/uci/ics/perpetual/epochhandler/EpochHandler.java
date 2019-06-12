package edu.uci.ics.perpetual.epochhandler;

import edu.uci.ics.perpetual.planner.ObjectRetreival;

public class EpochHandler{
	private static EpochHandler instance;
	private double budget;
	private double remainingTime;
	private int epochNumber;
	private EpochHandler()
	{
		
	}
	public static EpochHandler getInstance(){
        if (instance == null){
        	instance = new EpochHandler();
        }

        return instance;
    }
	public double getBudget() {
		return budget;
	}
	public void setBudget(double budget) {
		this.budget = budget;
	}
	public double getRemainingTime() {
		return remainingTime;
	}
	public void setRemainingTime(double remainingTime) {
		this.remainingTime = remainingTime;
	}
	public int getEpochNumber() {
		return epochNumber;
	}
	public void setEpochNumber(int epochNumber) {
		this.epochNumber = epochNumber;
	}
}
