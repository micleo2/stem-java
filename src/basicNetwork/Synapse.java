package basicNetwork;


/**
 * This class represents a connection between two neurons. There is a weight associated with every neuron, which multiplies the given input from neuron A and feeds that to neuron B.
 * @author Michael Leon
 * @version 1
 */
public class Synapse {
	private Neuron from, to;
	private double weight;
	
	
	public Synapse(Neuron a, Neuron b){
		from = a;
		to = b;
		weight = Math.random() * 2 - 1;
	}
	
	public Synapse(Neuron a, Neuron b, double w){
		from = a;
		to = b;
		weight = w;
	}
	
	public double getWeightedInput(){
		return from.getOutput() * weight;
	}

	public Neuron getFrom() {
		return from;
	}

	public void setFrom(Neuron from) {
		this.from = from;
	}

	public Neuron getTo() {
		return to;
	}

	public void setTo(Neuron to) {
		this.to = to;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}	
}
