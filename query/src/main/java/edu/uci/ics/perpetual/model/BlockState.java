package edu.uci.ics.perpetual.model;

import java.util.ArrayList;
import java.util.List;

import edu.uci.ics.perpetual.data.*;

public final class BlockState{
	private List<ObjectState> objectStateList;
	private int blockSize;
	private List<Integer> functionBitmap;
	private List<String> functionResultList;  // For deterministic functions
	private List<Double> functionProbResultList; // For probabilistic functions
	private boolean isResolved;
	private int numPositive;
	private int numNegative;
	private int numMayBe;
	
	
	
	public int getBlockSize() {
		return blockSize;
	}

	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}

	public int getNumPositive() {
		return numPositive;
	}

	public void setNumPositive(int numPositive) {
		this.numPositive = numPositive;
	}

	public int getNumMayBe() {
		return numMayBe;
	}

	public void setNumMayBe(int numMayBe) {
		this.numMayBe = numMayBe;
	}

	public int getNumNegative() {
		return numNegative;
	}

	public void setNumNegative(int numNegative) {
		this.numNegative = numNegative;
	}
	
	public BlockState(){
		//initializing lists
		functionBitmap = new ArrayList<Integer>();
		functionResultList = new ArrayList<String>();
		functionProbResultList = new ArrayList<Double>();
		numPositive = 0;
		numMayBe = 0;
		numNegative = 0;
	}
	
	public boolean isResolved() {
		return isResolved;
	}

	public void setResolved(boolean isResolved) {
		this.isResolved = isResolved;
	}

	public List<Integer> getFunctionBitmap() {
		return functionBitmap;
	}

	public void setFunctionBitmap(List<Integer> functionBitmap) {
		this.functionBitmap = functionBitmap;
	}
	
	public int getBlocksize() {
		return blockSize;
	}

	public void setBlocksize(int blockSize) {
		this.blockSize = blockSize;
	}
	

	public List<String> getFunctionResultList() {
		return functionResultList;
	}

	public void setFunctionResultList(List<String> functionResultList) {
		this.functionResultList = functionResultList;
	}

	public List<Double> getFunctionProbResultList() {
		return functionProbResultList;
	}

	public void setFunctionProbResultList(List<Double> functionProbResultList) {
		this.functionProbResultList = functionProbResultList;
	}

	
	
	public List<ObjectState> getObjectStateList() {
		return objectStateList;
	}
	public void setObjectStateList(List<ObjectState> objectStateList) {
		this.objectStateList = objectStateList;
	}
	
	// add, get and remove for functionBitmap
	public void addFunctionBit(int n)
	{
		functionBitmap.add(n);
	}
	public void addFunctionBit(int n, int index)
	{
		functionBitmap.add(index, n);
	}
	public int getFunctionBit(int index)
	{
		return functionBitmap.get(index);
	}
	public int removeFunctionBit(int index)
	{
		return functionBitmap.remove(index);
	}
	
	// add, get and remove for functionResultList
	public void addFunctionResult(String s)
	{
		functionResultList.add(s);
	}
	public void addFunctionResult(String s, int index)
	{
		functionResultList.add(index, s);
	}
	public String getFunctionResult(int index)
	{
		return functionResultList.get(index);
	}
	public String removeFunctionResult(int index)
	{
		return functionResultList.remove(index);
	}
	
	// add, get and remove for functionProbResultList
	public void addFunctionProbResult(double d)
	{
		functionProbResultList.add(d);
	}
	public void addFunctionProbResult(double d, int index)
	{
		functionProbResultList.add(index, d);
	}
	public double getFunctionProbResult(int index)
	{
		return functionProbResultList.get(index);
	}
	public double removeFunctionProbResult(int index)
	{
		return functionProbResultList.remove(index);
	}

}