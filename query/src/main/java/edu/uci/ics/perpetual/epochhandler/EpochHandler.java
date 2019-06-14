package edu.uci.ics.perpetual.epochhandler;

public class EpochHandler{
	private static EpochHandler instance;
	private double budget;
	private double remainingBudget;
	private int epochNumber;
	private EpochHandler()
	{
		epochNumber = 0;
		budget = 200;
		remainingBudget = budget;
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
		return remainingBudget;
	}
	public void setRemainingTime(double remainingTime) {
		this.remainingBudget = remainingTime;
	}
	public int getEpochNumber() {
		return epochNumber;
	}
	public void setEpochNumber(int epochNumber) {
		this.epochNumber = epochNumber;
	}
	public void AddNewEpoch()
	{
		incrementEpochNumber();
		resetRemainingBudget();
		
	}
	public void resetRemainingBudget() {
		remainingBudget = budget;
	}
	public void incrementEpochNumber() {
		epochNumber++;
	}
	public void deductFromRemainingBudget(double deducted)
	{
		remainingBudget = remainingBudget - deducted;
	}
	public boolean availableBudgetToRunFunction(double cost)
	{
		if(remainingBudget - cost >= 0)
			return true;
		return false;
	}
}
