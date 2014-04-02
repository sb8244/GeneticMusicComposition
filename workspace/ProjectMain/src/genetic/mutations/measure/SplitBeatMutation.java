package genetic.mutations.measure;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.SerializationUtils;

import support.SeededRandom;
import measure.Beat;
import measure.Measure;
import genetic.interfaces.MeasureMutation;

/**
 * Concrete Mutation which splits a beat down to the minimum
 * @author Steve
 *
 */
public class SplitBeatMutation extends MeasureMutation
{
	/**
	 * 
	 * @param m The Measure to operate on
	 */
	public SplitBeatMutation(Measure m) 
	{
		super(m);
	}

	public void mutate() throws Exception 
	{
		ArrayList<Beat> beats = this.measure.getBeats();
		if(beats.size() == 0) throw new Exception("SplitBeatMutation given Measure with no Beats");
		
		//check that a split can actually be performed
		boolean canSplit = false;
		for(Beat b: beats)
		{
			if(Arrays.asList(Beat.SPLIT_BEATS).contains(b.getBeatDuration()))
				canSplit = true;
		}
		if(canSplit)
		{
			//select a random position to operate on
			int beatPosition = (int)(SeededRandom.random()*beats.size());
			//select a beatPosition until a beat can be operated on
			while(!Arrays.asList(Beat.SPLIT_BEATS).contains(beats.get(beatPosition).getBeatDuration()))
			{
				beatPosition = (int)(SeededRandom.random()*beats.size());
			}
			
			Beat operateBeat = beats.get(beatPosition);
			operateBeat.setBeatDuration(operateBeat.getBeatDuration() / 2);
			//deep-clone using deep-clone on SerializationUtils instead of ObjectUtils
			//because ObjectUtils requires Cloneable which is complex to implement
			Beat newBeat = SerializationUtils.clone(operateBeat);
			
			this.measure.addBeatAfter(beatPosition, newBeat);
		}
	}

}
