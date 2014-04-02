package processor;

import static org.junit.Assert.*;
import measure.Beat;
import measure.Measure;

import org.junit.Test;

import suite.FullTestSuite;

/**
 * test Suite for MeasureMerge
 * @author Steve
 *
 */
public class MeasureMergeTest 
{
	/**
	 * Repeatedly get the next tone when there is only 1 measure with 1 tone
	 */
	@Test
	public void getNextToneSingleToneRepeated()
	{
		Measure m = new Measure();
		Beat b = new Beat(60, 1);
		m.addBeat(b);
		Measure[] ms = new Measure[1];
		ms[0] = m;
		
		MeasureMerge mm = new MeasureMerge(null, ms);
		
		for(int i = 0; i < 100; i++)
		{
			assertEquals(b, mm.getNextToneBeat());
		}
	}

	/**
	 * Test the incrementToneMeasureCount affects the iteration properly
	 */
	@Test
	public void testIncrementMeasureCount()
	{
		Measure m = new Measure();
		Beat b = new Beat(60, 1);
		m.addBeat(b);
		m.addBeat(new Beat(65, 4));
		Measure[] ms = new Measure[2];
		ms[0] = m;
		ms[1] = new Measure();
		ms[1].addBeat(b);
		
		MeasureMerge mm = new MeasureMerge(null, ms);
		
		for(int i = 0; i < 100; i++)
		{
			//the beat should always be b because it skips ms[0].beat[1]
			assertEquals(b, mm.getNextToneBeat());
			mm.incrementToneMeasureCount();
		}
	}
	
	/**
	 * getNextTone with a single measure of 2 beats, repeated
	 */
	@Test
	public void getNextToneSingleMeasureRepeated()
	{
		Measure m = new Measure();
		Beat even = new Beat(60, 1);
		Beat odd = new Beat(61, 1);
		m.addBeat(even);
		m.addBeat(odd);
		Measure[] ms = new Measure[1];
		ms[0] = m;
		
		MeasureMerge mm = new MeasureMerge(null, ms);
		
		for(int i = 0; i < 100; i++)
		{
			if(i % 2 == 0)
				assertEquals(even, mm.getNextToneBeat());
			else
				assertEquals(odd, mm.getNextToneBeat());
		}
	}
	
	/**
	 * getNextTone with multiple measures of 1 beat each
	 */
	@Test
	public void getNextToneMultipleMeasuresRepeated()
	{
		Measure m = new Measure();
		Measure m2 = new Measure();
		Beat even = new Beat(60, 1);
		Beat odd = new Beat(61, 1);
		m.addBeat(even);
		m2.addBeat(odd);
		Measure[] ms = new Measure[2];
		ms[0] = m;
		ms[1] = m2;
		
		MeasureMerge mm = new MeasureMerge(null, ms);
		
		for(int i = 0; i < 100; i++)
		{
			if(i % 2 == 0)
				assertEquals(even, mm.getNextToneBeat());
			else
				assertEquals(odd, mm.getNextToneBeat());
			mm.incrementToneMeasureCount();
		}
	}
	
	/**
	 * getNextTone with multiple measures and multiple beats
	 */
	@Test
	public void getNextToneMultipleMeasuresMultipleBeatsRepeated()
	{
		Measure m = new Measure();
		Measure m2 = new Measure();
		Beat zero = new Beat(60, 1);
		Beat one = new Beat(61, 1);
		Beat two = new Beat(61, 1);
		Beat three = new Beat(61, 1);
		m.addBeat(zero);
		m.addBeat(one);
		m2.addBeat(two);
		m2.addBeat(three);
		Measure[] ms = new Measure[2];
		ms[0] = m;
		ms[1] = m2;
		
		MeasureMerge mm = new MeasureMerge(null, ms);
		
		for(int i = 0; i < 100; i++)
		{
			if(i % 4 == 0)
				assertEquals(zero, mm.getNextToneBeat());
			else if(i % 4 == 1)
				assertEquals(one, mm.getNextToneBeat());
			else if(i % 4 == 2)
				assertEquals(two, mm.getNextToneBeat());
			else if(i % 4 == 3)
				assertEquals(three, mm.getNextToneBeat());
			if(i%4 == 1 || i % 4 == 3)
				mm.incrementToneMeasureCount();
		}
	}
	
	/**
	 * Perform a 1 beat merge
	 */
	@Test
	public void testSimpleMerge()
	{
		Measure[] rhythm = new Measure[1];
		Measure[] tone = new Measure[1];
		rhythm[0] = new Measure();
		tone[0] = new Measure();
		rhythm[0].addBeat(new Beat(60, 1));
		tone[0].addBeat(new Beat(70, 4));
		
		MeasureMerge mm = new MeasureMerge(rhythm, tone);
		Measure[] merged = mm.merge();
		
		assertEquals(1, merged.length);
		assertEquals(1, merged[0].getBeats().size());
		assertEquals(70, merged[0].getBeats().get(0).getNoteValue());
		assertEquals(1.0, merged[0].getBeats().get(0).getBeatDuration(), FullTestSuite.DELTA);
	}
	
	
	/**
	 * Perform a 1 beat merge where the rhythm is a rest
	 */
	@Test
	public void testSimpleMergeRest()
	{
		Measure[] rhythm = new Measure[1];
		Measure[] tone = new Measure[1];
		rhythm[0] = new Measure();
		tone[0] = new Measure();
		rhythm[0].addBeat(new Beat(1));
		tone[0].addBeat(new Beat(70, 4));
		
		MeasureMerge mm = new MeasureMerge(rhythm, tone);
		Measure[] merged = mm.merge();
		
		assertEquals(1, merged.length);
		assertEquals(1, merged[0].getBeats().size());
		assertTrue(merged[0].getBeats().get(0).isRest());
		assertEquals(1.0, merged[0].getBeats().get(0).getBeatDuration(), FullTestSuite.DELTA);
	}
	
	/**
	 * Perform a 1 beat merge where there is more tone than rhythm
	 */
	@Test
	public void testSimpleMergeMoreTones()
	{
		Measure[] rhythm = new Measure[1];
		Measure[] tone = new Measure[1];
		rhythm[0] = new Measure();
		tone[0] = new Measure();
		rhythm[0].addBeat(new Beat(60, 1));
		tone[0].addBeat(new Beat(70, 4));
		tone[0].addBeat(new Beat(71, 4));
		
		MeasureMerge mm = new MeasureMerge(rhythm, tone);
		Measure[] merged = mm.merge();
		
		assertEquals(1, merged.length);
		assertEquals(1, merged[0].getBeats().size());
		assertEquals(70, merged[0].getBeats().get(0).getNoteValue());
		assertEquals(1.0, merged[0].getBeats().get(0).getBeatDuration(), FullTestSuite.DELTA);
	}
	
	/**
	 * Perform a 1 beat merge where there is more tone than rhythm
	 */
	@Test
	public void testSimpleMergeMoreRhythm()
	{
		Measure[] rhythm = new Measure[1];
		Measure[] tone = new Measure[1];
		rhythm[0] = new Measure();
		tone[0] = new Measure();
		rhythm[0].addBeat(new Beat(60, 1));
		rhythm[0].addBeat(new Beat(71, 4));
		tone[0].addBeat(new Beat(70, 2));
		
		MeasureMerge mm = new MeasureMerge(rhythm, tone);
		Measure[] merged = mm.merge();
		
		assertEquals(1, merged.length);
		assertEquals(2, merged[0].getBeats().size());
		assertEquals(70, merged[0].getBeats().get(0).getNoteValue());
		assertEquals(1.0, merged[0].getBeats().get(0).getBeatDuration(), FullTestSuite.DELTA);
		
		assertEquals(70, merged[0].getBeats().get(1).getNoteValue());
		assertEquals(4.0, merged[0].getBeats().get(1).getBeatDuration(), FullTestSuite.DELTA);
	}
	
	/**
	 * Perform a multiple measure merge, normal case
	 */
	@Test
	public void multipleMeasureMerge()
	{
		Measure[] rhythm = new Measure[3];
		Measure[] tone = new Measure[3];
		for(int i = 0; i < rhythm.length; i++)
		{
			rhythm[i] = new Measure();
			tone[i] = new Measure();
		}
		rhythm[0].addBeat(new Beat(60, 1));
		rhythm[0].addBeat(new Beat(60, 2));
		rhythm[0].addBeat(new Beat(60, 1));
		rhythm[0].addBeat(new Beat(60, 1));
		
		rhythm[1].addBeat(new Beat(4));
		
		rhythm[2].addBeat(new Beat(60, .5));
		rhythm[2].addBeat(new Beat(60, 2));
		rhythm[2].addBeat(new Beat(60, .5));
		rhythm[2].addBeat(new Beat(60, 1));
		rhythm[2].addBeat(new Beat(60, 1));
		
		int currentNote = 70;
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 4; j++, currentNote++)
			{
				tone[i].addBeat(new Beat(currentNote, 1));
			}
		}
		
		MeasureMerge mm = new MeasureMerge(rhythm, tone);
		Measure[] merged = mm.merge();
		
		assertEquals(3, merged.length);
		assertEquals(4, merged[0].getBeats().size());
		assertEquals(1, merged[1].getBeats().size());
		assertEquals(5, merged[2].getBeats().size());
		
		assertEquals(1.0, merged[0].getBeats().get(0).getBeatDuration(), FullTestSuite.DELTA);
		assertEquals(2.0, merged[0].getBeats().get(1).getBeatDuration(), FullTestSuite.DELTA);
		assertEquals(1.0, merged[0].getBeats().get(2).getBeatDuration(), FullTestSuite.DELTA);
		assertEquals(1.0, merged[0].getBeats().get(3).getBeatDuration(), FullTestSuite.DELTA);

		assertEquals(4.0, merged[1].getBeats().get(0).getBeatDuration(), FullTestSuite.DELTA);
		
		assertEquals(.5, merged[2].getBeats().get(0).getBeatDuration(), FullTestSuite.DELTA);
		assertEquals(2, merged[2].getBeats().get(1).getBeatDuration(), FullTestSuite.DELTA);
		assertEquals(.5, merged[2].getBeats().get(2).getBeatDuration(), FullTestSuite.DELTA);
		assertEquals(1, merged[2].getBeats().get(3).getBeatDuration(), FullTestSuite.DELTA);
		assertEquals(1, merged[2].getBeats().get(4).getBeatDuration(), FullTestSuite.DELTA);
		
		assertEquals(70, merged[0].getBeats().get(0).getNoteValue());
		assertEquals(71, merged[0].getBeats().get(1).getNoteValue());
		assertEquals(72, merged[0].getBeats().get(2).getNoteValue());
		assertEquals(73, merged[0].getBeats().get(3).getNoteValue());
		
		assertTrue(merged[1].getBeats().get(0).isRest());
		//skip 74,75,76,77
		
		assertEquals(78, merged[2].getBeats().get(0).getNoteValue());
		assertEquals(79, merged[2].getBeats().get(1).getNoteValue());
		assertEquals(80, merged[2].getBeats().get(2).getNoteValue());
		assertEquals(81, merged[2].getBeats().get(3).getNoteValue());
		assertEquals(78, merged[2].getBeats().get(4).getNoteValue());
	}
}
