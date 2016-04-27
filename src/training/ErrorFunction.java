package training;

/**
 * @author Michael Leon
 *
 */
public class ErrorFunction {
	interface ErrorFunc{
		public double apply(double ideal, double actual);
	}
	
	public static ErrorFunc SQUARED_ERROR_FUNCTION = (ideal, actual) -> 0.5 * Math.pow(ideal - actual, 2);
}