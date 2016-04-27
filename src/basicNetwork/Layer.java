package basicNetwork;
import java.util.ArrayList;
import java.util.Iterator;

import transferFunctions.TransferFunction;

/**
 * Class that represents a layer of neurons
 * @author Michael Leon
 * @version 1
 */
public class Layer implements Iterable<Neuron> {
	private ArrayList<Neuron> neurons;

	/**
	 * Creates the input layer with the desired number of neurons. All the neurons in the input layer have their transfer function set
	 * @param neuronCount number of neurons in the input layer
	 * @param transferFunction the TF for all the neurons of the input layer
	 * @param bias 
	 */
	public Layer(int neuronCount, TransferFunction transferFunction, boolean bias){
		neurons = new ArrayList<Neuron>();
		for (int n = 0; n < neuronCount; n++){
			neurons.add(new Neuron(transferFunction));
		}
		if (bias)
			neurons.add(new BiasNeuron(transferFunction));
	}

	/**
	 * Calls {@link #Layer(int, TransferFunction)}; the equivalent of writing <blockquote> <code>new Layer(neuronCount, new SigmoidFunction())</code></blockquote>
	 * @param neuronCount the number of neurons for this layer
	 */
	public Layer(int neuronCount, boolean bias){
		this(neuronCount, TransferFunction.SigmoidFunction, bias);
	}

	/**
	 * Meshes this layer's outputs to the inputs of the next layer
	 * @param other the layer to feed this layer's outputs to
	 */
	public void setOutputTo(Layer other){
		for (Neuron thisN : this){
			for (Neuron thatN : other){
				thisN.addConnection(thatN);
			}
		}
	}

	/**
	 * Creates a primitive <code>double</code> 1D array of the output layer's {@link Neuron#getOutput()}
	 * @return 1D double array of all the output layer's neurons
	 */
	public double[] toArray(){
		double[] arr = new double[neurons.size()];
		for (int i = 0; i < neurons.size(); i++){
			arr[i] = neurons.get(i).getOutput();
		}
		return arr;
	}

	/**
	 * Calls the <code>size</code> method from an internal {@link ArrayList}
	 * @see java.util.ArrayList#size()
	 */
	public int size(){
		return neurons.size();
	}

	@Override
	public Iterator<Neuron> iterator() {
		return neurons.iterator();
	}

	/**
	 * Calls all the {@link Neuron#fire()} methods in all the neurons of this layer
	 */
	public void calculateOutput(){
		for (Neuron n : this){
			n.fire();
		}
	}

	public ArrayList<Neuron> getNeurons() {
		return neurons;
	}
}