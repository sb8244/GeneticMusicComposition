package genetic.mutations.measure;

import static org.junit.Assert.*;
import measure.Beat;
import measure.Measure;

import org.junit.Test;

/**
 * Test Suite for TransposeSinglePitchMutation
 * @author Steve
 *
 */
public class TransposeSinglePitchMutationTest 
{
	/**
	 * Single note in a measure
	 * @throws Exception
	 */
	@Test
	public void simple() throws Exception
	{
		Measure m = new Measure();
		m.addBeat(new Beat(60, 1));
		
		TransposeSinglePitchMutation tspm = new TransposeSinglePitchMutation(m);
		tspm.mutate();
		
		double pitch = m.getBeats().get(0).getNoteValue();
		assertTrue(pitch == 61 || pitch == 59);
	}
	
	/**
	 * Many notes in a measure
	 * @throws Exception
	 */
	@Test
	public void complex() throws Exception
	{
		Measure m = new Measure();
		m.addBeat(new Beat(60, 1));
		m.addBeat(new Beat(65, 1));
		m.addBeat(new Beat(70, 1));
		m.addBeat(new Beat(75, 1));
		
		TransposeSinglePitchMutation tspm = new TransposeSinglePitchMutation(m);
		tspm.mutate();
		
		int changedPitches = 0;
		if(m.getBeats().get(0).getNoteValue() != 60)
			changedPitches++;
		if(m.getBeats().get(1).getNoteValue() != 65)
			changedPitches++;
		if(m.getBeats().get(2).getNoteValue() != 70)
			changedPitches++;
		if(m.getBeats().get(3).getNoteValue() != 75)
			changedPitches++;
		
		assertEquals(1, changedPitches);
	}
}
