package genetic.mutations.measure;

import static org.junit.Assert.*;
import measure.Beat;
import measure.Measure;

import org.junit.Test;

/**
 * Test Suite for ReverseMeasureMutation
 * @author Steve
 *
 */
public class ReverseMeasureMutationTest 
{
	/**
	 * Simple reversal of two beats
	 * @throws Exception
	 */
	@Test
	public void simpleReverse() throws Exception
	{
		Beat one = new Beat(1);
		Beat two = new Beat(2);
		Measure m = new Measure();
		m.addBeat(one);
		m.addBeat(two);
		
		assertEquals(one, m.getBeats().get(0));
		assertEquals(two, m.getBeats().get(1));
		
		ReverseMeasureMutation rmm = new ReverseMeasureMutation(m);
		rmm.mutate();
		
		assertEquals(two, m.getBeats().get(0));
		assertEquals(one, m.getBeats().get(1));
	}
	
	/**
	 * Simple reversal of one beat
	 * @throws Exception
	 */
	@Test
	public void reverseOne() throws Exception
	{
		Beat one = new Beat(1);
		Measure m = new Measure();
		m.addBeat(one);
		
		assertEquals(one, m.getBeats().get(0));
		
		ReverseMeasureMutation rmm = new ReverseMeasureMutation(m);
		rmm.mutate();
		
		assertEquals(one, m.getBeats().get(0));
	}
	
	/**
	 * Simple reversal of many beats
	 * @throws Exception
	 */
	@Test
	public void simpleReverseMany() throws Exception
	{
		Measure m = new Measure();
		for(int i = 0; i < Beat.POSSIBLE_DURATIONS.length; i++)
			m.addBeat(new Beat(Beat.POSSIBLE_DURATIONS[i]));
		
		for(int i = 0; i < m.getBeats().size() -1 ; i++)
		{
			assertTrue(m.getBeats().get(i).getBeatDuration() < m.getBeats().get(i+1).getBeatDuration());
		}
		
		ReverseMeasureMutation rmm = new ReverseMeasureMutation(m);
		rmm.mutate();
		
		for(int i = 0; i < m.getBeats().size() -1 ; i++)
		{
			assertTrue(m.getBeats().get(i).getBeatDuration() > m.getBeats().get(i+1).getBeatDuration());
		}
	}
}
