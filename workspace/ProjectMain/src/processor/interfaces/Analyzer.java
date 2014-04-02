package processor.interfaces;

import processor.exceptions.BeatDurationOutOfBoundsException;

/**
 * Expose a simple analyzer interface
 * @author Steve
 *
 */
public interface Analyzer 
{

	/**
	 * Analyze this set of measures and populate rhythmic AnalysisVector
	 * @throws BeatDurationOutOfBoundsException Exception if a Beat duration is not in available set
	 */
	public void analyzeRhythm() throws BeatDurationOutOfBoundsException;
	
	/**
	 * Analyze this set of measures and populate tonal AnalysisVector
	 * @param desiredRange The desired range of pitches is a dependency. If null, the "range" metric will always be 1.0
	 */
	public void analyzeTones(Integer desiredRange);
}
