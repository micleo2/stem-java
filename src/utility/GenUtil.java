package utility;

import java.util.ArrayList;
import java.util.List;

import training.TrainingData;
import basicNetwork.NeuralNetwork;


public class GenUtil {
	public static final CanPrint CONSOLE = (s) -> System.out.println(s);

	public static Double[] toDoubleArray(double[] array){
		Double[] doubleArr = new Double[array.length];
		for (int i = 0; i < array.length; i++){
			doubleArr[i] = array[i];
		}
		return doubleArr;
	}

	public static NeuralNetwork createNet(TrainingData td) {
		int inputSize = td.getElement(0).getInput().length;
		NeuralNetwork nn = new NeuralNetwork(inputSize);
//		nn.addLayer((int)(Math.random() * 70) + 50, false);
		nn.addLayer((int)(Math.random() * 30) + 10, false);
		nn.addLayer(td.getElement(0).getPerfectOutput().length, false);
		return nn;
	}
	
	public static Double[] tweak(Double[] arr){
		Double[] ret = arr.clone();
		for (int i = 0; i < ret.length; i++) {
			Double cur = ret[i];
			ret[i] =  cur * (1 + (-Math.random() / 30)) + Math.random() / 40;
		}
		return ret;
	}
	
	public static int[] toIntArray(ArrayList<Integer> list){
		int[] arr = new int[list.size()];
		
		for (int i = 0; i < arr.length; i++) {
			arr[i] = list.get(i);
		}
		
		return arr;
	}
	
	public static List<Double> onesAndZeroes(List<Double> dailyValues) {
		for (int i = 0; i < dailyValues.size()-1; i++){
			dailyValues.set(i, dailyValues.get(i + 1) > dailyValues.get(i) ? 1.0 : 0.0);
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
	
	@SuppressWarnings("unchecked")
	public static <T extends Comparable> T max(T a, T b){
		if (a.compareTo(b) < 0){
			return b;
		}else{
			return a;
		}
	}
}
