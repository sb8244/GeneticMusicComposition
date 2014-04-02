package genetic.tonal;

import measure.Beat;
import measure.Measure;
import processor.AnalysisVector;
import processor.MusicAnalyzer;
import processor.exceptions.AnalysisVectorMismatchException;
import processor.exceptions.BeatDurationOutOfBoundsException;
import support.SeededRandom;
import genetic.interfaces.MusicChromosome;
import genetic.interfaces.Mutation;
import genetic.mutations.ReverseMeasuresMutation;
import genetic.mutations.SwapMeasuresMutation;
import genetic.mutations.measure.*;

/**
 * Tonal Implementation of a Chromosome
 * @author Steve
 *
 */
public class TonalChromosome extends MusicChromosome
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5938773690905137882L;
	private static final double TRANSPOSE_SINGLE_NOTE_MUTATION_CHANCE = .25;
	private static final double TRANSPOSE_MEASURE_UP_MUTATION_CHANCE = TRANSPOSE_SINGLE_NOTE_MUTATION_CHANCE + .1; //.35
	private static final double TRANSPOSE_MEASURE_DOWN_MUTATION_CHANCE = TRANSPOSE_MEASURE_UP_MUTATION_CHANCE + .1; //.45
	private static final double SWAP_NOTES_MUTATION_CHANCE = TRANSPOSE_MEASURE_DOWN_MUTATION_CHANCE + .15; //.6
	private static final double RANDOMIZE_MEASURE_MUTATION_CHANCE = SWAP_NOTES_MUTATION_CHANCE + .01; //.61
	private static final double RESET_MEASURE_MUTATION_CHANCE = RANDOMIZE_MEASURE_MUTATION_CHANCE + .01; //.62
	private static final double REVERSE_MEASURE_MUTATION_CHANCE = RESET_MEASURE_MUTATION_CHANCE + .09; //.71
	private static final double REVERSE_MEASURES_MUTATION_CHANCE = REVERSE_MEASURE_MUTATION_CHANCE + .14; //.85
	private static final double SWAP_MEASURES_MUTATION_CHANCE = REVERSE_MEASURES_MUTATION_CHANCE + .15; //1
	
	private int goalRange;
	/**
	 * @param numMeasures The measures that each measure will have
	 * @param goalVector The Rhythm AnalysisVector to check fitness against
	 * @param goalRange The range that these tones should fall in, given as part of the input Music
	 */
	public TonalChromosome(int numMeasures, AnalysisVector goalVector, int goalRange, int numBeats) 
	{
		super(numMeasures, goalVector, true, numBeats);
		this.goalRange = goalRange;
	}
	
	@Override
	public void mutate() throws Exception 
	{
		double rand = SeededRandom.random();
		Measure randomMeasure = this.getData()[(int)(SeededRandom.random() * this.getData().length)];
		Mutation mutation = null;
		if(rand <= TRANSPOSE_SINGLE_NOTE_MUTATION_CHANCE)
		{
			mutation = new TransposeSinglePitchMutation(randomMeasure);
		}
		else if(rand <= TRANSPOSE_MEASURE_UP_MUTATION_CHANCE)
		{
			mutation = new TransposeMeasureUpMutation(randomMeasure);
		}
		else if(rand <= TRANSPOSE_MEASURE_DOWN_MUTATION_CHANCE)
		{
			mutation = new TransposeMeasureDownMutation(randomMeasure);
		}
		else if(rand <= SWAP_NOTES_MUTATION_CHANCE)
		{
			mutation = new SwapBeatMutation(randomMeasure);
		}
		else if(rand <= RANDOMIZE_MEASURE_MUTATION_CHANCE)
		{
			mutation = new RandomizeMeasureMutation(randomMeasure, true);
		}
		else if(rand <= RESET_MEASURE_MUTATION_CHANCE)
		{
			mutation = new ResetMeasureMutation(randomMeasure, this.numBeats);
		}
		else if(rand <= REVERSE_MEASURE_MUTATION_CHANCE)
		{
			mutation = new ReverseMeasureMutation(randomMeasure);
		}
		else if(rand <= REVERSE_MEASURES_MUTATION_CHANCE)
		{
			mutation = new ReverseMeasuresMutation(this.getData());
		}
		else if(rand <= SWAP_MEASURES_MUTATION_CHANCE)
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
		analyzer.analyzeTones(goalRange);
		AnalysisVector vector = analyzer.getPitchAnalysisVector();
		double fitness = vector.distance(goalVector);
		super.setFitness(fitness);
		return fitness;
	}
	
	public AnalysisVector getVector()
	{
		MusicAnalyzer analyzer = new MusicAnalyzer(this.getData());
		analyzer.analyzeTones(goalRange);
		AnalysisVector vector = analyzer.getPitchAnalysisVector();
		return vector;
	}

	/**
	 * Perform a memetic fix
	 */
	public void memeticFix() 
	{
		boolean changed = false;
		for(Measure m: this.getData())
		{
			for(Beat b: m.getBeats())
			{
				double averageOctaveFixed = this.goalVector.get("averageOctave");
				double octaveRange = b.getOctave() - (averageOctaveFixed * Beat.MAX_OCTAVE);
				//goalRange / 12 will give the desired octave
				//This must be ceiled so it will be an integer octave and not a double
				//DO NOT TRUNCATE
				if(Math.abs(octaveRange) > Math.ceil(this.goalRange/12.0) )
				{
					b.setOctave((int) (averageOctaveFixed * Beat.MAX_OCTAVE) + 1);
					changed = true;
					break;
				}
			}
			if(changed)
				break;
		}
	}
}
