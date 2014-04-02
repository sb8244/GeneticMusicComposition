package processor;

import java.util.ArrayList;

import processor.exceptions.BeatDurationOutOfBoundsException;
import processor.interfaces.Analyzer;
import measure.Beat;
import measure.Measure;

/**
 * Analyzes a set of measures to populate Music Analysis Vectors
 * @author Steve
 *
 */
public class MusicAnalyzer implements Analyzer
{
	private Measure[] measures;
	private AnalysisVector rhythmicAnalysisVector;
	private AnalysisVector pitchAnalysisVector;
	private static double[] DISSONANCE_INTERVAL_VALUES = {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, .5, 1, 0};
	
	private Integer computedRange = null;
	private Integer averageTonesPerMeasure;
	
	/**
	 * Create a new MusicAnalyzer
	 * @param measures The ArrayList of Measure to analyze
	 */
	public MusicAnalyzer(ArrayList<Measure> measures)
	{
		this.measures = new Measure[measures.size()];
		for(int i = 0; i < measures.size(); i++)
			this.measures[i] = measures.get(i);
	}
	
	/**
	 * Create a new MusicAnalyzer
	 * @param measures The array of Measure to analyze
	 */
	public MusicAnalyzer(Measure[] measures)
	{
		this.measures = measures;
	}
	
	public void analyzeTones(Integer desiredRange)
	{
		int[] pitchCount = new int[12];
		//keep track of various counts along the way
		double noteCount = 0;
		double intervalCount = 0;
		double diatonicLeapCount = 0;
		double largeLeapCount = 0;
		double repeatedPitchCount = 0;
		double dissonanceIntervalsSum = 0;
		double runningOctaveSum = 0;
		//min amd max note placeholders
		int highestNote = 0;
		int lowestNote = 127;
		//Put all beats into a single array in order to avoid cross measure interval problems
		ArrayList<Beat> allBeats = new ArrayList<Beat>();
		for(Measure measure: measures)
		{
			allBeats.addAll(measure.getBeats());
		}
		for(int i = 0; i < allBeats.size(); i++)
		{
			//process an individual beat
			Beat beat = allBeats.get(i);
			if(!beat.isRest())
			{
				runningOctaveSum += beat.getOctave();
				pitchCount[beat.getPitchValue()]++;	
				noteCount++;
				highestNote = Math.max(highestNote, beat.getNoteValue());
				lowestNote = Math.min(lowestNote, beat.getNoteValue());
				//and also the next beat interval if applicable 
				if(i != allBeats.size() - 1)
				{
					Beat nextBeat = allBeats.get(i+1);
					if(!nextBeat.isRest())
					{
						intervalCount++;
						int intervalLeap = Math.abs(beat.getNoteValue() - nextBeat.getNoteValue());
						if(intervalLeap >= DISSONANCE_INTERVAL_VALUES.length)
							dissonanceIntervalsSum += 1.0;
						else
							dissonanceIntervalsSum += DISSONANCE_INTERVAL_VALUES[intervalLeap];
						if(intervalLeap == 0 || intervalLeap == 1)
							diatonicLeapCount++;
						else if(intervalLeap >= 6)
							largeLeapCount++;
						if(beat.getNoteValue() == nextBeat.getNoteValue())
							repeatedPitchCount++;
					}
				}
			}
		}
		
		int distinctPitchCount = 0;
		for(Integer i: pitchCount)
		{
			if(i > 0)
			{
				distinctPitchCount++;
			}
		}
		
		double variety = 0;
		if(noteCount > 0)
			variety = distinctPitchCount / noteCount;
		if(desiredRange == null)
		{
			desiredRange = Math.max(1, highestNote - lowestNote);
			this.computedRange = desiredRange;
		}
		intervalCount = Math.max(1, intervalCount);
		this.averageTonesPerMeasure = (int) (noteCount / measures.length);
		this.pitchAnalysisVector = new AnalysisVector();
		this.pitchAnalysisVector.put("variety", variety);
		this.pitchAnalysisVector.put("range", (highestNote - lowestNote) / (double)desiredRange);
		this.pitchAnalysisVector.put("dissonantIntervals", dissonanceIntervalsSum / intervalCount);
		this.pitchAnalysisVector.put("diatonicSteps", diatonicLeapCount / intervalCount);
		this.pitchAnalysisVector.put("largeSteps", largeLeapCount / intervalCount);
		this.pitchAnalysisVector.put("repeatedPitches", repeatedPitchCount / intervalCount);
		this.pitchAnalysisVector.put("averageOctave", runningOctaveSum / noteCount / Beat.MAX_OCTAVE);
	}
	
	/**
	 * 
	 * @return The range of the input if the desiredRange was null
	 */
	public Integer getComputedRange()
	{
		return this.computedRange;
	}
	
	public Integer getAverageTonesPerMeasure()
	{
		return averageTonesPerMeasure;
	}
	
	public void analyzeRhythm() throws BeatDurationOutOfBoundsException
	{
		//RhythmicVarietyCount is an array of booleans which will be true
		//when a distinct beat length is hit
		int[] beatLengthCount = new int[Beat.POSSIBLE_DURATIONS.length*2];
		int beatCount = 0;
		int restCount = 0;
		int syncopationCount = 0;
		int tieCount = 0;
		double longestRestBeatDuration = 0.0;
		double shortestRestBeatDuration = Beat.MAX_DURATION;
		double longestNoteBeatDuration = 0.0;
		double shortestNoteBeatDuration = Beat.MAX_DURATION;
		double beatDurationSum = 0;
		
		for(Measure measure: measures)
		{
			syncopationCount += measure.getSyncopatedBeatCount();
			for(Beat beat: measure.getBeats())
			{
				int mappedRhythmicVarietyIndex = this.getMappedRhythmicVarietyIndex(beat);
				beatLengthCount[mappedRhythmicVarietyIndex]++;
				if(beat.isRest())
				{
					restCount++;
					longestRestBeatDuration = Math.max(longestRestBeatDuration, beat.getBeatDuration());
					shortestRestBeatDuration = Math.min(shortestRestBeatDuration, beat.getBeatDuration());
				}
				else
				{
					longestNoteBeatDuration = Math.max(longestNoteBeatDuration, beat.getBeatDuration());
					shortestNoteBeatDuration = Math.min(shortestNoteBeatDuration, beat.getBeatDuration());
				}
				beatDurationSum += beat.getBeatDuration();
				
				if(beat.isTiedForward())
				{
					tieCount++;
				}
				beatCount++;
				
			}
		}
		
		int distinctBeatLengths = 0;
		for(Integer i: beatLengthCount)
		{
			if(i > 0)
			{
				distinctBeatLengths++;
			}
		}
		
		this.rhythmicAnalysisVector = new AnalysisVector();
		this.rhythmicAnalysisVector.put("rhythmicVariety", distinctBeatLengths / Beat.DURATION_COUNT);
		this.rhythmicAnalysisVector.put("restDensity", restCount / (double)beatCount);
		double maxRestRange = Beat.MAX_DURATION / Beat.MIN_DURATION;
		this.rhythmicAnalysisVector.put("restRange", (longestRestBeatDuration / shortestRestBeatDuration) / maxRestRange);
		this.rhythmicAnalysisVector.put("noteRange", (longestNoteBeatDuration / shortestNoteBeatDuration) / maxRestRange);
		this.rhythmicAnalysisVector.put("syncopationCount", syncopationCount  / (double)beatCount);
		this.rhythmicAnalysisVector.put("averageBeatDuration", beatDurationSum / (double)beatCount);
		this.rhythmicAnalysisVector.put("tieCount", tieCount / (double)beatCount);
	}
	
	/**
	 * 
	 * @return The rhythmic AnalysisVector calculated by analyze
	 */
	public AnalysisVector getRhythmicAnalysisVector()
	{
		return this.rhythmicAnalysisVector;
	}
	
	/**
	 * 
	 * @return The pitch AnalysisVector calculated by analyze
	 */
	public AnalysisVector getPitchAnalysisVector()
	{
		return this.pitchAnalysisVector;
	}
	
	/**
	 * Maps the possible beat durations + rests into an index that can
	 * 	be used to count how many types of each beat are present
	 * @param beat Beat
	 * @return An index containing the proper mapping [0-9]
	 * @throws BeatDurationOutOfBoundsException
	 */
	private int getMappedRhythmicVarietyIndex(Beat beat) throws BeatDurationOutOfBoundsException
	{
		double beatLength = beat.getBeatDuration();
		int index = 0;
		for(index = 0; index < Beat.POSSIBLE_DURATIONS.length; index++) {
			if(beatLength == Beat.POSSIBLE_DURATIONS[index])
				break;
		}
		if(index == Beat.POSSIBLE_DURATIONS.length)
			throw new BeatDurationOutOfBoundsException("Rhythm out of bounds");
		
		if(beat.isRest()) 
		{
			//add possible duration length to the index to create the top half of the array
			index += Beat.POSSIBLE_DURATIONS.length;
		}
		return index;
	}
}
