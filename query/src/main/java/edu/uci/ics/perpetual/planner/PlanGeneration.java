package edu.uci.ics.perpetual.planner;

import java.util.List;
import java.util.PriorityQueue;

import edu.uci.ics.perpetual.model.EnrichmentFunctionInfo;
import edu.uci.ics.perpetual.model.ObjectState;
import edu.uci.ics.perpetual.model.PlanPath;

public class PlanGeneration {

	public PlanGeneration() {
		// TODO Auto-generated constructor stub
	}
	public PriorityQueue<PlanPath> getInitialPlanPath(List<EnrichmentFunctionInfo> enrichmentFunctionList, List<ObjectState> objectStateList)
	{
		PriorityQueue<PlanPath> newPQ = new PriorityQueue<>();
		for(int i=0;i<objectStateList.size();i++)
		{
			newPQ.add(planOneObject(enrichmentFunctionList, objectStateList.get(i)));
		}
		return newPQ;
	}
	public PlanPath planOneObject(List<EnrichmentFunctionInfo> enrichmentFunctionList, ObjectState objectState)
	{
		PlanPath pp = new PlanPath();
		pp.setObject(objectState);
		int index = getLatestEvaluatedFunction(objectState);
		if(index+1 == enrichmentFunctionList.size())
			return pp;
		OGP(index+1, enrichmentFunctionList, pp);
		pp.calculateCost();
		return pp;
	}
	private int getLatestEvaluatedFunction(ObjectState objectState) {
		int index = -1;
		for(int i=0;i<objectState.getFunctionBitmap().size();i++)
			if(objectState.getFunctionBitmap().get(i) == 1)
				index = i;
		return index;			
	}
	public void OGP(int startIndex, List<EnrichmentFunctionInfo> enrichmentFunctionList, PlanPath pp)
	{
		if((enrichmentFunctionList.size() - startIndex) == 1)
		{
			pp.addEnrichmentFunction(enrichmentFunctionList.get(enrichmentFunctionList.size()-1));
		}
		else
		{
			int n = (enrichmentFunctionList.size() - startIndex);
			double [][] C = new double [n][n+1];
			boolean [][] skip = new boolean [n][n+1];
			double m [] = new double[n+1];
			m[0] = 1;
			double c [] = new double[n+1];
			for(int j=1, i=0;i<n;i++,j++)
				m[j] = 1-enrichmentFunctionList.get(i).getQuality();
			for(int j=1, i=0;i<n;i++,j++)
				c[j] = enrichmentFunctionList.get(i).getCost();
			for(int i=0;i<n;i++)
			{
				C[i][n] = m[i] * c[n];
				skip[i][n] = false;
			}
			for(int i=n-1;i>=0;i--)
			{
				for(int j=0;j<i;j++)
				{
					double notSkipCost = m[j]*c[i]+C[i][i+1];
					double skipCost = C[j][i+1];
					C[j][i] = Math.min(notSkipCost, skipCost);
					skip[j][i] = notSkipCost>skipCost;
				}
			}
			int j = 0;
			for(int i=1;i<n;i++)
			{
				if(!skip[j][i])
				{
					pp.addEnrichmentFunction(enrichmentFunctionList.get(i-1));
					j = i;
				}
			}
			pp.addEnrichmentFunction(enrichmentFunctionList.get(n-1));
		}
	}
}
