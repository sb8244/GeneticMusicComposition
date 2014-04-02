package genetic.interfaces;

import measure.Measure;

/**
 * Abstract mutation operating on Measures[]
 * @author Steve
 *
 */
public abstract class MeasuresMutation implements Mutation
{
	protected Measure[] measures;
	
	/**
	 * 
	 * @param measures The Measure[] this will mutate on
	 */
	public MeasuresMutation(Measure[] measures)
	{
		this.measures = measures;
	}
}
