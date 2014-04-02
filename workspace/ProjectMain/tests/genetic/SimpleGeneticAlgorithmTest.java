package genetic;

import static org.junit.Assert.*;

import org.junit.Test;

import genetic.interfaces.Chromosome;
import genetic.interfaces.GeneticAlgorithm;
import genetic.interfaces.Population;

/**
 * Real simple classes to make sure genetic algorithm doesn't choke up
 * @author Steve
 *
 */
public class SimpleGeneticAlgorithmTest 
{
	/**
	 * Just make sure a Chromosome is returned
	 * @throws Exception
	 */
	@Test
	public void simpleEvaluation() throws Exception
	{
		GeneticAlgorithm<String> mock = new MockGeneticAlgorithm(new MockImmediateReturnPopulation());
		Chromosome<String> fit = mock.execute();
		assertNotNull(fit);
		assertEquals(fit.getClass().getName(), "genetic.MockChromosome");
		assertEquals("immediate", fit.getData());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void delayedEvaluation() throws Exception
	{
		GeneticAlgorithm<String> mock = new MockGeneticAlgorithm(new MockDelayedReturnPopulation());
		Chromosome<?> fit = mock.execute();
		assertNotNull(fit);
		assertEquals(fit.getClass().getName(), "genetic.MockChromosome");
		assertEquals("delayed", fit.getData());
	}
	
	/**
	 * Test a population that requires mutation in order to process
	 * 
	 * In this case, the first member in the population(index 0) should return
	 * @throws Exception
	 */
	@Test
	public void testMutateEvaluationFirst() throws Exception
	{
		GeneticAlgorithm<String> mock = new MockGeneticAlgorithm(new MockMutateReturnPopulationLowestFirst());
		Chromosome<?> fit = mock.execute();
		assertNotNull(fit);
		assertEquals(fit.getClass().getName(), "genetic.MockChromosome");
		assertEquals("0", fit.getData());
	}
	
	/**
	 * Test a population that requires mutation in order to process
	 * 
	 * In this case, the last member in the population(index 9) should return
	 * @throws Exception
	 */
	@Test
	public void testMutateEvaluationLast() throws Exception
	{
		GeneticAlgorithm<String> mock = new MockGeneticAlgorithm(new MockMutateReturnPopulationHighestFirst());
		Chromosome<?> fit = mock.execute();
		assertNotNull(fit);
		assertEquals(fit.getClass().getName(), "genetic.MockChromosome");
		assertEquals("9", fit.getData());
	}
	
	/**
	 * Test a Population that requires Crossover to become fit. On "crossover", the
	 * 	0 element becomes fit
	 * @throws Exception
	 */
	@Test
	public void testCrossoverEvaluation() throws Exception
	{
		GeneticAlgorithm<String> mock = new MockGeneticAlgorithm(new MockMutateReturnPopulationLowestFirst());
		Chromosome<?> fit = mock.execute();
		assertNotNull(fit);
		assertEquals(fit.getClass().getName(), "genetic.MockChromosome");
		assertEquals("0", fit.getData());
	}
}

class MockChromosome extends Chromosome<String>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6993469342193199759L;

	public MockChromosome(String data)
	{
		this(data, 0.0);
	}
	
	public MockChromosome(String data, double fitness)
	{
		this.setData(data);
		this.setFitness(fitness);
	}
	
	public void mutate() throws Exception {
		this.setFitness(Math.max(0, getFitness()-.01));
	}

	public void reset() {
	}

	public double getFitness() throws Exception {
		return this.fitness;
	}

	public void introduceRandomness() {}
}

class MockImmediateReturnPopulation extends Population<String>
{
	@Override
	public Chromosome<String> evaluate() {
		Chromosome<String> c = new MockChromosome("immediate");
		return c;
	}
	public void mutate(){}
	public void crossover(){}
	public void reset() {}
}

class MockDelayedReturnPopulation extends Population<String>
{
	int times = 0;
	public MockDelayedReturnPopulation()
	{
		this.population = new MockChromosome[1];
		this.population[0] = new MockChromosome("test");
	}
	@Override
	public Chromosome<String> evaluate() {
		if(times == 5)
			return new MockChromosome("delayed");
		times++;
		return null;
	}
	public void mutate(){}
	public void crossover(){}
	public void reset() {}
}

abstract class MockMutateReturnPopulation extends Population<String>
{
	public Chromosome<String> evaluate() throws Exception
	{
		for(Chromosome<String> c: population)
			if(c.getFitness() == 0.0)
				return c;
		return null;
	}
	public void mutate() throws Exception
	{
		for(int i = 0; i < population.length; i++)
		{
			population[i].mutate();
		}
	}
}

class MockMutateReturnPopulationLowestFirst extends MockMutateReturnPopulation
{
	public MockMutateReturnPopulationLowestFirst()
	{
		population = new MockChromosome[10];
		for(int i = 0; i < 10; i++)
		{
			population[i] = new MockChromosome(i+"", 1.01);
		}
		population[0] = new MockChromosome("0", 1.0);
	}
	public void crossover(){}
	public void reset() {}
}

class MockMutateReturnPopulationHighestFirst extends MockMutateReturnPopulation
{
	public MockMutateReturnPopulationHighestFirst()
	{
		population = new MockChromosome[10];
		for(int i = 0; i < 10; i++)
		{
			population[i] = new MockChromosome(i+"", 1.01);
		}
		population[9] = new MockChromosome("9", 1.0);
	}
	public void crossover(){}
	public void reset() {}
}

class MockMutateCrossoverPopulation extends Population<String>
{
	public MockMutateCrossoverPopulation()
	{
		population = new MockChromosome[10];
		for(int i = 0; i < 10; i++)
		{
			population[i] = new MockChromosome(i+"", 1.01);
		}
	}
	
	public Chromosome<String> evaluate() throws Exception
	{
		for(Chromosome<String> c: population)
			if(c.getFitness() == 0.0)
				return c;
		return null;
	}

	public void mutate() {}

	public void crossover()
	{
		population[0] = new MockChromosome("0", 0.0);
	}

	public void reset() {}
}

class MockGeneticAlgorithm extends GeneticAlgorithm<String>
{
	public MockGeneticAlgorithm(Population<String> p)
	{
		this.population = p;
	}
}