package client;

import training.TrainingData;
import basicNetwork.NeuralNetwork;


public class SpecificDataRunner {
	public static void main(String[] args) {
		int gDays = 3;
		int sDays = 4;
		NeuralNetwork nn = new NeuralNetwork(gDays + sDays);
		nn.setNumSilverDays(sDays);
		nn.setNumGoldDays(gDays);
		NeuralNetwork other = new NeuralNetwork(gDays + sDays);
		other.setNumSilverDays(sDays);
		other.setNumGoldDays(gDays);
		TrainingData otherData = TrainingData.produceValidTrainingSet(other, 1);
		TrainingData td = TrainingData.produceValidTrainingSet(nn, 1);
	}
}