package genetic.mutations.measure;

import static org.junit.Assert.*;
import measure.Beat;
import measure.Measure;

import org.junit.Test;

/**
 * Test Suite for SplitBeatMutation
 * @author Steve
 *
 */
public class SplitBeatMutationTest 
{
	/**
	 * Split a single beat which can be split from 1.0 to .5
	 * @throws Exception
	 */
	@Test
	public void testSingleSplitPossible() throws Exception
	{
		Measure m = new Measure();
		m.addBeat(new Beat(1.0));
		
		assertEquals(1, m.getBeats().size());
		
		SplitBeatMutation sbm = new SplitBeatMutation(m);
		sbm.mutate();
		
		assertEquals(2, m.getBeats().size());
		assertEquals(.5, m.getBeats().get(0).getBeatDuration(), .0000000005);
		assertEquals(.5, m.getBeats().get(1).getBeatDuration(), .0000000005);
		
		assertTrue(m.getBeats().get(0) != m.getBeats().get(1));
	}
	
	/**
	 * This beat is at the minimum so it should not be split
	 * @throws Exception
	 */
	@Test
	public void testSingleSplitNotPossible() throws Exception
	{
		Measure m = new Measure();
		m.addBeat(new Beat(Beat.MIN_DURATION));
		
		assertEquals(1, m.getBeats().size());
		
		SplitBeatMutation sbm = new SplitBeatMutation(m);
		sbm.mutate();
		
		assertEquals(1, m.getBeats().size());
		assertEquals(Beat.MIN_DURATION, m.getBeats().get(0).getBeatDuration(), .0000000005);
	}
}
