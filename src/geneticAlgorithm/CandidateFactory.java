package geneticAlgorithm;

public class CandidateFactory {
	public static <T extends Rankable> CandidateWrapper<T> getCandidate(Class<T> cls){
		if (cls == NeuralNetworkWrapper.class){
			return (CandidateWrapper<T>) new NeuralNetworkWrapper();
		}
		return null;
	}
	
	public static <T extends Rankable> CandidateWrapper<T>[] getCandidateArray(Class<T> cls, int num){
		if (cls == NeuralNetworkWrapper.class){
			NeuralNetworkWrapper[] arr = new NeuralNetworkWrapper[num];
			for (int i = 0; i < num; i++){
				arr[i] = new NeuralNetworkWrapper();
			}
			                            
			return (CandidateWrapper<T>[]) arr;
		}
		return null;
	}
}
