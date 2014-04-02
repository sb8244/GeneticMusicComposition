package genetic.interfaces;

import java.util.Arrays;

import processor.AnalysisVector;
import support.SeededRandom;
import measure.Measure;

/**
 * Abstract Population to be used for Measure[] populations
 * @author Steve
 *
 */
public abstract class MusicPopulation extends Population<Measure[]>
{

	//Operating Point Pilots
	protected static double GOAL_ERROR = 0.05;
	protected static double MUTATE_PER_CHROMOSOME = .1;
	
	protected int numberOfMeasures;
	protected int populationSize;
	protected AnalysisVector goalVector;
	protected int numBeats;
	
	/**
	 * Set some population properties
	 * @param populationSize
	 * @param numberOfMeasures
	 * @param goalVector
	 */
	public MusicPopulation(int populationSize, int numberOfMeasures, AnalysisVector goalVector, int numBeats)
	{
		this.populationSize = populationSize;
		this.numberOfMeasures = numberOfMeasures;
		this.goalVector = goalVector;
		this.numBeats = numBeats;
	}
	
	public Chromosome<Measure[]> evaluate() throws Exception 
	{
		for(Chromosome<Measure[]> ch: population)
		{
			if(ch.getFitness() < GOAL_ERROR)
			{
				return ch;
			}
		}
		return null;
	}

	public void mutate() throws Exception
	{
		for(Chromosome<Measure[]> ch: population)
		{
			if(SeededRandom.random() <= MUTATE_PER_CHROMOSOME)
			{
				ch.mutate();
			}
		}
	}
	
	public void crossover()
	{
		MusicChromosome[] newPopulation = new MusicChromosome[this.population.length];
		int filled = 0;
		while(filled < newPopulation.length)
		{
			MusicChromosome[] tournament = new MusicChromosome[6];
			for(int i = 0; i < tournament.length; i++)
			{
				int index = (int)(SeededRandom.random() * this.population.length);
				tournament[i] = (MusicChromosome) this.population[index];
			}
			Arrays.sort(tournament);
			MusicChromosome one = tournament[0];
			MusicChromosome two = tournament[1];
			MusicChromosome child = one.crossover(two);

			newPopulation[filled++] = child;
		}
		this.population = newPopulation;
	}
}
