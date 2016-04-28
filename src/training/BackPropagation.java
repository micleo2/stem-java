package training;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import transferFunctions.TransferFunction;
import utility.CanPrint;
import utility.GenUtil;
import basicNetwork.Layer;
import basicNetwork.NeuralNetwork;
import basicNetwork.Neuron;
import basicNetwork.Synapse;

/**
 * The simple class for back propagation
 * @author Michael Leon
 * @version 1
 */
public class BackPropagation {
	public class SummaryStatistics{
		public double totalError;
		public int epoch, amountRight;
		public double percentageRight;
	}
	SummaryStatistics statistics;
	private double learningRate;
	private NeuralNetwork nn;
	private TrainingData trainingData;
	public static int bestPerformer = 0;

	public BackPropagation(NeuralNetwork n, TrainingData td){
		nn = n;
		trainingData = td;
		learningRate = .25d;
		statistics = new SummaryStatistics();
	}

	public void step(){
		statistics.epoch++;
		for (DataElement d : trainingData){
			nn.setInput(d.getInput());
			nn.computeOutput();
			double[] idealOut = d.getPerfectOutput();
			double[] dataInput = d.getInput();
			ArrayList<Layer> layers = nn.getLayers();
			Layer lastLayer = nn.getLayers().get(layers.size() - 1);

			/*ERROR SIGNAL ASSIGNMENT; START*/
			for (int i = 0; i < lastLayer.size(); i++){
				Neuron n = lastLayer.getNeurons().get(i);
				n.setErrorSignal(idealOut[i] - n.getOutput());
			}
			for (int i = layers.size() - 2; i <= 0; i--){
				Layer l = layers.get(i);
				for (Neuron n : l){
					double sig = 0;
					for (int k = 0; k < n.getConnections().size(); k++){
						Synapse s = n.getConnections().get(k);
						sig += s.getTo().getErrorSignal() * s.getWeight();
					}
					n.setErrorSignal(sig);
				}
			}
			/*ERROR SIGNAL ASSIGNMENT; END*/
			/*WEIGHT MODIFICATION; START*/
			for (int i = 0; i < layers.get(0).size(); i++){
				Neuron ne = layers.get(0).getNeurons().get(i);
				for (Synapse s : ne.getConnections()){
					double delta = 
							learningRate * ne.getErrorSignal() * TransferFunction.DerivativeSigmiod.calculateOutput(ne.getSum()) * dataInput[i];
					s.setWeight(s.getWeight() + delta);
				}
			}
			for (int i = 1; i < layers.size(); i++){
				Layer l = layers.get(i);
				for (Neuron n : l){
					for (Synapse s : n.getConnections()){
						double delta = 
								learningRate * n.getErrorSignal() * TransferFunction.DerivativeSigmiod.calculateOutput(n.getSum()) * s.getFrom().getOutput();
						s.setWeight(s.getWeight() + delta);
					}
				}
			}
			/*WEIGHT MODIFICATION; END*/
		}
	}

	public void step(int dummy){
		statistics.epoch++;
		for (DataElement d : trainingData){
			nn.setInput(d.getInput());
			nn.computeOutput();
			double[] idealOut = d.getPerfectOutput();
			double[] dataInput = d.getInput();
			ArrayList<Layer> layers = nn.getLayers();
			Layer lastLayer = nn.getLayers().get(layers.size() - 1);
			double ERROR = 0;
			for (int i = 0; i < idealOut.length; i++) {
				double thisErrorSignal = ErrorFunction.SQUARED_ERROR_FUNCTION.apply(idealOut[i], lastLayer.getNeurons().get(i).getOutput());
				ERROR += thisErrorSignal;
			}
			//OUTPUT LAYER; START
			for (int i = 0; i < idealOut.length; i++) {
				double thisErrorSignal = ErrorFunction.SQUARED_ERROR_FUNCTION.apply(idealOut[i], lastLayer.getNeurons().get(i).getOutput());
				lastLayer.getNeurons().get(i).setErrorSignal(thisErrorSignal);
				double totalout = lastLayer.getNeurons().get(i).getOutput() - idealOut[i];
				double outnet = TransferFunction.DerivativeSigmiod.calculateOutput(lastLayer.getNeurons().get(i).getOutput());
				lastLayer.getNeurons().get(i).settotalout(totalout);
				lastLayer.getNeurons().get(i).setOutnet(outnet);
				for (int j = 0; j < lastLayer.getNeurons().get(i).getConnections().size(); j++){
					Synapse s = lastLayer.getNeurons().get(i).getConnections().get(j);
					s.getFrom().settotalout(s.getFrom().getTotalout() + totalout*outnet);
					double netw5 = s.getFrom().getOutput();
					s.setWeight(s.getWeight() - ((totalout * outnet * netw5) * learningRate));
				}
			}
			//OUTPUT LAYER; END
			//HIDDEN LAYER; START
			for (int i = layers.size() - 2; i >= 0; i--){
				Layer curL = layers.get(i);
				for (int n = 0; n < curL.size(); n++){
					Neuron curN = curL.getNeurons().get(n);

					double outnet = TransferFunction.DerivativeSigmiod.calculateOutput(curN.getOutput());
					for (int s = 0; s < curN.getConnections().size(); s++){
						Synapse curS = curN.getConnections().get(s);
						double netw1 = curS.getFrom().getSum();
						curS.setWeight(curS.getWeight() - (learningRate * (curN.getTotalout() * outnet * netw1)));
					}
				}
			}
		}
	}

	/**
	 * Execute {@link #step()} x number of times
	 * @param iterations the number of iterations
	 */
	public void trainFor(int iterations){
		for (int i = 0; i < iterations; i++){
			//System.out.println("\ti " + i);
			step();
		}
	}

	/**
	 * Same as {@link #trainFor(int)}, however, <code>callBack</code> is called in conjuction with every invocation to {@link #step()} (right after <code>step</code> is called)
	 * @param callBack
	 */
	public void trainFor(int iterations, Consumer<BackPropagation> callBack){
		for (int i = 0; i < iterations; i++){
			step();
			callBack.accept(this);
		}
	}

	public void runTestDiagnostics(CanPrint stream, boolean debug){
		if (debug){
			stream.println("EPOCH " + statistics.epoch);
			stream.println("PERFORMING TEST DIAGNOSTICS...");
		}
		statistics.amountRight = 0;
		double averageSquaredError = 0;
		for (DataElement d : trainingData){
			Double[] input = GenUtil.toDoubleArray(d.getInput());
			Double[] shouldBe = GenUtil.toDoubleArray(d.getPerfectOutput());
			Double[] actual = GenUtil.toDoubleArray(nn.compute(d.getInput())); 

			double err = 0;
			for (int i = 0; i < actual.length; i++){
				err += ErrorFunction.SQUARED_ERROR_FUNCTION.apply(shouldBe[i], actual[i]);
				if (actual[i] < 0.5 && shouldBe[i] == 0 || actual[i] >= 0.5 && actual[i] == 1){
					statistics.amountRight++;
					if (debug)
						stream.println("GOT IT RIGHT!!");
				}
			}
			if (debug){
				stream.println("\tFor input test case\n\t\t" + Arrays.deepToString(input));
				stream.println("\tThe ANN outputted\n\t\t" + Arrays.deepToString(actual));
				stream.println("\tThe ideal would be\n\t\t" + Arrays.deepToString(shouldBe));
				stream.println("\tSQUARED ERROR: " + err);
			}
			averageSquaredError += err;
		}
		if (debug)
			stream.println(statistics.amountRight  + " right out of " + trainingData.size());
		statistics.percentageRight = (double)statistics.amountRight / trainingData.size();
		if (statistics.amountRight > bestPerformer){
			bestPerformer = statistics.amountRight;
		}
		averageSquaredError /= trainingData.size();
	}


	public SummaryStatistics getSummaryStatistics() {
		return statistics;
	}

	public double getLearningRate() {
		return learningRate;
	}

	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}

	public TrainingData getTrainingData() {
		return trainingData;
	}

	public void setTrainingData(TrainingData trainingData) {
		this.trainingData = trainingData;
	}

	public void runTestDiagnosticss(boolean debug) {
		if (debug){
			System.out.println("EPOCH " + statistics.epoch);
			System.out.println("PERFORMING TEST DIAGNOSTICS...");
		}
		double averageSquaredError = 0;
		int testCase = 0;
		for (DataElement d : trainingData){
			testCase++;
			Double[] input = GenUtil.toDoubleArray(d.getInput());
			Double[] shouldBe = GenUtil.toDoubleArray(d.getPerfectOutput());
			Double[] actual = GenUtil.toDoubleArray(nn.compute(d.getInput())); 

			double err = IntStream
					.range(0, actual.length)
					.mapToDouble(i -> ErrorFunction.SQUARED_ERROR_FUNCTION.apply(shouldBe[i], actual[i]))
					.sum();
			if (debug){
				System.out.println("For input test case " + testCase);
				//				System.out.println("\tThe ANN outputted\n\t\t" + Arrays.deepToString(actual));
				//				System.out.println("\tThe ideal would be\n\t\t" + Arrays.deepToString(shouldBe));
				System.out.println("SQUARED ERROR: " + err);
			}
			averageSquaredError += err;
		}

		averageSquaredError /= trainingData.size();
		statistics.totalError = averageSquaredError;
	}
}