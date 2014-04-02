package processor.exceptions;

/**
 * Exception for errors in AnalysisVectorDistance
 * @author Steve
 *
 */
public class AnalysisVectorMismatchException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 545074162335262559L;

	/**
	 * @param string The message of this exception
	 */
	public AnalysisVectorMismatchException(String string) {
		super(string);
	}
}
