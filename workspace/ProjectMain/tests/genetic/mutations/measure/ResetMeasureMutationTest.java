package genetic.mutations.measure;

import static org.junit.Assert.*;
import measure.Beat;
import measure.Measure;

import org.junit.Test;

import suite.FullTestSuite;

/**
 * Test Suite for ResetMeasureMutation
 * @author Steve
 *
 */
public class ResetMeasureMutationTest 
{
	/**
	 * Reset an empty Measure
	 * @throws Exception
	 */
	@Test
	public void simpleResetOne() throws Exception
	{
		Measure m = new Measure();
		
		assertEquals(0, m.getBeats().size());
		
		ResetMeasureMutation rmm = new ResetMeasureMutation(m, 4);
		rmm.mutate();
		
		assertEquals(4, m.getBeats().size());
		for(Beat b: m.getBeats())
		{
			assertEquals(1.0, b.getBeatDuration(), FullTestSuite.DELTA);
			assertEquals(60, b.getNoteValue());
			assertFalse(b.isRest());
		}
	}
	
	/**
	 * Reset a Measure which has beats
	 * @throws Exception
	 */
	@Test
	public void simpleResetTwo() throws Exception
	{
		Measure m = new Measure();
		m.addBeat(new Beat(.25));
		m.addBeat(new Beat(.25));
		m.addBeat(new Beat(.5));
		m.addBeat(new Beat(4));
		m.addBeat(new Beat(1));
		
		assertEquals(5, m.getBeats().size());
		
		ResetMeasureMutation rmm = new ResetMeasureMutation(m, 4);
		rmm.mutate();
		
		assertEquals(4, m.getBeats().size());
		for(Beat b: m.getBeats())
		{
			assertEquals(1.0, b.getBeatDuration(), FullTestSuite.DELTA);
			assertEquals(60, b.getNoteValue());
			assertFalse(b.isRest());
		}
	}
}
