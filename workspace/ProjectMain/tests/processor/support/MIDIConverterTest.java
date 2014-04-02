package processor.support;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import javax.sound.midi.InvalidMidiDataException;

import measure.Measure;

import org.junit.Test;

import suite.FullTestSuite;

/**
 * Test MIDIConverter
 * @author Steve
 *
 */
public class MIDIConverterTest 
{
	/**
	 * Test a measure that has a dotted half note in it
	 * @throws InvalidMidiDataException
	 * @throws IOException
	 */
	@Test
	public void dottedHalfNote() throws InvalidMidiDataException, IOException
	{
		MIDIConverter c = new MIDIConverter("tests/test-input/dottedHalfNote.mid");
		
		c.convert();
		
		ArrayList<Measure> m = c.getConvertedMeasures();
		
		assertEquals(1, m.size());	
		
		assertEquals(m.get(0).getBeats().get(0).getBeatDuration(), 3, FullTestSuite.DELTA);
		assertEquals(m.get(0).getBeats().get(1).getBeatDuration(), 1, FullTestSuite.DELTA);
	}
	
	/**
	 * Test a measure that has a dotted quarter note in it
	 * @throws InvalidMidiDataException
	 * @throws IOException
	 */
	@Test
	public void dottedQuarterNote() throws InvalidMidiDataException, IOException
	{
		MIDIConverter c = new MIDIConverter("tests/test-input/dottedQuarterNote.mid");
		
		c.convert();
		
		ArrayList<Measure> m = c.getConvertedMeasures();
		
		assertEquals(1, m.size());	
		
		assertEquals(m.get(0).getBeats().get(0).getBeatDuration(), 1.5, FullTestSuite.DELTA);
		assertEquals(m.get(0).getBeats().get(1).getBeatDuration(), 1.5, FullTestSuite.DELTA);
		assertEquals(m.get(0).getBeats().get(2).getBeatDuration(), 1, FullTestSuite.DELTA);
	}
	
	/**
	 * Test a tie across a measure between beat 4 and beat 1
	 * @throws InvalidMidiDataException
	 * @throws IOException
	 */
	@Test
	public void testTieAcrossMeasureQuarter() throws InvalidMidiDataException, IOException
	{
		MIDIConverter c = new MIDIConverter("tests/test-input/tieAcrossMeasureQuarter.mid");
		
		c.convert();
		
		ArrayList<Measure> m = c.getConvertedMeasures();
		
		assertEquals(2, m.size());
		
		assertEquals(m.get(0).getBeats().get(0).getBeatDuration(), 1.0, FullTestSuite.DELTA);
		assertEquals(m.get(0).getBeats().get(1).getBeatDuration(), 1.0, FullTestSuite.DELTA);
		assertEquals(m.get(0).getBeats().get(2).getBeatDuration(), 1.0, FullTestSuite.DELTA);
		assertEquals(m.get(0).getBeats().get(3).getBeatDuration(), 1.0, FullTestSuite.DELTA);
		assertTrue(m.get(0).getBeats().get(3).isTiedForward());

		assertEquals(m.get(1).getBeats().get(0).getBeatDuration(), 1.0, FullTestSuite.DELTA);
		assertEquals(m.get(1).getBeats().get(1).getBeatDuration(), 1.0, FullTestSuite.DELTA);
		assertEquals(m.get(1).getBeats().get(2).getBeatDuration(), 1.0, FullTestSuite.DELTA);
		assertEquals(m.get(1).getBeats().get(3).getBeatDuration(), 1.0, FullTestSuite.DELTA);
	}

	/**
	 * Test a tie across a measure between beat 4 and beat 1/2
	 * @throws InvalidMidiDataException
	 * @throws IOException
	 */
	@Test
	public void testTieAcrossMeasureDottedHalf() throws InvalidMidiDataException, IOException
	{
		MIDIConverter c = new MIDIConverter("tests/test-input/tieAcrossMeasureDottedHalf.mid");
		
		c.convert();
		
		ArrayList<Measure> m = c.getConvertedMeasures();
		
		assertEquals(2, m.size());
		
		assertEquals(m.get(0).getBeats().get(0).getBeatDuration(), 1.0, FullTestSuite.DELTA);
		assertEquals(m.get(0).getBeats().get(1).getBeatDuration(), 1.0, FullTestSuite.DELTA);
		assertEquals(m.get(0).getBeats().get(2).getBeatDuration(), 1.0, FullTestSuite.DELTA);
		assertEquals(m.get(0).getBeats().get(3).getBeatDuration(), 1.0, FullTestSuite.DELTA);
		assertTrue(m.get(0).getBeats().get(3).isTiedForward());

		assertEquals(m.get(1).getBeats().get(0).getBeatDuration(), 2.0, FullTestSuite.DELTA);
		assertEquals(m.get(1).getBeats().get(1).getBeatDuration(), 1.0, FullTestSuite.DELTA);
		assertEquals(m.get(1).getBeats().get(2).getBeatDuration(), 1.0, FullTestSuite.DELTA);
	}
	
	/**
	 * Tie in a measure that should end up like
	 * 
	 * H-e e q
	 * @throws InvalidMidiDataException
	 * @throws IOException
	 */
	@Test
	public void testTieInMeasure() throws InvalidMidiDataException, IOException
	{
		MIDIConverter c = new MIDIConverter("tests/test-input/tieInMeasure.mid");
		
		c.convert();
		
		ArrayList<Measure> m = c.getConvertedMeasures();
		
		assertEquals(1, m.size());
		
		assertEquals(m.get(0).getBeats().get(0).getBeatDuration(), 2.0, FullTestSuite.DELTA);
		assertEquals(m.get(0).getBeats().get(1).getBeatDuration(), .5, FullTestSuite.DELTA);
		assertEquals(m.get(0).getBeats().get(2).getBeatDuration(), .5, FullTestSuite.DELTA);
		assertEquals(m.get(0).getBeats().get(3).getBeatDuration(), 1.0, FullTestSuite.DELTA);
	}
}
