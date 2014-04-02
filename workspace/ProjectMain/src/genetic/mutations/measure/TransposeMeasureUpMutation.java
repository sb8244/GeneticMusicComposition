package genetic.mutations.measure;

import measure.Beat;
import measure.Measure;
import genetic.interfaces.MeasureMutation;

/**
 * Concrete Mutation which transposes all pitches in a measure up by 1
 * @author Steve
 *
 */
public class TransposeMeasureUpMutation extends MeasureMutation
{
	/**
	 * 
	 * @param m The Measure to operate on
	 */
	public TransposeMeasureUpMutation(Measure m) 
	{
		super(m);
	}

	public void mutate() throws Exception 
	{
		for(Beat b: this.measure.getBeats())
		{
			b.incrementPitch();
		}
	}

}
