package genetic.interfaces;

import support.SeededRandom;


/**
 * Genetic Algorithm Implementation
 * 
 * @author Steve
 * @param <T> The type of data this genetic algorithm holds in its Chromosomes
 *
 */
public abstract class GeneticAlgorithm<T>
{
	protected Population<T> population;
	private static int SAME_COUNT_BEFORE_RANDOMIZE = 50;
	private static int ITERATIONS_AFTER_RESULT = 15;
	private static int SAME_COUNT_RANGE_BEFORE_RANDOMIZE = 200;
	private static int MAX_ITERATIONS_BEFORE_KILL = 2000;
	
	/**
	 * Execute the genetic algorithm
	 * @return The fit Chromosome winner
	 * @throws Exception 
	 */
	public final Chromosome<T> execute() throws Exception
	{
		if(population == null) throw new Exception("Population is null.");
		
		int noChangeCount = 0;
		int iterationCountAfterFit = 0;
		int noChangeLargerScopeCount = 0;
		int iterationCount = 0;
		double lastChange = 0.0;
		//Run the initial evaluation
		Chromosome<T> fitReturn = population.evaluate();
		Chromosome<T> lowestChromosome = null;
		//While the fitReturn hasn't been provided, perform genetic operators
		while(fitReturn == null)
		{
			if(iterationCount >= MAX_ITERATIONS_BEFORE_KILL && iterationCountAfterFit == 0)
			{
				break;
			}
			
			this.evolveOnce();
			//evaluate
			
			Chromosome<T> possibleNewLowest = population.evaluate();
			if(lowestChromosome == null)
			{
				lowestChromosome = population.evaluate();
			}
			else if(possibleNewLowest != null)
			{
				if(possibleNewLowest.getFitness() < lowestChromosome.getFitness())
				{
					lowestChromosome = possibleNewLowest;
				}
			}
			
			Chromosome<T> lowest = population.getLowestFitnessChromosome();
			
			double lowestFitness = lowest.getFitness();

			//Keep track of slow fitness changes
			
			if(Math.abs(lowestFitness - lastChange) <= .5)
				noChangeLargerScopeCount++;
			else
				noChangeLargerScopeCount = 0;
			
			if(Math.abs(lowestFitness - lastChange) <= .01)
			{
				noChangeCount++;
			}
			else
			{
				lastChange = lowestFitness;
				noChangeCount = 0;
			}
			
			
			//introduce randomness after so many slow fitness changes
			if(noChangeCount >= SAME_COUNT_BEFORE_RANDOMIZE)
			{
				SeededRandom.incrementSeed();
				population.reset();
				noChangeCount = 0;
			}
			if(noChangeLargerScopeCount >= SAME_COUNT_RANGE_BEFORE_RANDOMIZE)
			{
				SeededRandom.incrementSeed();
				population.reset();
				noChangeLargerScopeCount = 0;
			}
			
			if(lowestChromosome != null)
			{
				iterationCountAfterFit++;
				if(iterationCountAfterFit >= ITERATIONS_AFTER_RESULT)
				{
					fitReturn = lowestChromosome;
				}
			}
			
			System.out.println(lowestFitness + " - " + population.getAverageFitness() +
					" - " + noChangeCount + 
					" - " + noChangeLargerScopeCount + 
					" - " + iterationCountAfterFit);
			iterationCount++;
		}
		
		//we have a winner
		return fitReturn;
	}
	
	/**
	 * Perform 1 genetic evolution (crossover & mutate)
	 * @throws Exception
	 */
	public final void evolveOnce() throws Exception
	{
		if(population == null) throw new Exception("Population is null.");
		//crossover
		population.crossover();
		//mutate
		population.mutate();
		
		this.evolutionHook();
	}
	
	/**
	 * 
	 * @return The Chromosome<T> with the highest fitness
	 * @throws Exception 
	 */
	public final Chromosome<T> getHighestFitnessChromosome() throws Exception
	{
		return population.getHighestFitnessChromosome();
	}
	
	/**
	 * 
	 * @return The Chromosome<T> with the lowest fitness
	 * @throws Exception 
	 */
	public final Chromosome<T> getLowestFitnessChromosome() throws Exception
	{
		return population.getLowestFitnessChromosome();
	}
	
	/**
	 * Default is to do nothing, can be overridden in children
	 */
	protected void evolutionHook() {}
}
