package genetic.interfaces;

import measure.Measure;

/**
 * Abstract Mutation which will operate on a Measure
 * @author Steve
 *
 */
public abstract class MeasureMutation implements Mutation
{
	protected Measure measure;
	
	/**
	 * 
	 * @param m The Measure to operate on
	 */
	public MeasureMutation(Measure m)
	{
		this.measure = m;
	}
}
