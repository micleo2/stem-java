package training;

import java.util.Arrays;


/**
 * An element of {@link TrainingData}. It contains two arrays; one for the set of inputs and the other for ideal outputs
 * @author Michael Leon
 * @version 2
 */
public class DataElement {
	
	@Override
	public String toString() {
		return "DataElement [input=" + Arrays.toString(input)
				+ ", perfectOutput=" + Arrays.toString(perfectOutput) + "]";
	}
	private double[] input;
	private double[] perfectOutput;
	
	public DataElement(double[] i, double[] o){
		input = i;
		perfectOutput = o;
	}
	
	public double[] getInput() {
		return input;
	}
	public void setInput(double[] input) {
		this.input = input;
	}
	public double[] getPerfectOutput() {
		return perfectOutput;
	}
	public void setperfectOutput(double[] output) {
		this.perfectOutput = output;
	}
}
