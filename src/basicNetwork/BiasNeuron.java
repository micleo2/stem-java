package basicNetwork;

import transferFunctions.TransferFunction;

/**
 * A neuron which constantly emits a value of one. Adding a bias allows the output of the activation function to be shifted to the left or right on the x-axis
 * <p>
 * You would be wasting computation time and resources if you connect a bias neuron to the layer before it; those connections have no impact on its output
 * </p>
 * @author Michael Leon
 * @version 1
 */
public class BiasNeuron extends Neuron {

	public BiasNeuron(TransferFunction t) {
		super(t);
	}
	
	/**Always return one
	 * @return <code>1.0d</code>
	 **/
	@Override
	public double getOutput(){
		return 1;
	}

}
