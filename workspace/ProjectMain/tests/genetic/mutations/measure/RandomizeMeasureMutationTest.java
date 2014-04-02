package genetic.mutations.measure;

import static org.junit.Assert.*;

import java.util.ArrayList;

import measure.Beat;
import measure.Measure;

import org.junit.Test;

import suite.FullTestSuite;

/**
 * Test Suite for RandomizeMeasureMutation
 * @author Steve
 *
 */
public class RandomizeMeasureMutationTest 
{
	/**
	 * Randomize a single beat in a measure
	 * @throws Exception
	 */
	@Test
	public void simpleRandomize() throws Exception
	{
		Beat b = new Beat(1.0);
		Measure m = new Measure();
		m.addBeat(b);
		
		assertEquals(1.0, this.getMeasureDuration(m), FullTestSuite.DELTA);
		
		RandomizeMeasureMutation rmm = new RandomizeMeasureMutation(m, true);
		rmm.mutate();
		
		assertEquals(1.0, this.getMeasureDuration(m), FullTestSuite.DELTA);
		//the beats shouldn't be the same Beat
		assertTrue(m.getBeats().get(0) != b);
	}
	
	/**
	 * Test a more realistic randomization
	 * @throws Exception
	 */
	@Test
	public void largerRandomize() throws Exception
	{
		ArrayList<Beat> beats = new ArrayList<Beat>();
		beats.add(new Beat(1.0));
		beats.add(new Beat(60, .25));
		beats.add(new Beat(60, .25));
		beats.add(new Beat(64, .5));
		beats.add(new Beat(62, 2.0));
		Measure m = new Measure();
		for(Beat b: beats)
			m.addBeat(b);
		
		assertEquals(4.0, this.getMeasureDuration(m), FullTestSuite.DELTA);
		
		RandomizeMeasureMutation rmm = new RandomizeMeasureMutation(m, true);
		rmm.mutate();
		
		assertEquals(4.0, this.getMeasureDuration(m), FullTestSuite.DELTA);
		//no beat shouldn't be the same as the input
		for(Beat newBeat: m.getBeats())
			assertFalse(beats.contains(newBeat));
	}
	
	/**
	 * 
	 * @param m
	 * @return The sum of beat durations in this measure
	 */
	public double getMeasureDuration(Measure m)
	{
		double sum  = 0.0;
		for(Beat b: m.getBeats())
			sum += b.getBeatDuration();
		return sum;
	}
}
