package genetic.mutations;

import org.apache.commons.lang3.ArrayUtils;

import measure.Measure;
import genetic.interfaces.MeasuresMutation;

/**
 * Concrete Mutation which reverse a Measure array
 * @author Steve
 *
 */
public class ReverseMeasuresMutation extends MeasuresMutation 
{
	/**
	 * 
	 * @param measures the Measure[] to operate on
	 */
	public ReverseMeasuresMutation(Measure[] measures) 
	{
		super(measures);
	}

	public void mutate() throws Exception 
	{
		ArrayUtils.reverse(measures);
	}

}
