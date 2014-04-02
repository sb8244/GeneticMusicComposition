package genetic.tonal;

import processor.AnalysisVector;
import genetic.interfaces.GeneticAlgorithm;
import measure.Measure;

/**
 * Concrete GA for Tones
 * @author Steve
 *
 */
public class TonalGeneticAlgorithm extends GeneticAlgorithm<Measure[]>
{
	/**
	 * Create a new Genetic Algorithm instance
	 * @param popSize The fixed population size
	 * @param numberOfMeasures The number of measures in each RhythmChromosome
	 * @param goalVector The goal Rhythm AnalysisVector to fitness against
	 * @param desiredRange The desired range given by the max tonal range in the input music
	 */
	public TonalGeneticAlgorithm(int popSize, int numberOfMeasures, AnalysisVector goalVector, int desiredRange, int numBeatsPerMeasure)
	{
		this.population = new TonalPopulation(popSize, numberOfMeasures, goalVector, desiredRange, numBeatsPerMeasure);
	}
	
	protected void evolutionHook()
	{
		//have to cast this in order to get access to memeticFix
		TonalPopulation castedPopulation = (TonalPopulation) this.population;
		castedPopulation.memeticFix();
	}
}
