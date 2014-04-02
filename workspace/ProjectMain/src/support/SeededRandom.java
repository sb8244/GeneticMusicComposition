package support;

import java.util.Random;

/**
 * Provide a seeded Random generator which is thread safe across any number of threads
 * 
 * It's a bit of a hack but necessary for proper behavior across sub heirarchy of threads
 * 
 * @author Steve
 *
 */
public class SeededRandom 
{
	/*
	 * A ThreadLocal will hold a Singleton for a single thread only and will not return for other threads
	 */
	private static ThreadLocal<Random> inst;
	private static ThreadLocal<Long> threadSeed;
	
	/**
	 * Initialize the Threaded Random instance
	 */
	public static void init()
	{
		inst = new ThreadLocal<Random>() {
			protected Random initialValue() {
				return new Random();
			}
		};
		threadSeed = new ThreadLocal<Long>() {
			protected Long initialValue() {
				return -1l;
			}
		};
		//Give the Thread a chance to catch up to the ThreadLocal locks
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Must call init before calling this
	 * @param seed The Seed to set
	 */
	public static void setSeed(long seed)
	{
		threadSeed.set(seed);
		System.err.println(threadSeed.get() + "");
		inst.get().setSeed(threadSeed.get());
	}
	
	/**
	 * Increment an already set seed by 1
	 */
	public static void incrementSeed()
	{
		Long seed = threadSeed.get();
		setSeed(seed + 1234567890);
	}
	
	/**
	 * Must call init before calling this
	 * @return A seeded Random
	 */
	public static Double random()
	{
		return inst.get().nextDouble();
	}
}
