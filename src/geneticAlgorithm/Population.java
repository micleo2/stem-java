package geneticAlgorithm;

import java.util.ArrayList;

import training.BackPropagation;
import training.TrainingData;
import utility.GenUtil;


/**Pinnacle class of GA. Creates a pool of ANNs, which are then ready to be evolved, mutated, cross over'ed etc.
 * @author Michael Leon
 *
 */
public class Population {
	private NeuralNetworkWrapper[] population;
	private int size, generation;
	private double mutationRate, averageError, throttleRate;
	int maxIteration = 1;
	ArrayList<Double> generationalFitnesses;
	
	public Population(int popSize, double mutationRate, double throttleRate) {
		population = new NeuralNetworkWrapper[popSize];
		this.mutationRate = mutationRate;
		this.size = popSize;
		for (int i = 0; i < popSize; i++){
			population[i] = new NeuralNetworkWrapper(GenUtil.createNet());
		}
		generation = 1;
		this.throttleRate = throttleRate;
		generationalFitnesses = new ArrayList<Double>();
	}
	
	//LET ALL ANNs HAVE SOME TIME TO TRAIN, THEM PERFORM A DIAGNOSTIC TEST AND SET FITNESS LEVELS IN PROPORTION TO THAT
	public void setFitnessLevels(){
		BackPropagation bp = null;
		for (int i = 0; i < population.length; i++){
			System.out.println("\tI"  +i);
			NeuralNetworkWrapper nwp = population[i];
			bp = new BackPropagation(nwp.getSolution(), TrainingData.produceValidTrainingSet(nwp.solution, 1));
			bp.trainFor(maxIteration);
			bp.runTestDiagnostics(null, false);
			nwp.setAmountRight(bp.getSummaryStatistics().amountRight);
			nwp.setPercentageCorrect(bp.getSummaryStatistics().percentageRight);
			averageError += bp.getSummaryStatistics().totalError;
		}
		averageError /= population.length;
//		GRADE ALL ANNs ON A CURVE TO THE HIGHEST SCORE;
		NeuralNetworkWrapper fittest = getHighestPerformer();
		for (int j = 0; j < population.length; j++) {
			NeuralNetworkWrapper neuralNet = population[j];
			neuralNet.setFitness(gradeFitness(neuralNet, fittest));
		}
		generationalFitnesses.add(this.getHighestPerformer().getPercentageCorrect());
	}

	private double gradeFitness(NeuralNetworkWrapper current, NeuralNetworkWrapper great) {
		return ((double)current.getAmountRight() / great.getAmountRight()) * 0.8;
	}

	public void evolveNextGeneration() {
		generation++;
		NeuralNetworkWrapper[] newGen = new NeuralNetworkWrapper[size];
		for (int i = 0; i < newGen.length; i++){
			NeuralNetworkWrapper partnerA = Population.<NeuralNetworkWrapper>selectMate(population);
			NeuralNetworkWrapper partnerB = Population.<NeuralNetworkWrapper>selectMate(population);

			while (partnerA == partnerB){
				partnerB = selectMate(population);
			}

			NeuralNetworkWrapper child = new NeuralNetworkWrapper();
			child.setDna(partnerA.getDna().crossover(partnerB.getDna()));
			child.getDna().mutate(mutationRate);
			child.updatePhenotype();
			newGen[i] = child;
		}
		population = newGen;
	}

	public static <T extends Rankable> T selectMate(T[] population) {
		T choice = null;
		while (choice == null){
			int pos = (int)(Math.random() * population.length);
			if (Math.random() < population[pos].getFitness()){
				choice = population[pos];
			}
		}
		
		return choice;
	}

	public NeuralNetworkWrapper getFittest() {
		NeuralNetworkWrapper greatest = population[0];
		for (int i = 0; i < population.length; i++) {
			NeuralNetworkWrapper current = population[i];
			if (current.getFitness() > greatest.getFitness()){
				greatest = current;
			}
		}
		return greatest;
	}
	
	public NeuralNetworkWrapper getHighestPerformer(){
		NeuralNetworkWrapper greatest = population[0];
		for (int i = 0; i < population.length; i++) {
			NeuralNetworkWrapper current = population[i];
			if (current.getAmountRight() > greatest.getAmountRight()){
				greatest = current;
			}
		}
		return greatest;
	}

	public void printDiagnostics() {
		
	}

	public double averageError() {
		return averageError;
	}

	public int getSize() {
		return size;
	}

	public int getGeneration() {
		return generation;
	}

	public ArrayList<Double> getGenerationalFitnesses() {
		return generationalFitnesses;
	}
}
