package processor.support;

import java.util.HashMap;

import measure.Beat;
import measure.Measure;

/**
 * Converter for Measure[] to MIDI
 * @author Steve
 *
 */
public class JFugueConverter
{
	private Measure[] measures;
	private HashMap<Double, String> durationMap;
	/**
	 * 
	 * @param measures The Measure[] to convert
	 */
	public JFugueConverter(Measure[] measures)
	{
		this.measures = measures;
		this.durationMap = new HashMap<Double, String>();
		this.durationMap.put(4.0, "w");
		this.durationMap.put(3.0, "h.");
		this.durationMap.put(2.0, "h");
		this.durationMap.put(1.5, "q.");
		this.durationMap.put(1.0, "q");
		this.durationMap.put(.75, "i.");
		this.durationMap.put(.5, "i");
		this.durationMap.put(.25, "s");
		
		assert(this.durationMap.size() == Beat.POSSIBLE_DURATIONS.length);
	}
	
	/**
	 * Perform conversion to a JFugue String
	 * @return The JFugue playback string represented by these Measure[]
	 */
	public String convertToString()
	{
		String output = "";
		Beat previousBeat = null;
		for(Measure m: measures)
		{
			for(Beat b: m.getBeats())
			{
				String duration = this.durationMap.get(b.getBeatDuration());
				String note = "[" + b.getNoteValue() + "]";
				if(b.isRest())
					note = "R";
				
				if(previousBeat != null && previousBeat.isTiedForward())
				{
					output += "-";
					note = "[" + previousBeat.getNoteValue() + "]";
				}
				
				output += note + duration;
				
				if(b.isTiedForward())
					output += "-";
				
				output += " ";
				previousBeat = b;
			}
			output += "| ";
		}
		return output;
	}
}
