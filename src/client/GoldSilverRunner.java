package client;

import geneticAlgorithm.Population;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import training.BackPropagation;
import training.TrainingData;
import basicNetwork.NeuralNetwork;

public class GoldSilverRunner {
	/**
	 * 1540 data points (each consists of two entities, so 3080 is how many data elements would be built into a training data set)
	 */
	public static final String SIX_YEARS = "only_goldsilver.txt";
	/**
	 * 750 data points (each consists of two entities, so 1500 is how many data elements would be built into a training data set)
	 */
	public static final String THREE_YEARS = "trimmed_only_goldsilver.txt";
	static int GA_ITERATIONS = 20;

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException{
		final PrintWriter writer = new PrintWriter("send_me_this_bro.txt", "UTF-8");
		File f = new File(THREE_YEARS);
		Population pop = new Population(5, 0.05, .01);
		writer.println(pop.getSize() + "-size population");
		writer.println("Spawning GA and ANNs...");

		System.out.println(pop.getSize() + "-size population");
		System.out.println("Spawning GA and ANNs...");

		for (int i = 0; i < GA_ITERATIONS; i++){
			System.out.println("setting fitness levels");
			pop.setFitnessLevels();
			System.out.println(pop.getGeneration() + " out of " + GA_ITERATIONS);
			System.out.println(pop.getGeneration() + "\t" + pop.getGenerationalFitnesses().get(pop.getGenerationalFitnesses().size()-1));
			writer.println(pop.getGeneration() + " out of " + GA_ITERATIONS);
			writer.println(pop.getGeneration() + "\t" + pop.getGenerationalFitnesses().get(pop.getGenerationalFitnesses().size()-1));
			pop.evolveNextGeneration();
		}
		pop.setFitnessLevels();
		NeuralNetwork overallFit = pop.getFittest().getSolution();
		BackPropagation bp = new BackPropagation(overallFit, TrainingData.produceValidTrainingSet(overallFit, 1));
		bp.trainFor(100);
		writer.println("best " + BackPropagation.bestPerformer);
		writer.close();
	}

	public static void writeOutputTo(BackPropagation backprop, String fileName) throws FileNotFoundException, UnsupportedEncodingException{
		final PrintWriter writer = new PrintWriter(fileName, "UTF-8");
		backprop.runTestDiagnostics((s) -> writer.println(s), true);
		writer.close();
	}

	/**
	 * @param file
	 * @param Should be even! windowSize is shared between the gold and silver slots. So, total size of each data element is windowSize, but each commodity takes up windowSize / 2
	 * @param timeStep How far ahead the ANN is expected to learn
	 * @return
	 * @throws FileNotFoundException
	 */
	public static TrainingData parseGoldAndSilver(File file, int windowSize, int timeStep) throws FileNotFoundException{
		TrainingData td = new TrainingData();
		Scanner sc = new Scanner(file);
		List<Double> goldValues = new ArrayList<Double>();
		List<Double> silverValues = new ArrayList<Double>();
		int x = 1;
		while (true){
			String line = null;
			try{
				line = sc.next();
			}catch(Exception e){
				break;
			}
			(x % 2 == 0 ? silverValues : goldValues).add(Double.valueOf(line));
			x++;
		}
		silverValues = onesAndZeroes(silverValues);
		goldValues = onesAndZeroes(goldValues);
		//		System.out.println(silverValues.size() + " " + goldValues.size());
		for (int i = 0; i < goldValues.size() - windowSize; i++){
			double[] input = new double[windowSize];
			double[] output = new double[timeStep];
			int inputIndex = 0;
			for (int n = 0; n < windowSize / 2; n++){
				input[inputIndex++] = goldValues.get(i + n);
			}
			for (int n = 0; n < windowSize / 2; n++){
				input[inputIndex++] = silverValues.get(n + i);
			}
			for (int n = 0; n < timeStep; n++){
				output[n] = silverValues.get(i + windowSize / 2 + n);
			}
			td.add(input, output);
		}
		sc.close();
		return td;
	}

	private static List<Double> onesAndZeroes(List<Double> dailyValues) {
		for (int i = 0; i < dailyValues.size()-1; i++){
			dailyValues.set(i, dailyValues.get(i + 1) > dailyValues.get(i) ? 0.0 : 1.0);
		}
		dailyValues.remove(dailyValues.size() - 1);
		return dailyValues;
	}

	public static List<Double> scaleDataFromLargest(List<Double> values){
		double largest = values
				.stream()
				.reduce((left, right) -> left > right? left : right)
				.orElse(-1.0);


		for (int i = 0; i < values.size(); i++) {
			values.set(i, values.get(i) / largest);
		}
		return values;
	}
	
	//.09626075083787594 is the largest percent increase 
	public static List<Double> scaleDataProportionally(List<Double> dailyValues){
		double largest = -1;
		for (int i = 0; i < dailyValues.size()-1; i++){
			double nextDay = dailyValues.get(i + 1);
			double ratio = dailyValues.get(i) / nextDay;
			ratio = (nextDay - dailyValues.get(i)) / dailyValues.get(i);
			if (ratio < 0){ratio = 0;}
			//			ratio = 
			dailyValues.set(i, ratio);
		}
		//		System.out.println("largest " + largest);
		dailyValues.remove(dailyValues.size() - 1);
		return dailyValues;	
	}
}
