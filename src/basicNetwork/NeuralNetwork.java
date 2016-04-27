package basicNetwork;
import java.util.ArrayList;


/**
 * The basic feed forward neural network class. Consists of layers of nodes which are all connected together, with information flowing in only one direction 
 * @author Michael Leon
 * @version 1
 * @see Layer
 * @see Neuron
 */
public class NeuralNetwork {
	private ArrayList<Layer> layers;
	private int numGoldDays, numSilverDays;
	

	/**
	 * Creates a network with one layer (the input layer) with "n" number of neurons
	 * @param inputLayerSize number of neurons to construct the input layer with
	 */
	public NeuralNetwork(int inputLayerSize){
		layers = new ArrayList<Layer>();
		layers.add(new Layer(inputLayerSize, false));
	}

	/**
	 * Add another layer into the network with the specified number number of neurons
	 * @param neuronCount amount of neurons to be created in the new layer
	 */
	public void addLayer(int neuronCount, boolean bias){
		Layer newLayer = new Layer(neuronCount, bias);
		layers.add(newLayer);
		layers.get(layers.size() - 2).setOutputTo(newLayer);
	}
	
	/**
	 * Get the output layer's value set from the last invocation of {@link #compute(double[])} or {@link #computeOutput()}
	 * @return the output layer vector
	 */
	public double[] getOutput(){
		return layers.get(layers.size() - 1).toArray();
	}
	
	/**
	 * Runs the last loaded input layer through the network and computes the result
	 * @return the vector for the output layer
	 */
	public double[] computeOutput(){
		for (int i = 1; i < layers.size(); i++){
			layers.get(i).calculateOutput();
		}
//		System.out.println(Arrays.deepToString(GenUtil.toDoubleArray(layers.get(layers.size() - 1).toArray())));
		return layers.get(layers.size() - 1).toArray();
	}
	
	
	/**
	 * Sets the input layer with the specified array and then feeds it throught the whole network
	 * @param input the values to set the input layer to
	 * @return the vector for the output layer
	 */
	public double[] compute(double[] input){
		setInput(input);
		for (int i = 1; i < layers.size(); i++){
			layers.get(i).calculateOutput();
		}
		return layers.get(layers.size() - 1).toArray();
	}

	
	/**
	 * Sets the input layer's neurons to the array given
	 * @param input the values for the input layer
	 */
	public void setInput(double[] input){
		if (input.length != layers.get(0).size()){
			throw new RuntimeException("Input array doesn't match input layer");
		}
		Layer inputLayer = layers.get(0);

		for (int i = 0; i < input.length; i++){
			inputLayer.getNeurons().get(i).setOutput(input[i]);
			inputLayer.getNeurons().get(i).setSum(input[i]);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(layers.size() + "-layer network");
		return sb.toString();
	}
	
	/**
	 * A more computationally heavy-duty {@link #toString()} method than its counter-part. Returns the entire weight matrix of the network; separates it by layer, then by neuron, then by order.
	 * @return the entire weight matrix of the network
	 */
	public String toDeepString(){
		StringBuilder sb = new StringBuilder();
		sb.append(layers.size() + "-layer network");
		for (int l = 0; l < layers.size(); l++){
			sb.append("{");
			for (int n = 0; n < layers.get(l).getNeurons().size(); n++){
				sb.append("(");
				for (int s = 0; s < layers.get(l).getNeurons().get(n).getConnections().size(); s++){
					sb.append("[");
					sb.append(layers.get(l).getNeurons().get(n).getConnections().get(s).getWeight());
					sb.append("]");
				}
				sb.append(")");
			}
			sb.append("}");
		}
		return sb.toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + numGoldDays;
		result = prime * result + numSilverDays;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NeuralNetwork other = (NeuralNetwork) obj;
		if (numGoldDays != other.numGoldDays)
			return false;
		if (numSilverDays != other.numSilverDays)
			return false;
		return true;
	}

	public ArrayList<Layer> getLayers() {
		return layers;
	}

	public void setLayers(ArrayList<Layer> layers) {
		this.layers = layers;
	}

	public int getNumGoldDays() {
		return numGoldDays;
	}

	public void setNumGoldDays(int numGoldDays) {
		this.numGoldDays = numGoldDays;
	}

	public int getNumSilverDays() {
		return numSilverDays;
	}

	public void setNumSilverDays(int numSilverDays) {
		this.numSilverDays = numSilverDays;
	}
}
