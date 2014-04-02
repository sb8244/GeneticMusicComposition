package genetic.mutations;

import static org.junit.Assert.*;
import measure.Measure;

import org.junit.Test;

/**
 * Test suite for SwapMeasuresMutation
 * @author Steve
 *
 */
public class SwapMeasuresMutationTest 
{
	/**
	 * Check that a swap was made
	 * @throws Exception
	 */
	@Test
	public void testSwapMade() throws Exception
	{
		Measure one = new Measure();
		Measure two = new Measure();
		
		Measure[] m = new Measure[2];
		m[0] = one;
		m[1] = two;
		
		assertEquals(one, m[0]);
		assertEquals(two, m[1]);
		
		SwapMeasuresMutation smm = new SwapMeasuresMutation(m);
		smm.mutate();
		
		assertEquals(one, m[1]);
		assertEquals(two, m[0]);
	}
	
	/**
	 * Check that an exception is thrown if array size is 1
	 * @throws Exception
	 */
	@Test(expected=Exception.class)
	public void testSwapNotMade() throws Exception
	{
		Measure one = new Measure();
		
		Measure[] m = new Measure[1];
		m[0] = one;
		
		SwapMeasuresMutation smm = new SwapMeasuresMutation(m);
		smm.mutate();
	}
}
