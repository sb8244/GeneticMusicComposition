package genetic.interfaces;

import org.apache.commons.lang3.SerializationUtils;

import processor.AnalysisVector;
import support.SeededRandom;
import measure.Measure;

/**
 * Implementation of Chromosome for Music pieces
 * @author Steve
 *
 */
public abstract class MusicChromosome extends Chromosome<Measure[]> implements Comparable<MusicChromosome>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8543598011690440591L;
	private static final double RANDOM_CHANCE = 1;
	private boolean canHaveRests;

	protected int numMeasures;
	protected AnalysisVector goalVector;
	protected int numBeats;
	
	/**
	 * Initialize a new MusicChromosome with a given number of measures
	 * @param numMeasures The fixed number of measures this Chromosome has
	 * @param goalVector AnalysisVector to check fitness against
	 * @param canHaveRests Can randomization product rests in this Chromosome
	 */
	public MusicChromosome(int numMeasures, AnalysisVector goalVector, boolean canHaveRests, int numBeats)
	{
		this.canHaveRests = canHaveRests;
		this.numMeasures = numMeasures;
		this.goalVector = goalVector;
		this.numBeats = numBeats;
		
		//reset / instantiate the measures
		this.reset();
		
		//fetch the most recent data placed by this.reset()
		Measure[] measures = this.getData();
		for(Measure m: measures)
			try {
				m.randomize(canHaveRests);
			} catch (Exception e) {
				e.printStackTrace();
			}
		this.setData(measures);
	}
	
	/**
	 * Reset the Measures to 4 beats each with a note value of MIDDLE_C a beat value of 1
	 */
	public void reset()
	{
		Measure[] measures = new Measure[numMeasures];
		for(int i = 0; i < numMeasures; i++)
		{
			measures[i] = new Measure();
			measures[i].reset(this.numBeats);
		}
		this.setData(measures);
	}
	
	public int compareTo(MusicChromosome other) {
		try {
			Double fitness = this.getFitness();
			Double otherFitness = other.getFitness();
			return fitness.compareTo(otherFitness);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * Crossover this Chromosome with another by:
	 * 	Picking a random crossover point to be used at the Measure[] level
	 * 	Take the first x measures from this Chromosome
	 * 	Take the last y measures from the other Chromosome
	 * @param other
	 * @return A deep-copy new Chromosome using the crossover method
	 */
	public MusicChromosome crossover(MusicChromosome other)
	{
		MusicChromosome child = null;
		//deep clone this in order to get a copy
		child = SerializationUtils.clone(this);
		
		int randomCrossoverPoint = (int)(SeededRandom.random() * this.getData().length);
		//anything < crossover point will stay, anything >= will become other's data
		
		Measure[] data = SerializationUtils.clone(this.getData());
		for(int i = randomCrossoverPoint; i < this.getData().length; i++)
		{
			data[i] = SerializationUtils.clone(other.getData()[i]);
		}
		child.setData(data);
		return child;
	}
	
	public void introduceRandomness()
	{
		Measure[] measures = this.getData();
		for(Measure m: measures)
		{
			try {
				if(SeededRandom.random() <= RANDOM_CHANCE)
					m.randomize(canHaveRests);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.setData(measures);
	}
	
	/**
	 * 
	 * @return The AnalysisVector that is used in figuring out the fitness
	 */
	public abstract AnalysisVector getVector();
}
