package genetic.mutations.measure;

import measure.Beat;
import measure.Measure;
import genetic.interfaces.MeasureMutation;

/**
 * Concrete mutation which transposes an entire Measure down by 1 value
 * @author Steve
 *
 */
public class TransposeMeasureDownMutation extends MeasureMutation
{

	/**
	 * 
	 * @param m The Measure to operate on
	 */
	public TransposeMeasureDownMutation(Measure m) 
	{
		super(m);
	}

	public void mutate() throws Exception 
	{
		for(Beat b: this.measure.getBeats())
		{
			b.decrementPitch();
		}
	}

}
