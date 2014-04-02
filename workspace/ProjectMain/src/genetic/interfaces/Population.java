package genetic.interfaces;


/**
 * Defines operations that a Population must implement. Sadly, this cannot be
 * 	abstract because the Chromosome is abstract and can not be initialized
 * 
 * @author Steve
 * @param <T> Data type of a Chromosome
 *
 */
public abstract class Population<T>
{
	protected Chromosome<T>[] population;
	
	/**
	 * 
	 * @return A fit chromosome or null if none are fit enough
	 * @throws Exception 
	 */
	public abstract Chromosome<T> evaluate() throws Exception;
	
	/**
	 * Perform a mutation on each Chromosome
	 * @throws Exception 
	 */
	public abstract void mutate() throws Exception;
	
	/**
	 * Perform a crossover on population
	 */
	public abstract void crossover();
	
	/**
	 * 
	 * @return The Chromosome<T> with the highest fitness
	 * @throws Exception 
	 */
	public Chromosome<T> getHighestFitnessChromosome() throws Exception
	{
		double max = Double.NEGATIVE_INFINITY;
		Chromosome<T> maxChromosome = null;
		for(Chromosome<T> curr: population)
		{
			if(curr.getFitness() > max)
			{
				max = curr.getFitness();
				maxChromosome = curr;
			}
		}
		return maxChromosome;
	}
	
	/**
	 * 
	 * @return The Chromosome<T> with the lowest fitness
	 * @throws Exception 
	 */
	public Chromosome<T> getLowestFitnessChromosome() throws Exception
	{
		double min = Double.POSITIVE_INFINITY;
		Chromosome<T> minChromosome = null;
		for(Chromosome<T> curr: population)
		{
			if(curr.getFitness() < min)
			{
				min = curr.getFitness();
				minChromosome = curr;
			}
		}
		return minChromosome;
	}
	
	/**
	 * 
	 * @return The average fitness of the population
	 * @throws Exception
	 */
	public double getAverageFitness() throws Exception
	{
		double sum = 0;
		for(Chromosome<T> curr: population)
		{
			sum += curr.getFitness();
		}
		sum = sum / population.length;
		return sum;
	}
	
	/**
	 * 
	 * @return The Chromosomes in this population
	 */
	public Chromosome<T>[] getChromosomes()
	{
		return this.population;
	}
	
	/**
	 * Re-instantiate the entire population
	 */
	public abstract void reset();
}
