package edu.uci.ics.perpetual.state;

import java.util.list;

public final class ObjectState{
	private int objectID;
	private List<Integer> functionBitmap;
	private List<String> functionResultList;  // For deterministic functions
	private List<float> functionProbResultList; // For probabilistic functions
	
}