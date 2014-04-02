package genetic.mutations.measure;

import java.util.ArrayList;
import java.util.Arrays;

import support.SeededRandom;
import measure.Beat;
import measure.Measure;
import genetic.interfaces.MeasureMutation;

/**
 * Concrete Mutation which join a beat up to the minimum
 * @author Steve
 *
 */
public class JoinBeatMutation extends MeasureMutation
{
	/**
	 * 
	 * @param m The Measure to operate on
	 */
	public JoinBeatMutation(Measure m) 
	{
		super(m);
	}

	public void mutate() throws Exception 
	{
		ArrayList<Beat> beats = this.measure.getBeats();
		if(beats.size() == 0) throw new Exception("JoinBeatMutation given Measure with no Beats");
		
		//get a list of beats that can be joined, if any
		ArrayList<Integer> possibleIndexes = new ArrayList<Integer>();
		for(int i = 0; i < beats.size() - 1; i++)
		{
			Beat b = beats.get(i);
			double combinedDuration = b.getBeatDuration() + beats.get(i+1).getBeatDuration();
			if(Arrays.asList(Beat.SPLIT_BEATS).contains(combinedDuration))
			{
				possibleIndexes.add(i);
			}
			//if(b.getBeatDuration() < Beat.MAX_DURATION && b.getBeatDuration() == beats.get(i+1).getBeatDuration())
			//	possibleIndexes.add(i);
		}
		if(possibleIndexes.size() > 0)
		{
			//select a random position to operate on
			int beatIndexPosition = (int)(SeededRandom.random()*possibleIndexes.size());
			int beatPosition = possibleIndexes.get(beatIndexPosition);
		
			Beat leftBeat = beats.get(beatPosition);
			Beat rightBeat = beats.get(beatPosition + 1);
			leftBeat.setBeatDuration(leftBeat.getBeatDuration() + rightBeat.getBeatDuration());
			
			this.measure.removeBeat(beatPosition+1);
		}
	}

}
