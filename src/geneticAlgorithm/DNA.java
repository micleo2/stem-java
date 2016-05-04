package geneticAlgorithm;

import java.util.ArrayList;
import java.util.Arrays;

import utility.GenUtil;
import basicNetwork.Layer;
import basicNetwork.NeuralNetwork;

public class DNA {
	/**
	 * int[0] = days of silver
	 * int[1] = days of gold
	 * int[n + 2] = number of neurons in nth layer (counting from first hidden layer)
	 */
	int[] genes;

	public DNA(int[] genes) {
		this.genes = genes;
	}

	public static DNA toDNA(NeuralNetwork nn){
		ArrayList<Layer> nLayers = nn.getLayers();
		int[] str = new int[nLayers.size()+1];
		str[0] = nn.getNumSilverDays();
		str[1] = nn.getNumGoldDays();
		for (int i = 2; i < str.length; i++) {
			str[i] = nLayers.get(i-1).size();
		}
		return new DNA(str);
	}

	public static NeuralNetwork toANN(DNA strand){
		int inputSize = strand.genes[0] + strand.genes[1];
		NeuralNetwork nn = new NeuralNetwork(inputSize);
		nn.setNumSilverDays(strand.genes[0]);
		nn.setNumGoldDays(strand.genes[1]);
		for (int i = 1; i < strand.genes.length;i++){
			nn.addLayer(strand.genes[i], false);
		}
		return nn;
	}


	public DNA crossover(DNA partner) {
		ArrayList<Integer> genome = new ArrayList<>();
		int numLayers = Math.random() > 0.5? this.genes.length : partner.genes.length;
		for (int i = 0; i < numLayers; i++){
			DNA current = null;
			if (i > partner.genes.length){
				current = this;
			}else if (i > this.genes.length){
				current = partner;
			}else{
				current = Math.random() > 0.5? this : partner;
			}
			genome.add(current.genes[i]);
		}
		return new DNA(GenUtil.toIntArray(genome));
	}

	public String toString(){
		return Arrays.toString(genes);
	}

	public void mutate(double rate){
		if (Math.random() < rate){
			System.out.println("MUTATING...");
			int geneToChange = (int) (2 + (Math.random() * (genes.length-3)));
			int newVal = this.genes[geneToChange];
			newVal = (int) (newVal + Math.random() * 10.0);
			this.genes[geneToChange] = newVal;
		}
	}

	public NeuralNetwork toANN() {
		return DNA.toANN(this);
	}
}