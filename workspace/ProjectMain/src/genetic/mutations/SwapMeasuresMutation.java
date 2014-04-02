package genetic.mutations;

import support.SeededRandom;
import measure.Measure;
import genetic.interfaces.MeasuresMutation;

/**
 * Concrete Mutation which randomly swaps two measures in the array
 * @author Steve
 *
 */
public class SwapMeasuresMutation extends MeasuresMutation
{
	
	/**
	 * 
	 * @param measures The Measure[] to operate on
	 */
	public SwapMeasuresMutation(Measure[] measures) 
	{
		super(measures);
	}

	public void mutate() throws Exception 
	{
		if(this.measures.length <= 1) throw new Exception("SwapMeasuresMutation given an array of size 1 or less");
		
		int firstPosition = (int)(SeededRandom.random()*this.measures.length);
		int secondPosition = (int)(SeededRandom.random()*this.measures.length);
		//don't swap if firstPosition == secondPosition, find new secondPosition
		while(secondPosition == firstPosition)
		{
			secondPosition = (int)(SeededRandom.random()*this.measures.length);
		}

		Measure temp = this.measures[firstPosition];
		this.measures[firstPosition] = this.measures[secondPosition];
		this.measures[secondPosition] = temp;
	}

}
