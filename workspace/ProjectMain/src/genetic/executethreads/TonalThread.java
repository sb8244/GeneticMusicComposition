package genetic.executethreads;

import measure.Measure;
import genetic.interfaces.Chromosome;
import genetic.tonal.TonalGeneticAlgorithm;
import processor.AnalysisVector;
import support.SeededRandom;

/**
 * Thread for executing Tonal Genetic Algorithm
 * @author Steve
 *
 */
public class TonalThread extends MusicThread
{
	/**
	 * The size of the Population pool that will be used
	 */
	public static int TONAL_POPULATION = 1000;
	
	private AnalysisVector goal;
	private int numMeasures;
	private int range;
	private long seed;
	private int numBeatsPerMeasure;
	
	/**
	 * Construct a new TonalThread with a given AnalysisVector and measures
	 * @param seed 
	 * @param analysis The Tonal Analysis Vector
	 * @param numMeasures The number of measures to generate
	 * @param range The range of the input file determined by analysis
	 */
	public TonalThread(long seed, AnalysisVector analysis, int numMeasures, int range, int numBeatsPerMeasure)
	{
		super("Tonal Thread");
		
		this.goal = analysis;
		this.numMeasures = numMeasures;
		this.range = range;
		this.seed = seed;
		this.numBeatsPerMeasure = numBeatsPerMeasure;
	}
	
	public void run()
	{
		SeededRandom.init();
		SeededRandom.setSeed(this.seed);
		
		TonalGeneticAlgorithm rga = new TonalGeneticAlgorithm(TONAL_POPULATION, numMeasures, goal, range, numBeatsPerMeasure);
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
