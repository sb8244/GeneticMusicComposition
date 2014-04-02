package genetic.executethreads;

import measure.Measure;
import genetic.interfaces.Chromosome;
import genetic.rhythm.RhythmGeneticAlgorithm;
import processor.AnalysisVector;
import support.SeededRandom;

/**
 * Thread for executing Rhythm Genetic Algorithm
 * @author Steve
 *
 */
public class RhythmThread extends MusicThread
{
	/**
	 * The size of the Population pool that will be used
	 */
	public static int RHYTHM_POPULATION = 1000;
	
	private AnalysisVector goal;
	private int numMeasures;
	private long seed;
	
	/**
	 * Construct a new RhythmThread with a given AnalysisVector and measures
	 * @param seed 
	 * @param analysis The Rhythm Analysis Vector
	 * @param numMeasures The number of measures to generate
	 */
	public RhythmThread(long seed, AnalysisVector analysis, int numMeasures)
	{
		super("Rhythm Thread");
		
		this.goal = analysis;
		this.numMeasures = numMeasures;
		this.seed = seed;
	}
	
	public void run()
	{
		SeededRandom.init();
		SeededRandom.setSeed(seed);
		
		RhythmGeneticAlgorithm rga = new RhythmGeneticAlgorithm(RHYTHM_POPULATION, numMeasures, goal);
		Chromosome<Measure[]> lowestRhythm;
		try 
		{
			lowestRhythm = rga.execute();
			if(lowestRhythm != null)
			{
				this.result = lowestRhythm.getData();
			}
			else
			{
				this.result = null;
			}
			System.out.println(this.getName() + " finished");
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
