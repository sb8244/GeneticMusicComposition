package genetic.mutations.measure;

import java.util.ArrayList;

import measure.Beat;
import measure.Measure;
import genetic.interfaces.MeasureMutation;

/**
 * Measure mutation which sets the last beat to (un)tied at a 50% chance each
 * @author Steve
 *
 */
public class AddTieMutation  extends MeasureMutation
{

	/**
	 * 
	 * @param m The Measure to operate on
	 */
	public AddTieMutation(Measure m)
	{
		super(m);
	}

	public void mutate() 
	{
		ArrayList<Beat> beats = this.measure.getBeats();
		
		if(Math.random() < .5)
			beats.get(beats.size()-1).setTiedForward(true);
		else
			beats.get(beats.size()-1).setTiedForward(false);
	}

}
