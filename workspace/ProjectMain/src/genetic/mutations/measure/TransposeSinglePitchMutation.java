package genetic.mutations.measure;

import support.SeededRandom;
import measure.Beat;
import measure.Measure;
import genetic.interfaces.MeasureMutation;

/**
 * Transposes a single pitch up or down randomly
 * @author Steve
 *
 */
public class TransposeSinglePitchMutation extends MeasureMutation
{
	/**
	 * 
	 * @param m Measure to operate on
	 */
	public TransposeSinglePitchMutation(Measure m) 
	{
		super(m);
	}

	public void mutate() throws Exception 
	{
		int randomPosition = (int)(SeededRandom.random() * this.measure.getBeats().size());
		Beat beat = this.measure.getBeats().get(randomPosition);
		//increment the pitch at 50% chance
		if(SeededRandom.random() <= .5)
		{
			beat.incrementPitch();
		}
		//decrement the pitch at 50% chance
		else
		{
			beat.decrementPitch();
		}
	}

}
