package measure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import support.SeededRandom;

/**
 * A Measure contains an ArrayList of Beats and wraps logic around them
 * @author Steve
 *
 */
public class Measure implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4030771439144402073L;
	private ArrayList<Beat> beats;
	
	/**
	 * Create a blank measure
	 */
	public Measure()
	{
		beats = new ArrayList<Beat>();
	}
	
	/**
	 * Add a beat to the end of this measure
	 * @param beat The beat to add
	 */
	public void addBeat(Beat beat)
	{
		this.beats.add(beat);
	}
	
	/**
	 * Add a Beat after a position in this measure
	 * @param position The position to insert at
	 * @param beat The beat to add
	 */
	public void addBeatAfter(int position, Beat beat)
	{
		this.beats.add(position+1, beat);
	}
	
	/**
	 * 
	 * @return The beats of this Measure
	 */
	public ArrayList<Beat> getBeats()
	{
		return this.beats;
	}
	
	/**
	 * Removes a beat at a given index
	 * @param index Beat index to remove
	 */
	public void removeBeat(int index)
	{
		this.beats.remove(index);
	}
	
	public String toString()
	{
		String ret = "";
		for(Beat b: beats)
		{
			ret += b.toString() + " ";
		}
		return ret.trim();
	}
	
	/**
	 * A snycopated beat lies off beat and is not a rest
	 * @return The number of syncopated beats
	 */
	public int getSyncopatedBeatCount()
	{
		double currentBeat = 0.0;
		int count = 0;
		for(Beat beat: beats)
		{
			//check if the current beat is off beat (decimal is not empty)
			if(!beat.isRest() && currentBeat != Math.floor(currentBeat))
			{
				count++;
			}
			currentBeat += beat.getBeatDuration();
		}
		return count;
	}

	/**
	 * Swap two beats
	 * @param firstPosition The first index to swap
	 * @param secondPosition The second index to swap
	 */
	public void swapBeats(int firstPosition, int secondPosition) 
	{
		Beat temp = this.beats.get(firstPosition);
		this.beats.set(firstPosition, this.beats.get(secondPosition));
		this.beats.set(secondPosition, temp);
	}

	/**
	 * Reverse the beats in this measure
	 */
	public void reverseBeats() 
	{
		Collections.reverse(beats);
	}
	
	/**
	 * Reset this Measure to 4 MIDDLE_C quarter notes
	 * @param numBeats 
	 */
	public void reset(int numBeats)
	{
		this.beats.clear();
		for(int count = 0; count < numBeats; count++)
		{
			int noteValue = Beat.MIDDLE_C;
			int beatValue = 1;
			this.addBeat(new Beat(noteValue, beatValue));
		}
	}
	
	/**
	 * Randomize this measure to the same overall duration of beats, random notes, random reset
	 * @param isTonalRandomization Specifies whether this randomization can use rests and if all durations should be 1
	 * @throws Exception 
	 */
	public void randomize(boolean isTonalRandomization) throws Exception
	{
		double sum = 0.0;
		for(Beat b : beats)
			sum += b.getBeatDuration();
		
		ArrayList<Beat> newBeats = new ArrayList<Beat>();
		double newSum = 0.0;
		
		//keep going until the newSum == sum
		while(newSum < sum)
		{
			double nextDuration;
			if(isTonalRandomization)
			{
				nextDuration = 1.0;
			}
			else
			{
				int nextDurationIndex = (int)(SeededRandom.random() * Beat.POSSIBLE_DURATIONS.length);
				nextDuration = Beat.POSSIBLE_DURATIONS[nextDurationIndex];
			}
			//only use this sum if the beat doesn't go over the current measure duration
			if(nextDuration + newSum <= sum)
			{
				newSum += nextDuration;
				boolean isRest = SeededRandom.random() <= .5;
				//50% chance that this is a rest
				if(isRest && !isTonalRandomization)
				{
					Beat newBeat = new Beat(nextDuration);
					newBeats.add(newBeat);
				}
				else
				{
					//randomly pick a new note value
					int noteValue = (int)(SeededRandom.random() * Beat.MAX_NOTE_VALUE) + Beat.MIN_NOTE_VALUE;
					Beat newBeat = new Beat(noteValue, nextDuration);
					newBeats.add(newBeat);
				}
			}
		}
		//just a check to keep things from getting wonky if there was a bug of any type
		if(newSum != sum) throw new Exception("The randomized Measure has more duration than the starting Measure");
		this.beats = newBeats;
	}
}
