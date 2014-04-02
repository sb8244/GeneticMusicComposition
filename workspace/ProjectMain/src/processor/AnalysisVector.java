package processor;

import java.util.HashMap;

import processor.exceptions.AnalysisVectorMismatchException;

/**
 * Extended HashMap which will allow for distance between "Vectors" to be calculated
 * 	using Euclidean Distance
 * 
 * A HashMap is used instead of ArrayList or Vector because HashMap is order agnostic and arrays aren't
 * 	This would mean that every type of the same array would need to populate in the same order, which could be 
 *  and entry point for bugs
 *  
 * @author Steve
 *
 */
public class AnalysisVector extends HashMap<String, Double>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6014851739482268758L;

	/**
	 * Perform the distance formula on two vector's to return the distance between
	 * EucDis = sqrt( (A1-B1)^2 + (A2-B2)^2 + (A3-B3)^2 + (A4-B4)^2 + (A5-B5)^2 )
	 * @param that The other AnalysisVector to compute distance from
	 * @return Euclidean distance between these two vectors
	 * @throws AnalysisVectorMismatchException
	 */
	public double distance(AnalysisVector that) throws AnalysisVectorMismatchException
	{
		if(this.size() != that.size())
			throw new AnalysisVectorMismatchException("Vector size mismatch");
		double runningSum = 0.0;
		for(String key: this.keySet())
		{
			try
			{
				double thisValue = this.get(key);
				double thatValue = that.get(key);
				runningSum += (thisValue - thatValue) * (thisValue - thatValue);
			} catch(NullPointerException e)
			{
				throw new AnalysisVectorMismatchException("Keyset mismatch");
			}
		}
		return Math.sqrt(runningSum);
	}
}
