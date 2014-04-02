package genetic.rhythm;

import measure.Measure;
import processor.AnalysisVector;
import processor.MusicAnalyzer;
import processor.exceptions.AnalysisVectorMismatchException;
import processor.exceptions.BeatDurationOutOfBoundsException;
import support.SeededRandom;
import genetic.interfaces.MusicChromosome;
import genetic.interfaces.Mutation;
import genetic.mutations.*;
import genetic.mutations.measure.*;

/**
 * Rhythm implementation of a Chromosome
 * @author Steve
 *
 */
public class RhythmChromosome extends MusicChromosome
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7606678007646898910L;
	private static double JOIN_BEAT_MUTATION_CHANCE_TOP = .25;
	private static double SPLIT_BEAT_MUTATION_CHANCE_TOP = JOIN_BEAT_MUTATION_CHANCE_TOP + .25;
	private static double SWAP_BEAT_MUTATION_CHANCE_TOP = SPLIT_BEAT_MUTATION_CHANCE_TOP + .2;
	private static double RANDOMIZE_MEASURE_MUTATION_CHANCE_TOP = SWAP_BEAT_MUTATION_CHANCE_TOP + .09;
	private static double RESET_MEASURE_MUTATION_CHANCE_TOP = RANDOMIZE_MEASURE_MUTATION_CHANCE_TOP + .01;
	private static double REVERSE_MEASURE_MUTATION_CHANCE_TOP = RESET_MEASURE_MUTATION_CHANCE_TOP + .01;
	private static double REVERSE_MEASURES_MUTATION_CHANCE_TOP = REVERSE_MEASURE_MUTATION_CHANCE_TOP + .09;
	private static double SWAP_MEASURES_MUTATION_CHANCE_TOP = REVERSE_MEASURES_MUTATION_CHANCE_TOP + .1;
	
	/**
	 * @param numMeasures The measures that each measure will have
	 * @param goalVector The Rhythm AnalysisVector to check fitness against
	 */
	public RhythmChromosome(int numMeasures, AnalysisVector goalVector, int numBeats) {
		super(numMeasures, goalVector, false, numBeats);
	}

	public void mutate() throws Exception 
	{
		double rand = SeededRandom.random();
		Measure randomMeasure = this.getData()[(int)(SeededRandom.random() * this.getData().length)];
		Mutation mutation = null;
		if(rand <= JOIN_BEAT_MUTATION_CHANCE_TOP)
		{
			mutation = new JoinBeatMutation(randomMeasure);
		}
		else if(rand <= SPLIT_BEAT_MUTATION_CHANCE_TOP)
		{
			mutation = new SplitBeatMutation(randomMeasure);
		}
		else if(rand <= SWAP_BEAT_MUTATION_CHANCE_TOP)
		{
			mutation = new SwapBeatMutation(randomMeasure);
		}
		else if(rand <= RANDOMIZE_MEASURE_MUTATION_CHANCE_TOP)
		{
			mutation = new RandomizeMeasureMutation(randomMeasure, true);
		}
		else if(rand <= RESET_MEASURE_MUTATION_CHANCE_TOP)
		{
			mutation = new ResetMeasureMutation(randomMeasure, this.numBeats);
		}
		else if(rand <= REVERSE_MEASURE_MUTATION_CHANCE_TOP)
		{
			mutation = new ReverseMeasureMutation(randomMeasure);
		}
		else if(rand <= REVERSE_MEASURES_MUTATION_CHANCE_TOP)
		{
			mutation = new ReverseMeasuresMutation(this.getData());
		}
		else if(rand <= SWAP_MEASURES_MUTATION_CHANCE_TOP)
		{
			if(this.numMeasures <= 1)
				return;
			mutation = new SwapMeasuresMutation(this.getData());
		}
		mutation.mutate();
	}

	public double getFitness() throws BeatDurationOutOfBoundsException, AnalysisVectorMismatchException
	{
		MusicAnalyzer analyzer = new MusicAnalyzer(this.getData());
		analyzer.analyzeRhythm();
		AnalysisVector vector = analyzer.getRhythmicAnalysisVector();
		double fitness = vector.distance(goalVector);
		super.setFitness(fitness);
		return fitness;
	}
	
	public AnalysisVector getVector()
	{
		MusicAnalyzer analyzer = new MusicAnalyzer(this.getData());
		try {
			analyzer.analyzeRhythm();
		} catch (BeatDurationOutOfBoundsException e) {
			e.printStackTrace();
		}
		AnalysisVector vector = analyzer.getRhythmicAnalysisVector();
		return vector;
	}
}
