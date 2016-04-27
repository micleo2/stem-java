package geneticAlgorithm;

import basicNetwork.NeuralNetwork;

public class NeuralNetworkWrapper extends CandidateWrapper<NeuralNetwork>{

	private double totalError;
	private int amountRight;

	public NeuralNetworkWrapper(NeuralNetwork nn) {
		this.setSolution(nn);
		this.setDna(DNA.toDNA(nn));
	}

	public NeuralNetworkWrapper() {
		// TODO Auto-generated constructor stub
	}

	public void updatePhenotype() {
		setSolution(getDna().toANN());
	}

	public void setError(double totalError) {
		this.totalError = totalError;
	}
	
	public double getError(){
		return totalError;
	}

	public int getAmountRight() {
		return amountRight;
	}

	public void setAmountRight(int amountRight) {
		this.amountRight = amountRight;
	}
}