package genetic.mutations.measure;

import static org.junit.Assert.*;

import org.junit.Test;

import measure.Beat;
import measure.Measure;
/**
 * Test Suite for TransposePitchUpMutation
 * @author Steve
 *
 */
public class TransposeMeasureUpMutationTest
{
	/**
	 * Simple single note
	 * @throws Exception
	 */
	@Test
	public void testSimple() throws Exception
	{
		Measure m = new Measure();
		m.addBeat(new Beat(60, 1));
		
		TransposeMeasureUpMutation tpdm = new TransposeMeasureUpMutation(m);
		tpdm.mutate();
		
		assertEquals(61, m.getBeats().get(0).getNoteValue());
	}
	
	/**
	 * Complex Measure with many notes, 127 border case
	 * @throws Exception
	 */
	@Test
	public void testComplex() throws Exception
	{
		Measure m = new Measure();
		m.addBeat(new Beat(60, 1));
		m.addBeat(new Beat(65, 1));
		m.addBeat(new Beat(0, 1));
		m.addBeat(new Beat(127, 1));
		
		TransposeMeasureUpMutation tpdm = new TransposeMeasureUpMutation(m);
		tpdm.mutate();
		
		assertEquals(61, m.getBeats().get(0).getNoteValue());
		assertEquals(66, m.getBeats().get(1).getNoteValue());
		assertEquals(1, m.getBeats().get(2).getNoteValue());
		assertEquals(127, m.getBeats().get(3).getNoteValue());
	}
}
