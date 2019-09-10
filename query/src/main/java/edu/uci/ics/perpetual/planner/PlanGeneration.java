package edu.uci.ics.perpetual.planner;

import java.util.List;
import java.util.PriorityQueue;
import java.util.ArrayList;

import edu.uci.ics.perpetual.data.*;

import edu.uci.ics.perpetual.model.BlockPath;
import edu.uci.ics.perpetual.model.EnrichmentFunctionInfo;
import edu.uci.ics.perpetual.model.ObjectState;
import edu.uci.ics.perpetual.model.PlanPath;
import edu.uci.ics.perpetual.model.BlockState;
import edu.uci.ics.perpetual.model.BlockPath;


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
	public PriorityQueue<BlockPath> getInitialPlanPathBlockBased(List<EnrichmentFunctionInfo> enrichmentFunctionList, List<ObjectState> objectStateList){
		PriorityQueue<BlockPath> blockPQ = new PriorityQueue<>();
		int blockSize = 20;
		ArrayList<DataObject> blockObjectList =  new ArrayList<DataObject>();
		for(int i=0;i<objectStateList.size();i++)
		{
			if(i%blockSize == 0 && i > 0) {				
				BlockState blockState = new BlockState();
				blockState.setObjectList(blockObjectList);
				blockState.setBlocksize(blockSize);
				blockPQ.add(planOneBlock(enrichmentFunctionList, blockState));
				blockObjectList =  new ArrayList<DataObject>();
			}
			blockObjectList.add(objectStateList.get(i).getObject());			
		}
		//System.out.println("number of blocks created = "+ blockPQ.size());
		return blockPQ;
	}
	
	public BlockPath planOneBlock(List<EnrichmentFunctionInfo> enrichmentFunctionList, BlockState blockState) {
		BlockPath blockPath = new BlockPath();
		blockPath.setBlockState(blockState);
		int index = getLatestBlockEvaluatedFunction(blockState);
		if(index+1 == enrichmentFunctionList.size())
			return blockPath;
		OGP_Block(index+1, enrichmentFunctionList, blockPath);
		blockPath.calculateCost();
		//pp.calculateCost();
		return blockPath;
	}
	
	
	
	public PlanPath planOneObject(List<EnrichmentFunctionInfo> enrichmentFunctionList, ObjectState objectState)
	{
		PlanPath pp = new PlanPath();
		pp.setObject(objectState);
		int index = getLatestEvaluatedFunction(objectState);
		if(index+1 == enrichmentFunctionList.size())
			return pp;
		OGP(index+1, enrichmentFunctionList, pp);
		pp.calculateBenefit();
		//pp.calculateCost();
		return pp;
	}
	private int getLatestEvaluatedFunction(ObjectState objectState) {
		int index = -1;
		for(int i=0;i<objectState.getFunctionBitmap().size();i++)
			if(objectState.getFunctionBitmap().get(i) == 1)
				index = i;
		return index;			
	}
	
	private int getLatestBlockEvaluatedFunction(BlockState blockState) {
		int index = -1;
		for(int i=0;i<blockState.getFunctionBitmap().size();i++)
			if(blockState.getFunctionBitmap().get(i) == 1)
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
	public void OGP_Block(int startIndex, List<EnrichmentFunctionInfo> enrichmentFunctionList, BlockPath bp)
	{
		if((enrichmentFunctionList.size() - startIndex) == 1)
		{
			bp.addEnrichmentFunction(enrichmentFunctionList.get(enrichmentFunctionList.size()-1));
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
					bp.addEnrichmentFunction(enrichmentFunctionList.get(i-1));
					j = i;
				}
			}
			bp.addEnrichmentFunction(enrichmentFunctionList.get(n-1));
		}
	}
}
