package processor.support;

import static org.junit.Assert.*;
import measure.Beat;
import measure.Measure;

import org.junit.Test;

/**
 * Test Suite for JFugueConverter
 * @author Steve
 *
 */
public class JFugueConverterTest
{
	/**
	 * Simple test of convertToString with a single measure / beat
	 */
	@Test
	public void simpleStringConvert()
	{
		Measure[] ms = new Measure[1];
		ms[0] = new Measure();
		ms[0].addBeat(new Beat(60, 1));
		
		JFugueConverter c = new JFugueConverter(ms);
		String s = c.convertToString();
		
		assertEquals("[60]q | ", s);
	}
	
	/**
	 * Test convertToString with a full Measure with every duration, and some rests
	 */
	@Test
	public void simpleMeasureConvert()
	{
		Measure[] ms = new Measure[1];
		ms[0] = new Measure();
		ms[0].addBeat(new Beat(60, 1));
		ms[0].addBeat(new Beat(.25));
		ms[0].addBeat(new Beat(.25));
		ms[0].addBeat(new Beat(70, .5));
		ms[0].addBeat(new Beat(71, 2));
		ms[0].addBeat(new Beat(72, 4));
		
		JFugueConverter c = new JFugueConverter(ms);
		String s = c.convertToString();
		
		assertEquals("[60]q Rs Rs [70]i [71]h [72]w | ", s);
	}
	
	/**
	 * Multiple measure convertToString
	 */
	@Test
	public void multipleMeasureConvert()
	{
		Measure[] ms = new Measure[2];
		ms[0] = new Measure();
		ms[0].addBeat(new Beat(60, 1));
		ms[0].addBeat(new Beat(.25));
		ms[0].addBeat(new Beat(.25));
		ms[0].addBeat(new Beat(70, .5));
		ms[0].addBeat(new Beat(71, 2));
		ms[0].addBeat(new Beat(72, 4));
		
		ms[1] = new Measure();
		ms[1].addBeat(new Beat(60, 2));
		
		JFugueConverter c = new JFugueConverter(ms);
		String s = c.convertToString();
		
		assertEquals("[60]q Rs Rs [70]i [71]h [72]w | [60]h | ", s);
	}
	
	/**
	 * Test a beat tied across a measure
	 */
	@Test
	public void tiedBeat()
	{
		Measure[] ms = new Measure[2];
		ms[0] = new Measure();
		ms[0].addBeat(new Beat(60, 1));
		ms[0].getBeats().get(0).setTiedForward(true);
		
		ms[1] = new Measure();
		ms[1].addBeat(new Beat(70, 1));
		ms[1].addBeat(new Beat(75, 1));
		
		JFugueConverter c = new JFugueConverter(ms);
		String s = c.convertToString();
		
		assertEquals("[60]q- | -[60]q [75]q | ", s);
	}
}
