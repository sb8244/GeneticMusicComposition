package genetic.mutations.measure;

import measure.Measure;
import genetic.interfaces.MeasureMutation;

/**
 * Concrete Mutation which reverses a Measure
 * @author Steve
 *
 */
public class ReverseMeasureMutation extends MeasureMutation
{
	/**
	 * 
	 * @param m The measure to operate on
	 */
	public ReverseMeasureMutation(Measure m) 
	{
		super(m);
	}

	public void mutate() throws Exception 
	{
		this.measure.reverseBeats();
	}

}
