import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;

import genetic.executethreads.JobThread;
/**
 * This runner will execute a specified number of jobs concurrently, but shouldn't be used
 * in experimentation to prevent speed contamination
 * 
 * @author Steve
 *
 */
public class ThreadedRunner 
{
	/**
	 * For experimentation, this should be set to 1 to prevent threading issues contaminating speed results
	 */
	private static final int MAX_JOBS = 1;
	
	/**
	 * Run each experiment 5 times
	 */
	private static final int NUM_TESTS = 5;
	
	/**
	 * The input list of songs to use in this runner (and other runners who may use this list)
	 */
	public static String[] inputList = {
		"140", "141", "aflat-tire(72)", "allsame", "blues-bass-4",
		"curtis(101)", "dada dadat(103)", "Dolores(118)", "dont-get-around-much(122)",
		"el-gaucho(131)", "floaty(113)", "four-on-six(150)", "freddie-freeloader(151)",
		"funk-time-fixed(115)", "funky-lil-blues(81)", "funky-soul-groove(77)", "gritty(140)",
		"gypsy-swing(91)", "house-o-horror(85)", "little-m(117)", "low-down(75)", "noir(85)",
		"off-beat(77)", "open-e(73)", "pent-up(137)", "room-ba-with-a-view(91)",
		"simple-triads(133)", "walk-120", "walk-120-rest", "walk-120-vary"
	};
	/**
	 * main runner
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		String base = "results/30nondc/";
		File tabFile = new File(base + "tabbedResults.csv");
		tabFile.delete();
		tabFile.createNewFile();
		HashSet<JobThread> runningThreads = new HashSet<JobThread>();
		for(String fileName: inputList)
		{
			fileName = "real/" + fileName;
			for(int i = 0; i < NUM_TESTS; i++)
			{
				//setup file information
				String outputName = base + fileName + "/";
				
				//get a random seed (or set a predefined seed)
				long seed = System.currentTimeMillis();
				JobThread jt = new JobThread("input/" + fileName + ".mid", outputName, seed, tabFile, i == NUM_TESTS-1);
				jt.start();
				Thread.sleep(5);
				
				runningThreads.add(jt);
				
				while(runningThreads.size() >= MAX_JOBS)
				{
					for(JobThread runningThread: runningThreads)
					{
						if(!runningThread.isAlive())
						{
							runningThreads.remove(runningThread);
							break;
						}
					}
					Thread.sleep(50);
				}
			}
		}
	}
}
