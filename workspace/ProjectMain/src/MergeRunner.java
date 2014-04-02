import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import genetic.executethreads.DivideMergeJobThread;


/**
 * This runner will utilize a Divide & Conquer 
 * @author Steve
 *
 */
public class MergeRunner 
{
	private static final int NUM_TESTS = 5;
	
	/**
	 * Main point
	 * @param args
	 * @throws InterruptedException
	 * @throws IOException 
	 */
	public static void main(String[] args) throws InterruptedException, IOException
	{
		String base = "results/30dc/";
		File tabFile = new File(base + "tabbedResults.csv");
		tabFile.delete();
		tabFile.createNewFile();
		
		for(String fileName: ThreadedRunner.inputList)
		{
			fileName = "real/" + fileName;
			for(int i = 0; i < NUM_TESTS; i++)
			{
				//setup file information
				String outputName = base + fileName + "/";
				
				//get a random seed (or set a predefined seed)
				long seed = System.currentTimeMillis();
				DivideMergeJobThread jt = new DivideMergeJobThread("input/" + fileName + ".mid", outputName, seed, tabFile, i == NUM_TESTS-1);
				jt.start();
				jt.join();
			}
		}
	}
}
