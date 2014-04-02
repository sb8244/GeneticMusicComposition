package genetic.rhythm;

import processor.AnalysisVector;
import genetic.interfaces.MusicChromosome;
import genetic.interfaces.MusicPopulation;

/**
 * Rhythm implementation of a Population of RhythmChromosome
 * @author Steve
 *
 */
public class RhythmPopulation extends MusicPopulation
{
	/**
	 * 
	 * @param populationSize The fixed size of the population
	 * @param numberOfMeasures The fixed number of measures in each Chromosome
	 * @param goalVector The Rhythm AnalysisVector to check fitness against
	 */
	public RhythmPopulation(int populationSize, int numberOfMeasures, AnalysisVector goalVector, int numBeats)
	{
		super(populationSize, numberOfMeasures, goalVector, numBeats);
		
		GOAL_ERROR = Math.sqrt(goalVector.size()) * .1;
		this.reset();
	}

	public void reset() 
	{
		population = new MusicChromosome[populationSize];
		for(int i = 0; i < population.length; i++)
		{
			population[i] = new RhythmChromosome(numberOfMeasures, goalVector, numBeats);
		}
	}
}
