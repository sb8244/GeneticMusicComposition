import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;

import org.jfugue.Player;

import measure.Measure;
import processor.AnalysisVector;
import processor.MeasureMerge;
import processor.MusicAnalyzer;
import processor.support.JFugueConverter;
import processor.support.MIDIConverter;
import support.SeededRandom;
import genetic.interfaces.Chromosome;
import genetic.rhythm.RhythmGeneticAlgorithm;
import genetic.tonal.TonalGeneticAlgorithm;

/**
 * A sample runner for Rhythmic genetic algorithm
 * @author Steve
 *
 */
public class Runner 
{
	private static int MEASURE_MULTIPLIER = 1;
	private static int RHYTHM_POPULATION = 5000;
	private static int TONAL_POPULATION = 2000;
	//private static String[] inputList = {"test-jfugue", "blues-bass-4", "walk-120" , "walk-120-rest", "walk-120-vary", "walk-160"};
	private static String[] inputList = {"floaty(113)", "funk-time-fixed", "little-m(117)", "dada dadat(103)", "curtis(101)"};
	/**
	 * main runner
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		for(String fileName: inputList)
		{
			fileName = "real/" + fileName;
			for(int i = 0; i < 5; i++)
			{
				//get a random seed (or set a predefined seed)
				long seed = System.currentTimeMillis();
				SeededRandom.setSeed(seed);

				//setup file information
				String outputName = "results/" + fileName + "/";

				Date date = new Date();
				File musicStringFile = new File(outputName + date.getTime() + ".txt");
				musicStringFile.getParentFile().mkdirs();
				FileWriter fw = new FileWriter(musicStringFile);
				fw.append("Seed: " + seed + "\r\n");
				fw.append("Tonal Population Size: " + TONAL_POPULATION + "\r\n");
				fw.append("Rhythm Population Size: " + RHYTHM_POPULATION + "\r\n");
				fw.append("Measure Multiplier: " + MEASURE_MULTIPLIER);
				fw.close();
				
				//convert input MIDI to Object
				MIDIConverter converter = new MIDIConverter("input/" + fileName + ".mid");
				converter.convert(MEASURE_MULTIPLIER-1);

				ArrayList<Measure> measures = converter.getConvertedMeasures();
				
				MusicAnalyzer analyzer= new MusicAnalyzer(measures);

				/*
				 * Run Rhythmic GA
				 */
				analyzer.analyzeRhythm();
				AnalysisVector goal = analyzer.getRhythmicAnalysisVector();

				RhythmGeneticAlgorithm rga = new RhythmGeneticAlgorithm(RHYTHM_POPULATION, measures.size(), goal);

				Chromosome<Measure[]> lowestRhythm = rga.execute();
				Measure[] m = lowestRhythm.getData();

				for(Measure measure: m)
					System.out.println("Measure: " + measure.toString());

				/*
				 * Run Tonal GA
				 */
				analyzer.analyzeTones(null);
				goal = analyzer.getPitchAnalysisVector();
				TonalGeneticAlgorithm tga = new TonalGeneticAlgorithm(TONAL_POPULATION, measures.size(), goal, analyzer.getComputedRange(), 4);

				Chromosome<Measure[]> lowestTone = tga.execute();
				m = lowestTone.getData();
				for(Measure measure: m)
					System.out.println("Measure: " + measure.toString());

				/*
				 * Results
				 */
				System.out.println();
				System.err.println("/////RESULT");

				/*
				 * Merge individual products into new Product
				 */
				MeasureMerge merger = new MeasureMerge(lowestRhythm.getData(), lowestTone.getData());
				Measure[] product = merger.merge();
				for(Measure measure: product)
					System.out.println("Measure: " + measure.toString());

				/*
				 * Convert Product into MIDI format using JFugue
				 */
				JFugueConverter jconverter = new JFugueConverter(product);
				Player player = new Player();

				File midiFile = new File(outputName + date.getTime() + ".midi");
				midiFile.getParentFile().mkdirs();
				String outputString = "I33 " + jconverter.convertToString();
				player.saveMidi(outputString, midiFile);

				/*
				 * Write output and parameters to file
				 */
				
				fw = new FileWriter(musicStringFile);
				fw.append(outputString);
				fw.close();
				
				//player.play(outputString);
				
			}
		}
	}
}
