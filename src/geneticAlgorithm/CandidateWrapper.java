package geneticAlgorithm;


public class CandidateWrapper<T> implements Rankable{
	@Override
	public String toString() {
		return "CandidateWrapper [solution=" + solution + ", fitness="
				+ fitness + ", totalError=" + totalError + ", dna=" + dna + "]";
	}

	protected T solution;
	protected double fitness, totalError;
	protected DNA dna;
	
	public void updatePhenotype(){};

	public T getSolution() {
		return solution;
	}

	public void setSolution(T solution) {
		this.solution = solution;
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public DNA getDna() {
		return dna;
	}

	public void setDna(DNA crossover){
		dna = crossover;
	}
}