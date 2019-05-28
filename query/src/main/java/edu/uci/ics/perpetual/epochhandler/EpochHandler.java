package edu.uci.ics.perpetual.epochhandler;

public class EpochHandler {
	private double passedTime;
	private double remainingTime;
	private int epochNumber;
	public double getPassedTime() {
		return passedTime;
	}
	public void setPassedTime(double passedTime) {
		this.passedTime = passedTime;
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
