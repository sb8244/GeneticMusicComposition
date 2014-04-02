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
public class JobThread extends Thread
{
	private String fileName;
	private long seed;
	private String outputFilename;
	private boolean writeNewLine;
	private File tabFile;
	
	/**
	 * Create a new JobThread for a file and random seed
	 * @param fileName The filename of the input midi
	 * @param outputFilename The filename of the output file to generate
	 * @param seed The random seed to use
	 */
	public JobThread(String fileName, String outputFilename, long seed, File tabFile, boolean writeNewLine)
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
			
			MusicAnalyzer analyzer= new MusicAnalyzer(measures);
			analyzer.analyzeRhythm();
			analyzer.analyzeTones(null);
			
			AnalysisVector rhythmGoal = analyzer.getRhythmicAnalysisVector();
			AnalysisVector tonalGoal = analyzer.getPitchAnalysisVector();

			RhythmThread rt = new RhythmThread(seed, rhythmGoal, measures.size());
			rt.start();
			
			TonalThread tt = new TonalThread(seed, tonalGoal, measures.size(), analyzer.getComputedRange(), analyzer.getAverageTonesPerMeasure());
			tt.start();
			
			rt.join();
			tt.join();
			
			Measure[] rhythm = rt.getResults();
			Measure[] tones = tt.getResults();
			
			String outputString = "";
			if(rhythm != null && tones != null)
			{
				MeasureMerge merger = new MeasureMerge(rhythm, tones);
				Measure[] product = merger.merge();
				
				/*
				 * Convert Product into MIDI format using JFugue
				 */
				JFugueConverter jconverter = new JFugueConverter(product);
				Player player = new Player();
	
				File midiFile = new File(outputFilename + date.getTime() + ".midi");
				midiFile.getParentFile().mkdirs();
				outputString += "Complete\n";
				String resultString = "I33 " + jconverter.convertToString() + "\n";
				outputString += resultString + "\n";
				player.saveMidi(resultString, midiFile);
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
			
			FileWriter tabFileWriter = new FileWriter(this.tabFile, true);
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
