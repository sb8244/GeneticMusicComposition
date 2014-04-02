package measure;

import java.io.Serializable;

/**
 * Beat holds values for the pitch and duration of a note
 * @author Steve
 *
 */
public class Beat implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8762276693010569713L;

	/**
	 * Note names for pitches based on index position
	 */
	public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
	
	/**
	 * Only Beats with durations in this set can be processed
	 */
	public static final Double[] POSSIBLE_DURATIONS = {.25, .5, .75, 1.0, 1.5, 2.0, 3.0, 4.0};
	
	/**
	 * These beats are able to be split
	 */
	public static final Double[] SPLIT_BEATS = {.5, 1.0, 2.0, 3.0, 4.0};
	
	/**
	 * The count of possible durations * 2 (for rest and nonrest)
	 */
	public static final Double DURATION_COUNT = POSSIBLE_DURATIONS.length * 2.0;
	
	/**
	 * The maximum possible duration of a beat
	 */
	public static final Double MAX_DURATION = POSSIBLE_DURATIONS[POSSIBLE_DURATIONS.length-1];
	
	/**
	 * The minimum possible duration of a beat
	 */
	public static final Double MIN_DURATION = POSSIBLE_DURATIONS[0];
	
	/**
	 * The maximum MIDI value of a note
	 */
	public static final Integer MAX_NOTE_VALUE = 127;
	
	/**
	 * The minimum MIDI value of a note
	 */
	public static final Integer MIN_NOTE_VALUE = 0;

	/**
	 * A Middle C is defined as 60 in the MIDI specs
	 */
	public static final int MIDDLE_C = 60;

	/**
	 * The Maximum possible octave of notes
	 */
	public static final double MAX_OCTAVE = 10;
	
	private int noteValue;
	private double beatLength;
	private boolean isRest;
	private boolean tiedForward;
	
	/**
	 * Create a new rest beat
	 * @param beatLength The length of this beat
	 */
	public Beat(double beatLength)
	{
		this.setBeatDuration(beatLength);
		this.isRest = true;
		this.tiedForward = false;
	}
	
	/**
	 * Create a new note beat
	 * @param noteValue The MIDI value of this note
	 * @param beatLength The duration of this beat in POSSIBLE_DURATIONS
	 */
	public Beat(int noteValue, double beatLength)
	{
		this.isRest = false;
		this.noteValue = noteValue;
		this.tiedForward = false;
		this.setBeatDuration(beatLength);
	}
	
	/**
	 * 
	 * @return If this is a rest or not
	 */
	public boolean isRest()
	{
		return this.isRest;
	}
	
	/**
	 * 
	 * @return The MIDI note value
	 */
	public int getNoteValue()
	{
		return this.noteValue;
	}
	
	/**
	 * 
	 * @return The pitch of thie MIDI note
	 */
	public int getPitchValue()
	{
		return this.noteValue % 12;
	}
	
	/**
	 * 
	 * @return The octave this note belongs to
	 */
	public int getOctave()
	{
		return this.noteValue / 12;
	}
	
	/**
	 * 
	 * @return The duration of a beat
	 */
	public double getBeatDuration()
	{
		return this.beatLength;
	}
	
	/**
	 * @param duration The duration this Beat will now have
	 * @throws Exception 
	 */
	public void setBeatDuration(double duration)
	{
		if(!this.isPossibleDuration(duration))
			System.err.println("Beat Duration is not in the allowed set: " + duration);
		this.beatLength = duration;
	}
	
	private boolean isPossibleDuration(double duration)
	{
		for(Double d: POSSIBLE_DURATIONS)
		{
			if(d.equals(duration))
				return true;
		}
		return false;
	}
	
	/**
	 * Set the octave of this note to the given octave, maintaining pitch
	 * @param octave The octave to set to
	 */
	public void setOctave(int octave)
	{
		this.noteValue = this.getPitchValue() + (12 * octave);
	}
	
	/**
	 * Increment the pitch by 1
	 */
	public void incrementPitch()
	{
		this.noteValue = Math.min(MAX_NOTE_VALUE, this.noteValue+1);
	}
	
	/**
	 * Decrement the pitch by 1
	 */
	public void decrementPitch()
	{
		this.noteValue = Math.max(MIN_NOTE_VALUE, this.noteValue-1);
	}
	
	/**
	 * Set whether this note is tied forward or not
	 * @param val True / false
	 */
	public void setTiedForward(boolean val)
	{
		this.tiedForward = val;
	}
	
	/**
	 * 
	 * @return True if this note is tied forward
	 */
	public boolean isTiedForward()
	{
		return this.tiedForward;
	}
	
	public String toString()
	{
		String ret = (isRest()?"":NOTE_NAMES[noteValue % 12]+":") + beatLength + (isRest()?"r":"" + (isTiedForward()?"tied":""));
		return ret;
	}
}
