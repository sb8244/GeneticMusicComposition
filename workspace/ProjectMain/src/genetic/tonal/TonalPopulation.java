package genetic.tonal;

import measure.Measure;
import processor.AnalysisVector;
import genetic.interfaces.Chromosome;
import genetic.interfaces.MusicChromosome;
import genetic.interfaces.MusicPopulation;

/**
 * Concrete Population to be used with Tonal GA
 * @author Steve
 *
 */
public class TonalPopulation extends MusicPopulation
{
	private int desiredRange;

	/**
	 * 
	 * @param populationSize The fixed size of the population
	 * @param numberOfMeasures The fixed number of measures in each Chromosome
	 * @param goalVector The Rhythm AnalysisVector to check fitness against
	 * @param desiredRange The desired range to be used in fitness calculation
	 */
	public TonalPopulation(int populationSize, int numberOfMeasures, AnalysisVector goalVector, int desiredRange, int numBeats)
	{
		super(populationSize, numberOfMeasures, goalVector, numBeats);
		this.desiredRange = desiredRange;
		GOAL_ERROR = Math.sqrt(goalVector.size()) * .1;
		this.reset();
	}
	
	public void reset()
	{
		population = new MusicChromosome[populationSize];
		for(int i = 0; i < populationSize; i++)
		{
			population[i] = new TonalChromosome(numberOfMeasures, goalVector, desiredRange, this.numBeats);
		}
	}
	
	/**
	 * Perform a memetic fix on the entire population
	 */
	public void memeticFix()
	{
		for(Chromosome<Measure[]> chr: population)
		{
			TonalChromosome tc = (TonalChromosome) chr;
			tc.memeticFix();
		}
	}
}
