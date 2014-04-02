package genetic.mutations.measure;

import static org.junit.Assert.*;
import measure.Beat;
import measure.Measure;

import org.junit.Test;

/**
 * Test Suite for JoinBeatMutation
 * @author Steve
 *
 */
public class JoinBeatMutationTest 
{
	/**
	 * for Double compares
	 */
	public static double DELTA = .0000005;
	
	/**
	 * Simple test where two beats that can be joined are joined
	 * @throws Exception
	 */
	@Test
	public void simpleJoin() throws Exception
	{
		Measure m = new Measure();
		m.addBeat(new Beat(1));
		m.addBeat(new Beat(1));
		
		assertEquals(2, m.getBeats().size());
		
		JoinBeatMutation jbm = new JoinBeatMutation(m);
		jbm.mutate();
		
		assertEquals(1, m.getBeats().size());
		assertEquals(2.0, m.getBeats().get(0).getBeatDuration(), DELTA);
	}
	
	/**
	 * Nothing should happen because the beats do not sum to a merged beat value
	 * @throws Exception 
	 */
	@Test
	public void joinNotPossible() throws Exception
	{
		Measure m = new Measure();
		m.addBeat(new Beat(.25));
		m.addBeat(new Beat(1));
		
		assertEquals(2, m.getBeats().size());
		
		JoinBeatMutation jbm = new JoinBeatMutation(m);
		jbm.mutate();
		
		assertEquals(2, m.getBeats().size());
		assertEquals(0.25, m.getBeats().get(0).getBeatDuration(), DELTA);
		assertEquals(1.0, m.getBeats().get(1).getBeatDuration(), DELTA);
	}
	
	/**
	 * Perform a join with a non similar beat duration on the left
	 * @throws Exception
	 */
	@Test
	public void joinWithOthers() throws Exception
	{
		Measure m = new Measure();
		m.addBeat(new Beat(2));
		m.addBeat(new Beat(.25));
		m.addBeat(new Beat(.25));
		
		assertEquals(3, m.getBeats().size());
		
		JoinBeatMutation jbm = new JoinBeatMutation(m);
		jbm.mutate();
		
		assertEquals(2, m.getBeats().size());
		assertEquals(2.0, m.getBeats().get(0).getBeatDuration(), DELTA);
		assertEquals(.5, m.getBeats().get(1).getBeatDuration(), DELTA);
	}
	
	/**
	 * Perform a join with all similar beat durations
	 * @throws Exception
	 */
	@Test
	public void joinWithManySimilar() throws Exception
	{
		Measure m = new Measure();
		m.addBeat(new Beat(2));
		m.addBeat(new Beat(2));
		m.addBeat(new Beat(2));
		m.addBeat(new Beat(2));
		
		assertEquals(4, m.getBeats().size());
		
		JoinBeatMutation jbm = new JoinBeatMutation(m);
		jbm.mutate();
		
		assertEquals(3, m.getBeats().size());
		
		//1 should be bigger, 0 should be less; it's random
		int seenBigger = 0;
		int seenLess = 0;
		for(Beat b: m.getBeats())
		{
			if(b.getBeatDuration() > (2.0 + DELTA))
				seenBigger++;
			if(b.getBeatDuration() < (2.0 - DELTA))
				seenLess--;
		}
		assertEquals(1, seenBigger);
		assertEquals(0, seenLess);
	}
}
