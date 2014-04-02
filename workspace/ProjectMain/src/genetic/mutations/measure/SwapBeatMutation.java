package genetic.mutations.measure;

import support.SeededRandom;
import measure.Measure;
import genetic.interfaces.MeasureMutation;

/**
 * Concrete Mutation in charge of swapping 2 random beats in a measure
 * @author Steve
 *
 */
public class SwapBeatMutation extends MeasureMutation
{
	/**
	 * 
	 * @param m The Measure to operate on
	 */
	public SwapBeatMutation(Measure m) 
	{
		super(m);
	}

	public void mutate() throws Exception 
	{
		//Can only swap if there are more than 1 beats
		if(this.measure.getBeats().size() > 1)
		{
			int firstPosition = (int)(SeededRandom.random()*this.measure.getBeats().size());
			int secondPosition = (int)(SeededRandom.random()*this.measure.getBeats().size());
			while(firstPosition == secondPosition)
			{
				secondPosition = (int)(SeededRandom.random()*this.measure.getBeats().size());
			}
			this.measure.swapBeats(firstPosition, secondPosition);
		}
	}

}
