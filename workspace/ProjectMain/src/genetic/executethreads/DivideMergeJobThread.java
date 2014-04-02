package genetic.executethreads;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.sound.midi.InvalidMidiDataException;

import org.jfugue.Player;

import measure.Measure;
import processor.AnalysisVector;
import processor.MeasureMerge;
import processor.MusicAnalyzer;
import processor.exceptions.BeatDurationOutOfBoundsException;
import processor.support.JFugueConverter;
import processor.support.MIDIConverter;

/**
 * Thread to execute an entire job from reading the file down to producing
 * the output files
 * 
 * @author Steve
 *
 */
public class DivideMergeJobThread extends Thread
{
	private String fileName;
	private long seed;
	private String outputFilename;
	private static int MEASURES_TO_DIVIDE_BY = 3;
	
	private boolean writeNewLine;
	private File tabFile;
	
	/**
	 * Create a new JobThread for a file and random seed
	 * @param fileName The filename of the input midi
	 * @param outputFilename The filename of the output file to generate
	 * @param seed The random seed to use
	 * @param tabFileWriter A FileWriter to receive the tab output contribution
	 * @param writeNewLine Whether the tabbed line will end or be tabbed
	 */
	public DivideMergeJobThread(String fileName, String outputFilename, long seed, File tabFile, boolean writeNewLine)
	{
		super ("Job Thread");
		this.fileName = fileName;
		this.seed = seed;
		this.outputFilename = outputFilename;
		this.writeNewLine = writeNewLine;
		this.tabFile = tabFile;
	}
	
	public void run()
	{
		try 
		{
			Date date = new Date();
			File musicStringFile = new File(outputFilename + date.getTime() + ".txt");
			musicStringFile.getParentFile().mkdirs();
			FileWriter fw = new FileWriter(musicStringFile);
			
			fw.append("Seed: " + seed + "\r\n");
			fw.append("Tonal Population Size: " + TonalThread.TONAL_POPULATION + "\r\n");
			fw.append("Rhythm Population Size: " + RhythmThread.RHYTHM_POPULATION + "\r\n");
			fw.close();
			
			long startTime = System.currentTimeMillis();
			
			MIDIConverter converter = new MIDIConverter(fileName);
			converter.convert();
			
			ArrayList<Measure> measures = converter.getConvertedMeasures();
			ArrayList<Measure[]> products = new ArrayList<Measure[]>();
			ArrayList<RhythmThread> rhythmThreads = new ArrayList<RhythmThread>();
			ArrayList<TonalThread> tonalThreads = new ArrayList<TonalThread>();
			
			int iterationCount = (int)Math.ceil(measures.size() / (double)MEASURES_TO_DIVIDE_BY);
			int addedExtraMeasureCount = 0;
			for(int i = 0; i < iterationCount; i++)
			{
				ArrayList<Measure> dividedMeasures = new ArrayList<Measure>();
				int startPosition = MEASURES_TO_DIVIDE_BY * i;
				for(int j = 0; j < MEASURES_TO_DIVIDE_BY && j + startPosition < measures.size(); j++)
				{
					dividedMeasures.add(measures.get(j+startPosition));
				}
				if(dividedMeasures.size() < MEASURES_TO_DIVIDE_BY)
				{
					System.out.println();
				}
				
				MusicAnalyzer analyzer= new MusicAnalyzer(dividedMeasures);
				analyzer.analyzeRhythm();
				analyzer.analyzeTones(null);
				
				AnalysisVector rhythmGoal = analyzer.getRhythmicAnalysisVector();
				AnalysisVector tonalGoal = analyzer.getPitchAnalysisVector();
	
				RhythmThread rt = new RhythmThread(seed, rhythmGoal,dividedMeasures.size());
				rt.start();
				
				TonalThread tt = new TonalThread(seed, tonalGoal, dividedMeasures.size(), analyzer.getComputedRange(), analyzer.getAverageTonesPerMeasure());
				tt.start();
				
				rhythmThreads.add(rt);
				tonalThreads.add(tt);
			}
			
			/*
			 * Let all threads finish executing
			 */
			for(MusicThread t: rhythmThreads)
			{
				t.join();
			}
			for(MusicThread t: tonalThreads)
			{
				t.join();
			}
			
			/*
			 * Grab all data from all threads (they are parallel)
			 */
			boolean failed = false;
			for(int currentThread = 0; currentThread < rhythmThreads.size(); currentThread++)
			{
				Measure[] rhythm = rhythmThreads.get(currentThread).getResults();
				Measure[] tones = tonalThreads.get(currentThread).getResults();
				
				if(rhythm != null && tones != null)
				{
					MeasureMerge merger = new MeasureMerge(rhythm, tones);
					Measure[] product = merger.merge();
					products.add(product);
				}
				else
				{
					failed = true;
				}
			}
			
			String outputString = "";
			if(!failed)
			{
				Measure[] product = new Measure[measures.size()];
				int currentPosition = 0;
				for(Measure[] divided: products)
				{
					for(Measure m: divided)
					{
						product[currentPosition++] = m;
					}
				}
				
				/*
				 * Convert Product into MIDI format using JFugue
				 */
				JFugueConverter jconverter = new JFugueConverter(product);
				Player player = new Player();
	
				File midiFile = new File(outputFilename + date.getTime() + ".midi");
				midiFile.getParentFile().mkdirs();
				String resultString = "I33 " + jconverter.convertToString() + "\n";
				player.saveMidi(resultString, midiFile);
				outputString += "COMPLETED\n";
				outputString += resultString + "\n";
			}
			else
			{
				outputString += "DID NOT COMPLETE\n";
			}
			
			long endTime = System.currentTimeMillis();
			
			/*
			 * Write output and parameters to file
			 */
			fw = new FileWriter(musicStringFile, true);
			fw.append(outputString);
			fw.append(endTime - startTime + "\n");
			fw.close();
			
			FileWriter tabFileWriter = new FileWriter(tabFile, true);
			tabFileWriter.append(endTime-startTime + "");
			if(this.writeNewLine)
				tabFileWriter.append("\n");
			else
				tabFileWriter.append("\t");
			tabFileWriter.close();
			System.err.println("Job finished");
		} catch (InvalidMidiDataException e) 
		{
			e.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		} catch (BeatDurationOutOfBoundsException e) 
		{
			e.printStackTrace();
		} catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
}
