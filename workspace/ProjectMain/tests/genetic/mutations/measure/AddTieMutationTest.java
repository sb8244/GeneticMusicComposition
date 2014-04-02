package genetic.mutations.measure;

import static org.junit.Assert.*;

import org.junit.Test;

import measure.Beat;
import measure.Measure;

/**
 * Test Suite for AddTieMutation
 * @author Steve
 *
 */
public class AddTieMutationTest 
{
	/**
	 * Really basic test to make sure it works
	 */
	@Test
	public void simpleTest()
	{
		Measure m = new Measure();
		Beat b = new Beat(1.0);
		m.addBeat(b);
		
		assertFalse(b.isTiedForward());
		
		AddTieMutation mut = new AddTieMutation(m);
		boolean swapped = false;
		
		//100 tries at 50% chance should always create a swapped beat
		for(int i = 0; i < 100; i++)
		{
			mut.mutate();
			swapped = swapped || b.isTiedForward();
		}
		
		assertTrue(swapped);
	}
}
