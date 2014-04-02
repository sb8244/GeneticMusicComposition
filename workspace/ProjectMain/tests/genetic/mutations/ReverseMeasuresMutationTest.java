package genetic.mutations;

import static org.junit.Assert.*;
import measure.Measure;

import org.junit.Test;

/**
 * Test suite for ReverseMeasuresMutation
 * @author Steve
 *
 */
public class ReverseMeasuresMutationTest 
{
	/**
	 * Simple test to make sure that measures were swapped
	 * @throws Exception
	 */
	@Test
	public void testReverseMade() throws Exception
	{
		Measure one = new Measure();
		Measure two = new Measure();
		
		Measure[] measures = new Measure[2];
		measures[0] = one;
		measures[1] = two;
		
		ReverseMeasuresMutation rmm = new ReverseMeasuresMutation(measures);
		
		assertEquals(measures[0], one);
		assertEquals(measures[1], two);
		
		rmm.mutate();
		
		assertEquals(measures[0], two);
		assertEquals(measures[1], one);
	}
}
