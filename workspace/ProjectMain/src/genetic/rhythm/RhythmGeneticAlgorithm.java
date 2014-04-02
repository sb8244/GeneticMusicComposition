package genetic.rhythm;

import processor.AnalysisVector;
import measure.Measure;
import genetic.interfaces.GeneticAlgorithm;

/**
 * Concrete Genetic Algorithm for RhythmChromosomes
 * @author Steve
 *
 */
public class RhythmGeneticAlgorithm extends GeneticAlgorithm<Measure[]>
{
	/**
	 * Create a new Genetic Algorith instance
	 * @param popSize The fixed population size
	 * @param numberOfMeasures The number of measures in each RhythmChromosome
	 * @param goalVector The goal Rhythm AnalysisVector to fitness against
	 */
	public RhythmGeneticAlgorithm(int popSize, int numberOfMeasures, AnalysisVector goalVector)
	{
		this.population = new RhythmPopulation(popSize, numberOfMeasures, goalVector, 4);
	}
}
