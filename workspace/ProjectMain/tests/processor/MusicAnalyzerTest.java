package processor;

import static org.junit.Assert.*;

import java.util.ArrayList;

import measure.Beat;
import measure.Measure;

import org.junit.Test;

import processor.exceptions.BeatDurationOutOfBoundsException;
import suite.FullTestSuite;
/**
 * Test suite for MusicAnalyzer
 * @author Steve
 *
 */
public class MusicAnalyzerTest 
{
	private static double TOLERANCE = .000000005;
	
	/**
	 * Test rhythmic variety with only 1 quarter note
	 * @throws BeatDurationOutOfBoundsException
	 */
	@Test
	public void rhythmicVarietyAllQuarter() throws BeatDurationOutOfBoundsException
	{
		ArrayList<Measure> measures = new ArrayList<Measure>();
		Measure m = new Measure();
		Beat quarter = new Beat(0, 1);
		m.addBeat(quarter);
		m.addBeat(quarter);
		m.addBeat(quarter);
		m.addBeat(quarter);
		measures.add(m);
		
		MusicAnalyzer ma = new MusicAnalyzer(measures);
		ma.analyzeRhythm();
		
		AnalysisVector rhythmAnalysisVector = ma.getRhythmicAnalysisVector();
		
		assertEquals(1/Beat.DURATION_COUNT, rhythmAnalysisVector.get("rhythmicVariety").doubleValue(), TOLERANCE);
	}
	
	/**
	 * Test rythmic variety with 1 quarter note & rested quarter
	 * @throws BeatDurationOutOfBoundsException
	 */
	@Test
	public void rhythmicVarietyQuarterWithRest() throws BeatDurationOutOfBoundsException
	{
		ArrayList<Measure> measures = new ArrayList<Measure>();
		Measure m = new Measure();
		Beat quarter = new Beat(0, 1);
		Beat restQuarter = new Beat(1);
		m.addBeat(quarter);
		m.addBeat(quarter);
		m.addBeat(quarter);
		m.addBeat(restQuarter);
		measures.add(m);
		
		MusicAnalyzer ma = new MusicAnalyzer(measures);
		ma.analyzeRhythm();
		
		AnalysisVector rhythmAnalysisVector = ma.getRhythmicAnalysisVector();
		
		assertEquals(2/Beat.DURATION_COUNT, rhythmAnalysisVector.get("rhythmicVariety").doubleValue(), TOLERANCE);
	}
	
	/**
	 * Test every possible beat duration and rest with multiple measures
	 * @throws BeatDurationOutOfBoundsException
	 */
	@Test
	public void rhythmicVarietyAllPossible() throws BeatDurationOutOfBoundsException
	{
		ArrayList<Measure> measures = new ArrayList<Measure>();
		
		for(Double d: Beat.POSSIBLE_DURATIONS)
		{
			Beat beat = new Beat(0, d);
			Beat rest = new Beat(d);
			Measure m = new Measure();
			m.addBeat(beat);
			m.addBeat(rest);
			measures.add(m);
		}
		
		MusicAnalyzer ma = new MusicAnalyzer(measures);
		ma.analyzeRhythm();
		
		AnalysisVector rhythmAnalysisVector = ma.getRhythmicAnalysisVector();
		
		assertEquals(1.0, rhythmAnalysisVector.get("rhythmicVariety").doubleValue(), TOLERANCE);
	}
	
	/**
	 * Test when a beat with incorrect duration is used
	 * @throws BeatDurationOutOfBoundsException is expected
	 */
	@Test(expected = BeatDurationOutOfBoundsException.class)
	public void analyzeWithOutOfRangeBeatDuration() throws BeatDurationOutOfBoundsException
	{
		ArrayList<Measure> measures = new ArrayList<Measure>();
		
		for(Double d: Beat.POSSIBLE_DURATIONS)
		{
			Beat beat = new Beat(0, d);
			Beat rest = new Beat(d);
			Measure m = new Measure();
			m.addBeat(beat);
			m.addBeat(rest);
			measures.add(m);
		}
		Measure junk = new Measure();
		junk.addBeat(new Beat(-1));
		measures.add(junk);
		System.err.println("I expect to see a beat duration error message in this test. It should be -1.0");
		MusicAnalyzer ma = new MusicAnalyzer(measures);
		ma.analyzeRhythm();
	}
	
	/**
	 * Test Rest Density attribute with no rests
	 * @throws BeatDurationOutOfBoundsException
	 */
	@Test
	public void restDensityNoRest() throws BeatDurationOutOfBoundsException
	{
		ArrayList<Measure> measures = new ArrayList<Measure>();
		Measure m = new Measure();
		Beat quarter = new Beat(0, 1);
		m.addBeat(quarter);
		m.addBeat(quarter);
		
		measures.add(m);
		
		MusicAnalyzer ma = new MusicAnalyzer(measures);
		ma.analyzeRhythm();
		
		AnalysisVector rhythmAnalysisVector = ma.getRhythmicAnalysisVector();
		assertEquals(0, rhythmAnalysisVector.get("restDensity").doubleValue(), TOLERANCE);
	}
	
	/**
	 * Test Rest Density attribute with all rests
	 * @throws BeatDurationOutOfBoundsException
	 */
	@Test
	public void restDensityAllRest() throws BeatDurationOutOfBoundsException
	{
		ArrayList<Measure> measures = new ArrayList<Measure>();
		Measure m = new Measure();
		Beat quarterRest = new Beat(1);
		m.addBeat(quarterRest);
		m.addBeat(quarterRest);
		
		measures.add(m);
		
		MusicAnalyzer ma = new MusicAnalyzer(measures);
		ma.analyzeRhythm();
		
		AnalysisVector rhythmAnalysisVector = ma.getRhythmicAnalysisVector();
		assertEquals(1, rhythmAnalysisVector.get("restDensity").doubleValue(), TOLERANCE);
	}
	
	/**
	 * Test Rest Density attribute with half rests
	 * @throws BeatDurationOutOfBoundsException
	 */
	@Test
	public void restDensityHalfRest() throws BeatDurationOutOfBoundsException
	{
		ArrayList<Measure> measures = new ArrayList<Measure>();
		Measure m = new Measure();
		Beat quarter = new Beat(0, 1);
		Beat quarterRest = new Beat(1);
		m.addBeat(quarter);
		m.addBeat(quarterRest);
		m.addBeat(quarter);
		m.addBeat(quarterRest);
		
		measures.add(m);
		
		MusicAnalyzer ma = new MusicAnalyzer(measures);
		ma.analyzeRhythm();
		
		AnalysisVector rhythmAnalysisVector = ma.getRhythmicAnalysisVector();
		assertEquals(.5, rhythmAnalysisVector.get("restDensity").doubleValue(), TOLERANCE);
	}
	
	/**
	 * Test restRange with no rests ( always 0 )
	 * @throws BeatDurationOutOfBoundsException
	 */
	@Test
	public void restRangeNoRest() throws BeatDurationOutOfBoundsException
	{
		ArrayList<Measure> measures = new ArrayList<Measure>();
		Measure m = new Measure();
		Beat quarter = new Beat(0, 1);
		m.addBeat(quarter);
		m.addBeat(quarter);
		
		measures.add(m);
		
		MusicAnalyzer ma = new MusicAnalyzer(measures);
		ma.analyzeRhythm();
		
		AnalysisVector rhythmAnalysisVector = ma.getRhythmicAnalysisVector();
		assertEquals(0, rhythmAnalysisVector.get("restRange").doubleValue(), TOLERANCE);
	}
	
	/**
	 * Test noteRange with no notes (always 0)
	 * @throws BeatDurationOutOfBoundsException
	 */
	@Test
	public void noteRangeNoNote() throws BeatDurationOutOfBoundsException
	{
		ArrayList<Measure> measures = new ArrayList<Measure>();
		Measure m = new Measure();
		Beat quarter = new Beat(1);
		m.addBeat(quarter);
		m.addBeat(quarter);
		
		measures.add(m);
		
		MusicAnalyzer ma = new MusicAnalyzer(measures);
		ma.analyzeRhythm();
		
		AnalysisVector rhythmAnalysisVector = ma.getRhythmicAnalysisVector();
		assertEquals(0, rhythmAnalysisVector.get("noteRange").doubleValue(), TOLERANCE);
	}
	
	/**
	 * Test restRange with 1 whole rest (4 / 4 / 16)
	 * @throws BeatDurationOutOfBoundsException
	 */
	@Test
	public void restRangeWholeRest() throws BeatDurationOutOfBoundsException
	{
		ArrayList<Measure> measures = new ArrayList<Measure>();
		Measure m = new Measure();
		Beat quarter = new Beat(4);
		m.addBeat(quarter);
		
		measures.add(m);
		
		MusicAnalyzer ma = new MusicAnalyzer(measures);
		ma.analyzeRhythm();
		
		AnalysisVector rhythmAnalysisVector = ma.getRhythmicAnalysisVector();
		assertEquals(4.0/4.0/16.0, rhythmAnalysisVector.get("restRange").doubleValue(), TOLERANCE);
	}
	
	/**
	 * Test note range with whole note (4/4/16)
	 * @throws BeatDurationOutOfBoundsException
	 */
	@Test
	public void noteRangeWholeNote() throws BeatDurationOutOfBoundsException
	{
		ArrayList<Measure> measures = new ArrayList<Measure>();
		Measure m = new Measure();
		Beat quarter = new Beat(0, 4);
		m.addBeat(quarter);
		
		measures.add(m);
		
		MusicAnalyzer ma = new MusicAnalyzer(measures);
		ma.analyzeRhythm();
		
		AnalysisVector rhythmAnalysisVector = ma.getRhythmicAnalysisVector();
		assertEquals(4.0/4.0/16.0, rhythmAnalysisVector.get("noteRange").doubleValue(), TOLERANCE);
	}
	
	/**
	 * Test rest range with a range ( 4/2/16)
	 * @throws BeatDurationOutOfBoundsException
	 */
	@Test
	public void restRangeVaryRest() throws BeatDurationOutOfBoundsException
	{
		ArrayList<Measure> measures = new ArrayList<Measure>();
		Measure m = new Measure();
		Beat half = new Beat(2);
		Beat whole = new Beat(4);
		m.addBeat(half);
		m.addBeat(whole);
		
		measures.add(m);
		
		MusicAnalyzer ma = new MusicAnalyzer(measures);
		ma.analyzeRhythm();
		
		AnalysisVector rhythmAnalysisVector = ma.getRhythmicAnalysisVector();
		assertEquals(4.0/2.0/16.0, rhythmAnalysisVector.get("restRange").doubleValue(), TOLERANCE);
	}
	
	/**
	 * Test note range with a range ( 4/2/16)
	 * @throws BeatDurationOutOfBoundsException
	 */
	@Test
	public void noteRangeVaryNoteWithRest() throws BeatDurationOutOfBoundsException
	{
		ArrayList<Measure> measures = new ArrayList<Measure>();
		Measure m = new Measure();
		Beat half = new Beat(0, 2);
		Beat whole = new Beat(0, 4);
		Beat wholeR = new Beat(4);
		m.addBeat(half);
		m.addBeat(whole);
		m.addBeat(wholeR);
		
		measures.add(m);
		
		MusicAnalyzer ma = new MusicAnalyzer(measures);
		ma.analyzeRhythm();
		
		AnalysisVector rhythmAnalysisVector = ma.getRhythmicAnalysisVector();
		assertEquals(4.0/2.0/16.0, rhythmAnalysisVector.get("noteRange").doubleValue(), TOLERANCE);
	}
	
	/**
	 * Test syncopationCoutn when there is no syncopation
	 * @throws BeatDurationOutOfBoundsException
	 */
	@Test
	public void syncopationCountNoSync() throws BeatDurationOutOfBoundsException
	{
		ArrayList<Measure> measures = new ArrayList<Measure>();
		Measure m = new Measure();
		m.addBeat(new Beat(1));
		m.addBeat(new Beat(1));
		m.addBeat(new Beat(2));
		
		measures.add(m);
		
		MusicAnalyzer ma = new MusicAnalyzer(measures);
		ma.analyzeRhythm();
		
		AnalysisVector rhythmAnalysisVector = ma.getRhythmicAnalysisVector();
		assertEquals(0, rhythmAnalysisVector.get("syncopationCount").doubleValue(), TOLERANCE);
	}
	
	/**
	 * Basic syncopationCount test where all beats after eighth note are syncopated
	 * @throws BeatDurationOutOfBoundsException
	 */
	@Test
	public void syncopationCountWithSync() throws BeatDurationOutOfBoundsException
	{
		ArrayList<Measure> measures = new ArrayList<Measure>();
		Measure m = new Measure();
		m.addBeat(new Beat(.5));
		m.addBeat(new Beat(0, 1));
		m.addBeat(new Beat(0, 1));
		m.addBeat(new Beat(0, 2));
		
		measures.add(m);
		
		MusicAnalyzer ma = new MusicAnalyzer(measures);
		ma.analyzeRhythm();
		
		AnalysisVector rhythmAnalysisVector = ma.getRhythmicAnalysisVector();
		assertEquals(3/4.0, rhythmAnalysisVector.get("syncopationCount").doubleValue(), TOLERANCE);
	}
	
	/**
	 * Test more complex syncopationCount (see comments for which notes are syncopated)
	 * @throws BeatDurationOutOfBoundsException
	 */
	@Test
	public void syncopationCountWithSyncComplex() throws BeatDurationOutOfBoundsException
	{
		ArrayList<Measure> measures = new ArrayList<Measure>();
		Measure m = new Measure();
		m.addBeat(new Beat(.5)); // not
		m.addBeat(new Beat(0, 1)); // yes
		m.addBeat(new Beat(1)); // not
		m.addBeat(new Beat(0, 2)); // yes
		Measure m2 = new Measure();
		m2.addBeat(new Beat(0, 1)); // not
		m2.addBeat(new Beat(.5)); //not
		m2.addBeat(new Beat(0, .5)); //yes
		m2.addBeat(new Beat(0, 2)); //not
		
		measures.add(m);
		measures.add(m2);
		
		MusicAnalyzer ma = new MusicAnalyzer(measures);
		ma.analyzeRhythm();
		
		AnalysisVector rhythmAnalysisVector = ma.getRhythmicAnalysisVector();
		assertEquals(3/8.0, rhythmAnalysisVector.get("syncopationCount").doubleValue(), TOLERANCE);
	}
	
	/**
	 * Simple pitchVariety where there is 1 pitch and 1 note
	 */
	@Test
	public void pitchVarietySimple()
	{
		ArrayList<Measure> measures = new ArrayList<Measure>();
		Measure m = new Measure();
		m.addBeat(new Beat(0, 1));
		measures.add(m);
		
		MusicAnalyzer ma = new MusicAnalyzer(measures);
		ma.analyzeTones(null);
		
		AnalysisVector pitchAnalysisVector = ma.getPitchAnalysisVector();
		assertEquals(1.0, pitchAnalysisVector.get("variety").doubleValue(), TOLERANCE);
	}
	
	/**
	 * pitchVariety with 2 pitches and 2 notes
	 */
	@Test
	public void pitchVarietySimple2()
	{
		ArrayList<Measure> measures = new ArrayList<Measure>();
		Measure m = new Measure();
		m.addBeat(new Beat(0, 1));
		m.addBeat(new Beat(1, 1));
		measures.add(m);
		
		MusicAnalyzer ma = new MusicAnalyzer(measures);
		ma.analyzeTones(null);
		
		AnalysisVector pitchAnalysisVector = ma.getPitchAnalysisVector();
		assertEquals(1.0, pitchAnalysisVector.get("variety").doubleValue(), TOLERANCE);
	}
	
	/**
	 * pitchVariety with 1 pitch, 4 notes
	 */
	@Test
	public void pitchVarietyAllSame()
	{
		ArrayList<Measure> measures = new ArrayList<Measure>();
		Measure m = new Measure();
		m.addBeat(new Beat(0, 1));
		m.addBeat(new Beat(0, 1));
		m.addBeat(new Beat(0, 1));
		m.addBeat(new Beat(0, 1));
		measures.add(m);
		
		MusicAnalyzer ma = new MusicAnalyzer(measures);
		ma.analyzeTones(null);
		
		AnalysisVector pitchAnalysisVector = ma.getPitchAnalysisVector();
		assertEquals(1/4.0, pitchAnalysisVector.get("variety").doubleValue(), TOLERANCE);
	}
	
	/**
	 * pitchVariety all rests 
	 */
	@Test
	public void pitchVarietyAllRest()
	{
		ArrayList<Measure> measures = new ArrayList<Measure>();
		Measure m = new Measure();
		m.addBeat(new Beat(1));
		m.addBeat(new Beat(1));
		m.addBeat(new Beat(1));
		m.addBeat(new Beat(1));
		measures.add(m);
		
		MusicAnalyzer ma = new MusicAnalyzer(measures);
		ma.analyzeTones(null);
		
		AnalysisVector pitchAnalysisVector = ma.getPitchAnalysisVector();
		assertEquals(0, pitchAnalysisVector.get("variety").doubleValue(), TOLERANCE);
	}
	
	/**
	 * pitchVariety where an octave is used (1 pitch 2 notes)
	 */
	@Test
	public void pitchVarietyOctave()
	{
		ArrayList<Measure> measures = new ArrayList<Measure>();
		Measure m = new Measure();
		m.addBeat(new Beat(1));
		m.addBeat(new Beat(0, 1));
		m.addBeat(new Beat(12, 1));
		measures.add(m);
		
		MusicAnalyzer ma = new MusicAnalyzer(measures);
		ma.analyzeTones(null);
		
		AnalysisVector pitchAnalysisVector = ma.getPitchAnalysisVector();
		assertEquals(1/2.0, pitchAnalysisVector.get("variety").doubleValue(), TOLERANCE);
	}
	
	/**
	 * pitchVariety where a complex measure pattern is used and notated throughout
	 */
	@Test
	public void pitchVarietyComplex()
	{
		ArrayList<Measure> measures = new ArrayList<Measure>();
		Measure m = new Measure();
		m.addBeat(new Beat(1));
		m.addBeat(new Beat(0, 1)); // 1-1
		m.addBeat(new Beat(12, 1)); // 1-2
		m.addBeat(new Beat(1, 1)); // 2-3
		measures.add(m);
		Measure m2 = new Measure();
		m.addBeat(new Beat(13, 2)); // 2 - 4
		m.addBeat(new Beat(14, 2)); // 3 - 5
		measures.add(m2);
		MusicAnalyzer ma = new MusicAnalyzer(measures);
		ma.analyzeTones(null);
		
		AnalysisVector pitchAnalysisVector = ma.getPitchAnalysisVector();
		assertEquals(3/5.0, pitchAnalysisVector.get("variety").doubleValue(), TOLERANCE);
	}
	
	/**
	 * When a null dependency is passed to analyzing the tone, the range should be 1
	 */
	@Test
	public void pitchRangeNullDependency()
	{
		ArrayList<Measure> measures = new ArrayList<Measure>();
		Measure m = new Measure();
		m.addBeat(new Beat(1));
		m.addBeat(new Beat(0, 1));
		m.addBeat(new Beat(12, 1));
		measures.add(m);
		
		MusicAnalyzer ma = new MusicAnalyzer(measures);
		ma.analyzeTones(null);
		
		AnalysisVector pitchAnalysisVector = ma.getPitchAnalysisVector();
		assertEquals(1.0, pitchAnalysisVector.get("range").doubleValue(), TOLERANCE);
	}
	
	/**
	 * Simple pitchRange test where an octave range is seen with 2 octave desired
	 */
	@Test
	public void pitchRangeSimple()
	{
		ArrayList<Measure> measures = new ArrayList<Measure>();
		Measure m = new Measure();
		m.addBeat(new Beat(1));
		m.addBeat(new Beat(0, 1));
		m.addBeat(new Beat(12, 1));
		measures.add(m);
		
		MusicAnalyzer ma = new MusicAnalyzer(measures);
		ma.analyzeTones(24); // 2 octave desired
		
		AnalysisVector pitchAnalysisVector = ma.getPitchAnalysisVector();
		assertEquals(.5, pitchAnalysisVector.get("range").doubleValue(), TOLERANCE);
	}
	
	/**
	 * Simple pitchRange test where a 1 note range is seen
	 */
	@Test
	public void pitchRangeSimple2()
	{
		ArrayList<Measure> measures = new ArrayList<Measure>();
		Measure m = new Measure();
		m.addBeat(new Beat(1));
		m.addBeat(new Beat(0, 1));
		m.addBeat(new Beat(1, 1));
		measures.add(m);
		
		MusicAnalyzer ma = new MusicAnalyzer(measures);
		ma.analyzeTones(24); // 2 octave desired
		
		AnalysisVector pitchAnalysisVector = ma.getPitchAnalysisVector();
		assertEquals((1-0)/24.0, pitchAnalysisVector.get("range").doubleValue(), TOLERANCE);
	}
	
	/**
	 * Complex pitch range seen from 1-120 with a desired range of 24
	 */
	@Test
	public void pitchRangeComplex()
	{
		ArrayList<Measure> measures = new ArrayList<Measure>();
		Measure m = new Measure();
		m.addBeat(new Beat(1));
		m.addBeat(new Beat(5, 1)); 
		m.addBeat(new Beat(12, 1)); 
		m.addBeat(new Beat(1, 1)); 
		measures.add(m);
		Measure m2 = new Measure();
		m.addBeat(new Beat(13, 2));
		m.addBeat(new Beat(120, 2));
		measures.add(m2);
		
		MusicAnalyzer ma = new MusicAnalyzer(measures);
		ma.analyzeTones(24); // 2 octave desired
		
		AnalysisVector pitchAnalysisVector = ma.getPitchAnalysisVector();
		assertEquals((120-1)/24.0, pitchAnalysisVector.get("range").doubleValue(), TOLERANCE);
	}
	
	/**
	 * Test all 0 value dissonance steps
	 */
	@Test
	public void dissonantInterval0RatingSteps()
	{
		ArrayList<Measure> measures = new ArrayList<Measure>();
		Measure m = new Measure();
		m.addBeat(new Beat(0, 1));
		m.addBeat(new Beat(0, 1)); // 0
		m.addBeat(new Beat(1, 1)); // 1
		m.addBeat(new Beat(3, 1)); // 2
		m.addBeat(new Beat(6, 1)); // 3
		m.addBeat(new Beat(8, 1)); // 4
		m.addBeat(new Beat(13, 1)); // 5
		m.addBeat(new Beat(1)); // rest
		m.addBeat(new Beat(19, 1)); 
		m.addBeat(new Beat(26, 1));  // 7
		m.addBeat(new Beat(34, 1)); // 8
		m.addBeat(new Beat(43, 1)); // 9
		m.addBeat(new Beat(55, 1)); // 12
		measures.add(m);
		
		MusicAnalyzer ma = new MusicAnalyzer(measures);
		ma.analyzeTones(null); // 2 octave desired
		
		AnalysisVector pitchAnalysisVector = ma.getPitchAnalysisVector();
		assertEquals(0, pitchAnalysisVector.get("dissonantIntervals").doubleValue(), TOLERANCE);
	}
	
	/**
	 * Test all .5 value dissonance steps
	 */
	@Test
	public void dissonantIntervalPoint5RatingSteps()
	{
		ArrayList<Measure> measures = new ArrayList<Measure>();
		Measure m = new Measure();
		m.addBeat(new Beat(0, 1));
		m.addBeat(new Beat(0+10, 1)); // 10
		measures.add(m);
		
		MusicAnalyzer ma = new MusicAnalyzer(measures);
		ma.analyzeTones(null); // 2 octave desired
		
		AnalysisVector pitchAnalysisVector = ma.getPitchAnalysisVector();
		assertEquals(.5 / 1, pitchAnalysisVector.get("dissonantIntervals").doubleValue(), TOLERANCE);
	}
	
	/**
	 * Test all 1 value dissonance steps + some >= 13
	 */
	@Test
	public void dissonantInterval1RatingSteps()
	{
		ArrayList<Measure> measures = new ArrayList<Measure>();
		Measure m = new Measure();
		m.addBeat(new Beat(0, 1));
		m.addBeat(new Beat(6, 1)); // 6
		m.addBeat(new Beat(17, 1)); // 11
		m.addBeat(new Beat(30, 1)); // 13
		m.addBeat(new Beat(44, 1)); // 14
		measures.add(m);
		
		MusicAnalyzer ma = new MusicAnalyzer(measures);
		ma.analyzeTones(null); // 2 octave desired
		
		AnalysisVector pitchAnalysisVector = ma.getPitchAnalysisVector();
		assertEquals(4 / 4.0, pitchAnalysisVector.get("dissonantIntervals").doubleValue(), TOLERANCE);
	}
	
	/**
	 * Complex, across measure dissonance
	 */
	@Test
	public void dissonantIntervalComplex()
	{
		ArrayList<Measure> measures = new ArrayList<Measure>();
		Measure m = new Measure();
		m.addBeat(new Beat(0, 1));
		m.addBeat(new Beat(6, 1)); // 6 - 1
		m.addBeat(new Beat(4, 1)); // -2 - 0
		m.addBeat(new Beat(5, 1)); // 1 - 0
		measures.add(m);
		Measure m2 = new Measure();
		m2.addBeat(new Beat(15, 1)); // 10 - .5
		m2.addBeat(new Beat(60, 1)); // 45 - 1
		m2.addBeat(new Beat(48, 1)); // -12 - 0
		m2.addBeat(new Beat(48, 1)); // 0 - 0
		m2.addBeat(new Beat(38, 4)); // -10 - .5
		measures.add(m2);
		
		MusicAnalyzer ma = new MusicAnalyzer(measures);
		ma.analyzeTones(null); // 2 octave desired
		
		AnalysisVector pitchAnalysisVector = ma.getPitchAnalysisVector();
		assertEquals(3.0 / 8, pitchAnalysisVector.get("dissonantIntervals").doubleValue(), TOLERANCE);
	}
	
	/**
	 * Test several diatonic step intervals (1 or 2 note step)
	 */
	@Test
	public void diatonicStepSimpleAcrossMeasures()
	{
		ArrayList<Measure> measures = new ArrayList<Measure>();
		Measure m = new Measure();
		Measure m2 = new Measure();
		m.addBeat(new Beat(0, 1));
		m.addBeat(new Beat(0, 1)); // 0
		m.addBeat(new Beat(1, 1)); // 1
		m2.addBeat(new Beat(2, 1)); // 1
		m2.addBeat(new Beat(1, 1)); // 1
		m2.addBeat(new Beat(3, 1)); // 1
		m2.addBeat(new Beat(6, 1)); // 0
		measures.add(m);
		measures.add(m2);
		
		MusicAnalyzer ma = new MusicAnalyzer(measures);
		ma.analyzeTones(null); // 2 octave desired
		
		AnalysisVector pitchAnalysisVector = ma.getPitchAnalysisVector();
		assertEquals(4 / 6.0, pitchAnalysisVector.get("diatonicSteps").doubleValue(), TOLERANCE);
	}
	
	/**
	 * Test several large step intervals (1 or 2 note step)
	 */
	@Test
	public void largeStepSimpleAcrossMeasures()
	{
		ArrayList<Measure> measures = new ArrayList<Measure>();
		Measure m = new Measure();
		Measure m2 = new Measure();
		m.addBeat(new Beat(0, 1));
		m.addBeat(new Beat(0, 1)); // 0
		m.addBeat(new Beat(1, 1)); // 0
		m2.addBeat(new Beat(2, 1)); // 0
		m2.addBeat(new Beat(1, 1)); // 0
		m2.addBeat(new Beat(3, 1)); // 0
		m2.addBeat(new Beat(6, 1)); // 0
		m2.addBeat(new Beat(12, 1)); // 1
		m2.addBeat(new Beat(20, 1)); // 1
		m2.addBeat(new Beat(12, 1)); // 1
		measures.add(m);
		measures.add(m2);
		
		MusicAnalyzer ma = new MusicAnalyzer(measures);
		ma.analyzeTones(null); // 2 octave desired
		
		AnalysisVector pitchAnalysisVector = ma.getPitchAnalysisVector();
		assertEquals(3 / 9.0, pitchAnalysisVector.get("largeSteps").doubleValue(), TOLERANCE);
	}
	
	/**
	 * Test several large step intervals (1 or 2 note step)
	 */
	@Test
	public void repeatedNotesSimpleAcrossMeasures()
	{
		ArrayList<Measure> measures = new ArrayList<Measure>();
		Measure m = new Measure();
		Measure m2 = new Measure();
		m.addBeat(new Beat(0, 1));
		m.addBeat(new Beat(0, 1)); // 1
		m.addBeat(new Beat(1, 1)); // 0
		m2.addBeat(new Beat(1, 1)); // 1
		m2.addBeat(new Beat(1, 1)); // 1
		m2.addBeat(new Beat(3, 1)); // 0
		measures.add(m);
		measures.add(m2);
		
		MusicAnalyzer ma = new MusicAnalyzer(measures);
		ma.analyzeTones(null); // 2 octave desired
		
		AnalysisVector pitchAnalysisVector = ma.getPitchAnalysisVector();
		assertEquals(3 / 5.0, pitchAnalysisVector.get("repeatedPitches").doubleValue(), TOLERANCE);
	}
	
	/**
	 * Test tieCount with no ties
	 * @throws BeatDurationOutOfBoundsException
	 */
	@Test
	public void noTies() throws BeatDurationOutOfBoundsException
	{
		ArrayList<Measure> measures = new ArrayList<Measure>();
		Measure m = new Measure();
		m.addBeat(new Beat(1));
		m.addBeat(new Beat(1));
		measures.add(m);
		
		MusicAnalyzer ma = new MusicAnalyzer(measures);
		ma.analyzeRhythm();
		
		AnalysisVector av = ma.getRhythmicAnalysisVector();
		assertEquals(0, av.get("tieCount").doubleValue(), FullTestSuite.DELTA);
	}
	
	/**
	 * Test single tieCount with 1 measure
	 * @throws BeatDurationOutOfBoundsException
	 */
	@Test
	public void simpleTie() throws BeatDurationOutOfBoundsException
	{
		ArrayList<Measure> measures = new ArrayList<Measure>();
		Measure m = new Measure();
		Beat b = new Beat(1);
		b.setTiedForward(true);;
		m.addBeat(new Beat(1));
		m.addBeat(b);
		measures.add(m);
		
		MusicAnalyzer ma = new MusicAnalyzer(measures);
		ma.analyzeRhythm();
		
		AnalysisVector av = ma.getRhythmicAnalysisVector();
		assertEquals(1/2.0, av.get("tieCount").doubleValue(), FullTestSuite.DELTA);
	}
	
	/**
	 * Test multiple tieCounts in more than 1 measure
	 * @throws BeatDurationOutOfBoundsException
	 */
	@Test
	public void complexTie() throws BeatDurationOutOfBoundsException
	{
		ArrayList<Measure> measures = new ArrayList<Measure>();
		Measure m = new Measure();
		Beat b = new Beat(1);
		Beat other = new Beat(1);
		other.setTiedForward(true);
		b.setTiedForward(true);
		m.addBeat(new Beat(1));
		m.addBeat(b);
		measures.add(m);
		Measure m2 = new Measure();
		m2.addBeat(other);
		measures.add(m2);
		
		MusicAnalyzer ma = new MusicAnalyzer(measures);
		ma.analyzeRhythm();
		
		AnalysisVector av = ma.getRhythmicAnalysisVector();
		assertEquals(2/3.0, av.get("tieCount").doubleValue(), FullTestSuite.DELTA);
	}
}
