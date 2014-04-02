package genetic.mutations.measure;

import measure.Measure;
import genetic.interfaces.MeasureMutation;

/**
 * Concrete Mutation which Resets a Measure to 4 beats of 1 duration and middle C note
 * @author Steve
 *
 */
public class ResetMeasureMutation extends MeasureMutation
{
	private int numBeats;
	
	/**
	 * 
	 * @param m The Measure to operate on
	 */
	public ResetMeasureMutation(Measure m, int numBeats) 
	{
		super(m);
		this.numBeats = numBeats;
	}

	
	public void mutate() throws Exception 
	{
		this.measure.reset(numBeats);
	}

}
