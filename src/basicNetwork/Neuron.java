package basicNetwork;
import java.util.ArrayList;

import transferFunctions.TransferFunction;


/**
 * Mathematical function conceived as a model of biological neurons; receives one or more inputs (representing dendrites) and sums them to produce an output (representing a neuron's axon)
 * @author Michael Leon
 * @see NeuralNetwork
 */
public class Neuron {
	private TransferFunction transferFunction;
	private ArrayList<Synapse> connections;
	private double output, errorSignal, sum;
	public static int ID = 0;
	public int instanceID;
	private double totalout, outnet;
	
	

	public Neuron(TransferFunction t){
		transferFunction = t;
	}
	
	/**
	 * Sums the weighted inputs, then passes the sum through the {@link #transferFunction} and sets that as the output
	 * @return the sum of weighted inputs passed through the {@link #transferFunction}
	 */
	public double fire(){
		double sum = 0;
		for (Synapse c : connections){
			sum += c.getWeightedInput();
		}
		this.sum = sum;
		output = transferFunction.calculateOutput(sum);
		return output;
	}
	
	
	/**
	 * Create a new {@link Synapse} between this neuron and the <code>receiver</code>
	 * @param receiver
	 */
	public void addConnection(Neuron receiver) {
		if (connections == null){connections = new ArrayList<Synapse>();}
		if (receiver.connections == null){receiver.connections = new ArrayList<Synapse>();}
		connections.add(new Synapse(this, receiver));
		receiver.connections.add(new Synapse(this, receiver));
	}
	
	public double getOutput() {
		return output;
	}

	public void setOutput(double output) {
		this.output = output;
	}

	/**
	 * @return the connections
	 */
	public ArrayList<Synapse> getConnections() {
		return connections;
	}

	/**
	 * @param connections the connections to set
	 */
	public void setConnections(ArrayList<Synapse> connections) {
		this.connections = connections;
	}

	public double getErrorSignal() {
		return errorSignal;
	}

	public void setErrorSignal(double errorSignal) {
		this.errorSignal = errorSignal;
	}

	public double getSum() {
		return sum;
	}

	public void setSum(double sum) {
		this.sum = sum;
	}
	
	public double getOutnet() {
		return outnet;
	}

	public void setOutnet(double outnet) {
		this.outnet = outnet;
	}

	public double getTotalout() {
		return totalout;
	}

	public void settotalout(double totalout) {
		this.totalout = totalout;
	}

	
}