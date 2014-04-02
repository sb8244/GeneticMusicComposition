package processor.exceptions;

/**
 * Exception for when a Beat's duration isn't in POSSIBLE_DURATIONS
 * @author Steve
 *
 */
public class BeatDurationOutOfBoundsException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3317054130961334173L;

	/**
	 * @param string The message of this exception
	 */
	public BeatDurationOutOfBoundsException(String string) {
		super(string);
	}
}
