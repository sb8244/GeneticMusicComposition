package genetic.mutations.measure;

import static org.junit.Assert.*;
import measure.Beat;
import measure.Measure;

import org.junit.Test;

/**
 * Test Suite for SwapBeatMutation
 * @author Steve
 *
 */
public class SwapBeatMutationTest
{
	/**
	 * Test that a swap gets made when a Measure contains all Beat durations possible
	 * @throws Exception
	 */
	@Test
	public void testSwapGetsMade() throws Exception
	{
		Measure m = new Measure();
		for(int i = 0; i < Beat.POSSIBLE_DURATIONS.length; i++)
			m.addBeat(new Beat(Beat.POSSIBLE_DURATIONS[i]));
		
		for(int i = 0; i < m.getBeats().size() - 1; i++)
		{
			assertTrue(m.getBeats().get(i).getBeatDuration() < m.getBeats().get(i+1).getBeatDuration());
		}
		
		SwapBeatMutation sbm = new SwapBeatMutation(m);
		sbm.mutate();
		
		int swaps = 0;
		for(int i = 0; i < m.getBeats().size() - 1; i++)
		{
			if(m.getBeats().get(i).getBeatDuration() > m.getBeats().get(i+1).getBeatDuration())
				swaps++;
		}
		
		//1 or 2 swaps must always happen
		assertTrue(swaps == 1 || swaps == 2);
	}
	
	/**
	 * No swap should occur because there is only 1 beat
	 * @throws Exception
	 */
	@Test
	public void testNoSwap() throws Exception
	{
		Beat b = new Beat(1);
		Measure m = new Measure();
		m.addBeat(b);
		
		assertEquals(1, m.getBeats().size());
		assertEquals(b, m.getBeats().get(0));
		
		SwapBeatMutation sbm = new SwapBeatMutation(m);
		sbm.mutate();
		
		assertEquals(1, m.getBeats().size());
		assertEquals(b, m.getBeats().get(0));
	}
}