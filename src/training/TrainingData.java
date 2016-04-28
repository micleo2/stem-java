package training;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import utility.GenUtil;
import basicNetwork.NeuralNetwork;

/**
 * Represents the data needed for a learning rule to train a neural network
 * @author Michael Leon
 * @version 1
 */
public class TrainingData implements Collection<DataElement>{
	private static final File file = new File("trimmed_only_goldsilver.txt");
	public static List<Double> goldValues = new ArrayList<Double>();
	public static List<Double> silverValues = new ArrayList<Double>();
	public static final Map<NeuralNetwork, TrainingData> trainingDataCache = new HashMap<NeuralNetwork, TrainingData>();

	static{
		int x = 1;
		Scanner sc = null;
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
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
		silverValues = GenUtil.onesAndZeroes(silverValues);
		goldValues = GenUtil.onesAndZeroes(goldValues);
	}

	@Override
	public String toString() {
		return "TrainingData [sets=" + sets.toString() + "] \n";
	}

	private ArrayList<DataElement> sets;

	/**
	 * Default constructor. Initializes the data elements
	 */
	public TrainingData(){
		sets = new ArrayList<DataElement>();
	}

	/**Gold values come first, then silver values come second(for the input of each {@link #DataElement}.
	 * @param nn
	 * @param windowSize
	 * @param timeStep
	 * @param silverValues
	 * @param goldValues
	 * @return
	 */
	public static TrainingData produceValidTrainingSet(NeuralNetwork nn, int timeStep){
		if (TrainingData.trainingDataCache.containsKey(nn)){
			System.out.println("CACHED VALUE RETRIEVED, COMPUTING RESULT AVOIDED...");
			return trainingDataCache.get(nn);
		}
		TrainingData td = new TrainingData();
		int inputSize = nn.getNumGoldDays() + nn.getNumSilverDays();
		int largerSize = Math.max(nn.getNumGoldDays(), nn.getNumSilverDays());
		for (int i = 0; i < silverValues.size() - largerSize; i++){
			double[] input = new double[inputSize];
			double[] output = new double[timeStep];
			int inputIndex = 0;
			for (int n = 0; n < nn.getNumGoldDays(); n++){
				input[inputIndex++] = goldValues.get(i + n);
			}
			for (int n = 0; n < nn.getNumSilverDays(); n++){
				input[inputIndex++] = silverValues.get(n + i);
			}
			for (int n = 0; n < timeStep; n++){
				output[n] = silverValues.get(nn.getNumSilverDays() + n + i);
			}
			td.add(input, output);
		}
		trainingDataCache.put(nn, td);
		return td;
	}

	public DataElement getElement(int index){
		return sets.get(index);
	}

	public boolean add(double[] input, double[] output){
		DataElement d = new DataElement(input, output);
		return add(d);
	}

	@Override
	public boolean add(DataElement arg0) {
		return sets.add(arg0);
	}

	@Override
	public boolean addAll(Collection<? extends DataElement> arg0) {
		return sets.addAll(arg0);
	}

	@Override
	public void clear() {
		sets.clear();
	}

	@Override
	public boolean contains(Object arg0) {
		return sets.contains(arg0);
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		return sets.containsAll(arg0);
	}

	@Override
	public boolean isEmpty() {
		return sets.isEmpty();
	}

	@Override
	public Iterator<DataElement> iterator() {
		return sets.iterator();
	}

	@Override
	public boolean remove(Object arg0) {
		return sets.remove(arg0);
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		return sets.removeAll(arg0);
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		return sets.retainAll(arg0);
	}

	@Override
	public int size() {
		return sets.size();
	}

	@Override
	public Object[] toArray() {
		return sets.toArray();
	}

	@Override
	public <T> T[] toArray(T[] arg0) {
		return sets.toArray(arg0);
	}
}