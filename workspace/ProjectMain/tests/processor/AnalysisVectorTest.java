package processor;

import static org.junit.Assert.*;

import org.junit.Test;

import processor.exceptions.AnalysisVectorMismatchException;

/**
 * Test suite for AnalysisVector
 * @author Steve
 *
 */
public class AnalysisVectorTest 
{

	/**
	 * Test distance between two single point vectors
	 * @throws AnalysisVectorMismatchException
	 */
	@Test
	public void easySingleDistance() throws AnalysisVectorMismatchException
	{
		AnalysisVector one = new AnalysisVector();
		AnalysisVector two = new AnalysisVector();
		
		one.put("a", 1.0);
		two.put("a", 0.0);
		
		assertEquals(1.0, one.distance(two), 0.00000005);
	}
	
	/**
	 * Equal vectors
	 * @throws AnalysisVectorMismatchException
	 */
	@Test
	public void easyDoubleZeroDistance() throws AnalysisVectorMismatchException
	{
		AnalysisVector one = new AnalysisVector();
		AnalysisVector two = new AnalysisVector();
		
		one.put("a", 1.0);
		one.put("b", 2.0);
		two.put("a", 1.0);
		two.put("b", 2.0);
		
		assertEquals(0.0, one.distance(two), 0.00000005);
	}
	
	/**
	 * Three dimensional vector test
	 * @throws AnalysisVectorMismatchException
	 */
	@Test
	public void threeDistance() throws AnalysisVectorMismatchException
	{
		AnalysisVector one = new AnalysisVector();
		AnalysisVector two = new AnalysisVector();
		
		one.put("a", 1.0);
		one.put("b", 2.0);
		one.put("c", 3.0);
		
		two.put("a", 4.0);
		two.put("b", 5.0);
		two.put("c", 6.0);
		//from http://www.calculatorsoup.com/calculators/geometry-solids/distance-two-points.php
		double goal = 5.1961524227066;
		assertEquals(goal, one.distance(two), 0.00000005);
	}
	
	/**
	 * Vectors have wrong sizes, throw exception
	 * @throws AnalysisVectorMismatchException Expected
	 */
	@Test(expected= AnalysisVectorMismatchException.class)
	public void sizeMismatch() throws AnalysisVectorMismatchException
	{
		AnalysisVector one = new AnalysisVector();
		AnalysisVector two = new AnalysisVector();
		
		one.put("a", 1.0);
		one.put("b", 1.0);
		two.put("b", 0.0);
		
		one.distance(two);
	}
	
	/**
	 * Vectors have wrong keys, throw exception
	 * @throws AnalysisVectorMismatchException Expected
	 */
	@Test(expected= AnalysisVectorMismatchException.class)
	public void keyMismatch() throws AnalysisVectorMismatchException
	{
		AnalysisVector one = new AnalysisVector();
		AnalysisVector two = new AnalysisVector();
		
		one.put("a", 1.0);
		two.put("b", 0.0);
		
		one.distance(two);
	}
}
