package genetic.mutations.measure;

import measure.Measure;
import genetic.interfaces.MeasureMutation;

/**
 * Concrete Mutation which randomizes a Measure
 * @author Steve
 *
 */
public class RandomizeMeasureMutation extends MeasureMutation
{
	boolean canHaveRests;
	/**
	 * 
	 * @param m The Measure to operate on
	 * @param canHaveRests Can randomization produce rests
	 */
	public RandomizeMeasureMutation(Measure m, boolean canHaveRests) 
	{
		super(m);
		this.canHaveRests = canHaveRests;
	}

	
	public void mutate() throws Exception 
	{
		this.measure.randomize(this.canHaveRests);
	}

}
