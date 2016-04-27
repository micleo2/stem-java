package transferFunctions;

/**
 * A transfer function is a function which every neuron must have. The neuron passes the sum of the weighted inputs through a tranfer function, and returns the output of the function
 * @author Michael Leon
 * @version 1
 */
public interface TransferFunction {
	
	/**
	 * A transfer function needs to be able to calculate an output
	 * @param input the sum of weighted inputs
	 * @return the output
	 */
	public double calculateOutput(double input);
	
	public static final TransferFunction SigmoidFunction = 
			x -> 1 / (1 + Math.pow(Math.E, -x));
			
	public static final TransferFunction DerivativeSigmiod = 
					x -> SigmoidFunction.calculateOutput(x) * (1 - SigmoidFunction.calculateOutput(x));
			
	public static final TransferFunction IdentityFunction =
			input -> input;
	
}
