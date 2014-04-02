package suite;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import genetic.*;
import genetic.mutations.*;
import genetic.mutations.measure.*;
import processor.*;
import processor.support.JFugueConverterTest;
import processor.support.MIDIConverterTest;
import support.SeededRandom;

/**
 * Test Suite for all tests
 * @author Steve
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ 
	SimpleGeneticAlgorithmTest.class, 
	MusicAnalyzerTest.class,
	AnalysisVectorTest.class,
	ReverseMeasuresMutationTest.class,
	SwapMeasuresMutationTest.class,
	JoinBeatMutationTest.class,
	SplitBeatMutationTest.class,
	SwapBeatMutationTest.class,
	ReverseMeasureMutationTest.class,
	ResetMeasureMutationTest.class,
	RandomizeMeasureMutationTest.class,
	TransposeMeasureUpMutationTest.class,
	TransposeMeasureDownMutationTest.class,
	TransposeSinglePitchMutationTest.class,
	JFugueConverterTest.class,
	MIDIConverterTest.class,
	AddTieMutationTest.class
})
public class FullTestSuite {
	/**
	 * DELTA for double comparisons
	 */
	public static double DELTA = .0000005;
	
	/**
	 * Setup the Tests
	 */
	@BeforeClass 
    public static void setUpClass() 
	{      
		SeededRandom.init();
		SeededRandom.setSeed(System.currentTimeMillis());
    }
}

/*
 * Next Todos
 */
