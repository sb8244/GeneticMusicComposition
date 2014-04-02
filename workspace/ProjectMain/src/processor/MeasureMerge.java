package processor;

import measure.Beat;
import measure.Measure;

/**
 * Merges a tonal and rhythmic measure[] together to create a complete Measure[] with
 * 	combined tones and rhythms
 * @author Steve
 *
 */
public class MeasureMerge 
{
	private Measure[] rhythm;
	private Measure[] tones;
	
	/**
	 * 
	 * @param rhythmicMeasures The set of Measure corresponding to rhythms
	 * @param tonalMeasures The set of Measure corresponding to tones
	 */
	public MeasureMerge(Measure[] rhythmicMeasures, Measure[] tonalMeasures)
	{
		this.rhythm = rhythmicMeasures;
		this.tones = tonalMeasures;
		this.toneMeasureCount = 0;
		this.toneBeatCount = 0;
	}
	
	/**
	 * Perform the actual merge of the rhythms and tones
	 * @return A new Measure[] with the rhythms and tones of the input given
	 */
	public Measure[] merge()
	{
		Measure[] merged = new Measure[this.rhythm.length];
		
		for(int i = 0; i < this.rhythm.length; i++)
		{
			Measure rMeasure = this.rhythm[i];
			Measure newMeasure = new Measure();
			for(Beat rBeat: rMeasure.getBeats())
			{
				Beat newBeat = null;
				if(rBeat.isRest())
				{
					newBeat = new Beat(rBeat.getBeatDuration());
				}
				else
				{
					Beat toneBeat = this.getNextToneBeat();
					newBeat = new Beat(toneBeat.getNoteValue(), rBeat.getBeatDuration());
				}
				newMeasure.addBeat(newBeat);
			}
			merged[i] = newMeasure;
			//increment the tone measure count to keep things on a measure by measure basis
			this.incrementToneMeasureCount();
		}
		
		return merged;
	}
	
	private int toneMeasureCount;
	private int toneBeatCount;
	
	/**
	 * Get the next available tone; wrapping around to the beginning if the end
	 *  has been reached
	 * If the next tone beat is requested and the beat count would go outside of the number of beats,
	 *  the beat count wraps to 0 but stays in the current measure. This is to keep consistency in between
	 *  the measure and not have huge jumps
	 * @return The next available tone
	 */
	protected Beat getNextToneBeat()
	{
		if(this.toneBeatCount >= this.tones[this.toneMeasureCount].getBeats().size())
		{
			this.toneBeatCount = 0;
		}
		if(this.toneMeasureCount >= this.tones.length)
		{
			this.toneMeasureCount = 0;
			this.toneBeatCount = 0;
		}
		Beat nextBeat = this.tones[this.toneMeasureCount].getBeats().get(this.toneBeatCount);
		this.toneBeatCount++;
		return nextBeat;
	}
	
	protected void incrementToneMeasureCount()
	{
		this.toneMeasureCount++;
		this.toneBeatCount = 0;
		
		if(this.toneMeasureCount >= this.tones.length)
		{
			this.toneMeasureCount = 0;
		}
	}
}
