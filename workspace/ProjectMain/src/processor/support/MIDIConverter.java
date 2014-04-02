package processor.support;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import org.apache.commons.lang3.ArrayUtils;

import measure.Beat;
import measure.Measure;

/**
 * Converts a MIDI file to an array of Measure
 * 
 * @author Steve
 *
 */
public class MIDIConverter 
{
	private Sequence sequence;
	private double oneBeatDuration;
	private ArrayList<Measure> convertedMeasures;
	
	private int beatsPerMeasure;
	
	/**
	 * 
	 * @param fileName The MIDI file name
	 * @throws InvalidMidiDataException
	 * @throws IOException
	 */
	public MIDIConverter(String fileName) throws InvalidMidiDataException, IOException
	{
		this.sequence = MidiSystem.getSequence(new File(fileName));
		if(fileName.contains("jfugue"))
			this.oneBeatDuration = 30;
		else
			this.oneBeatDuration = sequence.getResolution();
		this.beatsPerMeasure = 4;
		this.convertedMeasures = new ArrayList<Measure>();
	}
	
	/**
	 * Perform conversion
	 * 
	 * Possible improvement could be to allow for simultaneous notes of different pitches by keeping track of
	 * 	when the last tick of a pitch was, instead of just the last tick
	 */
	public void convert()
	{
		for(int t = 0; t < sequence.getTracks().length; t++)
		{
			//get the track
			Track track = sequence.getTracks()[t];
			
			//lastActiveNoteTick will keep track of when the last note started; for determining the length of a note
			long lastActiveNoteTick = 0;
			//keeps track of when last note ended; this is used for calculating the time between a note and the last note (for rests)
			long lastOffNoteTick = 0;
			
			double currentMeasureBeatCount = 0;
			//Keep a Queue of beats that will be used to create the measures
			Queue<Beat> queuedBeats = new ArrayDeque<Beat>();
			Queue<Beat> nextMeasureHolder = new ArrayDeque<Beat>();
			for(int trackEventPosition = 0; trackEventPosition < track.size(); trackEventPosition++)
			{
				MidiEvent event = track.get(trackEventPosition);
				MidiMessage message = event.getMessage();
				
				if(message instanceof ShortMessage)
				{
					ShortMessageParser smp = new ShortMessageParser((ShortMessage)message);
					
					//Was this message actually processed?
					boolean isProcessedMessage = smp.isOnMessage() || smp.isOffMessage();
					if(smp.isOnMessage())
					{
						long ticksBetweenLastAndCurrent = event.getTick() - lastOffNoteTick;
						//If the note are immediately proceeding the last, then there was a rest
						if(ticksBetweenLastAndCurrent > 0)
						{
							double restDuration = (ticksBetweenLastAndCurrent / oneBeatDuration);
							currentMeasureBeatCount += restDuration;
							if(currentMeasureBeatCount > this.beatsPerMeasure)
							{
								//create and add the beat to a placeholder queue
								double tiedBeatDuration = currentMeasureBeatCount - this.beatsPerMeasure;
								nextMeasureHolder.addAll(this.generateRestBeats(tiedBeatDuration));
								
								//subtract the tied beat duration to compensate
								restDuration -= tiedBeatDuration;
								currentMeasureBeatCount -= tiedBeatDuration;
							}
							queuedBeats.addAll(this.generateRestBeats(restDuration));
						}
						lastActiveNoteTick = event.getTick();
					}
					else if(smp.isOffMessage())
					{
						//The time between this tick and the last active note is this notes length
						long lastBeatTickDuration = event.getTick() - lastActiveNoteTick;
						double beatDuration = (lastBeatTickDuration / oneBeatDuration);
						currentMeasureBeatCount += beatDuration;
						int lastPitch = smp.getLastPitch();
						
						/*
						 * Now we have to do a check in case beatCount > beatsPerMeasure 
						 * This indicates that a note was tied across measures and needs to be accounted for
						 * 
						 * Also log if this note should be tied forward. Backward ties are assumed from a tie forward
						 */
						boolean tiedForward = false;
						if(currentMeasureBeatCount > this.beatsPerMeasure)
						{
							//create and add the beat to a placeholder queue
							double tiedBeatDuration = currentMeasureBeatCount - this.beatsPerMeasure;
							Beat tiedBeat = new Beat(lastPitch, tiedBeatDuration);
							nextMeasureHolder.add(tiedBeat);
							
							//subtract the tied beat duration to compensate
							beatDuration -= tiedBeatDuration;
							currentMeasureBeatCount -= tiedBeatDuration;
							
							tiedForward = true;
						}
						
						ArrayList<Beat> newBeats = this.generateBeatBeats(lastPitch, beatDuration);
						Beat firstBeat = newBeats.get(0);
						firstBeat.setTiedForward( tiedForward || firstBeat.isTiedForward() );
						queuedBeats.addAll(newBeats);
						
						lastOffNoteTick = event.getTick();
					}
					
					//If this message was processed and finished populating an entire measure, then add beats to a measure
					if(isProcessedMessage && currentMeasureBeatCount >= this.beatsPerMeasure && queuedBeats.size() > 0)
					{
						Measure measure = new Measure();
						while(queuedBeats.size() > 0)
						{
							Beat nextBeat = queuedBeats.poll();
							
							measure.addBeat(nextBeat);
						}
						currentMeasureBeatCount = 0;
						this.convertedMeasures.add(measure);
						
						//Add nextMeasureHolder and clear it out
						while(nextMeasureHolder.size() > 0)
						{
							Beat nextBeat = nextMeasureHolder.poll();
							currentMeasureBeatCount += nextBeat.getBeatDuration();
							queuedBeats.add(nextBeat);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param pitch The pitch of the note to generate
	 * @param duration The duration to generate from
	 * @return A list of Beats that accounts for splitting ties
	 */
	private ArrayList<Beat> generateBeatBeats(int pitch, double duration)
	{
		ArrayList<Beat> beats = new ArrayList<Beat>(2);
		ArrayList<Double> durations = this.splitUpDuration(duration);
		for(Double d: durations)
		{
			Beat b = new Beat(pitch, d);
			beats.add(b);
		}
		if(beats.size() > 1)
			beats.get(0).setTiedForward(true);
		return beats;
	}
	
	/**
	 * 
	 * @param duration The duration to generate from
	 * @return A list of Rest Beats that accounts for splitting ties
	 */
	private ArrayList<Beat> generateRestBeats(double duration)
	{
		ArrayList<Beat> beats = new ArrayList<Beat>(2);
		ArrayList<Double> durations = this.splitUpDuration(duration);
		for(Double d: durations)
		{
			Beat b = new Beat(d);
			beats.add(b);
		}
		if(beats.size() > 1)
			beats.get(0).setTiedForward(true);
		return beats;
	}
	
	/**
	 * Splits a duration into possible beat durations based on known combinations
	 * 
	 * TODO: Extend this as needed for beat durations which aren't accounted for
	 * 
	 * @param duration The input duration
	 * @return An ArrayList<Double> containing the applicable durations that can be used
	 */
	private ArrayList<Double> splitUpDuration(double duration)
	{
		ArrayList<Double> durations = new ArrayList<Double>();
		
		if(ArrayUtils.contains(Beat.POSSIBLE_DURATIONS, duration))
		{
			durations.add(duration);
		}
		else if(duration == 2.5)
		{
			durations.add(2.0);
			durations.add(.5);
		}
		else if(duration == 3.5)
		{
			durations.add(3.0);
			durations.add(.5);
		}
		else if(duration == 4.5)
		{
			durations.add(4.0);
			durations.add(.5);
		}
		
		return durations;
	}
	
	/**
	 * 
	 * @return The converted measures
	 */
	public ArrayList<Measure> getConvertedMeasures()
	{
		return this.convertedMeasures;
	}

	/**
	 * Convert and then repeat the measures for a given number of times
	 * @param repeatMeasures The number of times to repeat
	 */
	public void convert(int repeatMeasures) 
	{
		this.convert();
		int startLength = this.convertedMeasures.size();
		for(int i = 0; i < repeatMeasures; i++)
		{
			for(int j = 0; j < startLength; j++)
			{
				this.convertedMeasures.add(this.convertedMeasures.get(j));
			}
		}
	}
}
