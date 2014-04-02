package genetic.interfaces;

import java.io.Serializable;

/**
 * Holds all methods and responsibilities shared between all Chromosomes
 * 
 * @author Steve
 * @param <T> Data type stored
 *
 */
public abstract class Chromosome<T> implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8276996084319851399L;
	protected double fitness;
	private T data;
	
	/**
	 * 
	 * @return The fitness of this chromosome
	 * @throws Exception 
	 */
	public abstract double getFitness() throws Exception;
	
	/**
	 * 
	 * @return The data this Chromosome holds
	 */
	public T getData()
	{
		return data;
	}
	
	/**
	 * 
	 * @param newFitness The new fitness of this Chromosome
	 */
	protected void setFitness(double newFitness)
	{
		fitness = newFitness;
	}
	
	/**
	 * 
	 * @param data The new data of this Chromosome
	 */
	protected void setData(T data)
	{
		this.data = data;
	}
	
	/**
	 * Perform mutation logic, however decided by the Chromosome
	 * @throws Exception 
	 */
	public abstract void mutate() throws Exception;
	
	/**
	 * Provide a way to reset a Chromosome
	 */
	public abstract void reset();
}
