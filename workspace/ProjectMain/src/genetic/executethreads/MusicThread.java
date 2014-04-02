package genetic.executethreads;

import measure.Measure;

/**
 * Simple thread wrapper which provides a Measure[] data element
 * that can be set / get
 * 
 * @author Steve
 *
 */
public abstract class MusicThread extends Thread
{
	/**
	 * Needed to call super in children
	 * @param string
	 */
	public MusicThread(String string) {
		super(string);

		this.result = null;
	}

	protected Measure[] result;
	
	/**
	 * 
	 * @return The Measure[] produced by this thread. Only guaranteed after the thread has finished
	 */
	public Measure[] getResults()
	{
		return this.result;
	}
}
