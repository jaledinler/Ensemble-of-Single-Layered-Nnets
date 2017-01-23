import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Nnet {
	Instances trainSet;
	Instances tuneSet;
	Instances testSet;
	int labelIndex;
	double[][] weights;
	double biasWeight;
	double learningRate;
	int numsteps;
	double[][] bestWeights;
	double bestBiasWeight;
	public Nnet (Instances trainSet, Instances tuneSet, Instances testSet) {
		this.trainSet = trainSet;
		this.testSet = testSet;
		this.tuneSet = tuneSet;
		this.labelIndex = trainSet.numAttributes()-1;
		this.learningRate = 0.1;
		this.weights = new double[trainSet.numAttributes()-1][];
		this.bestWeights = new double[trainSet.numAttributes()-1][];
		for (int i = 0; i < trainSet.numAttributes()-1; i++) {
			Attribute att = trainSet.getAttribute(i);
			weights[i] = new double[att.getNumValues()];
			bestWeights[i] = new double[att.getNumValues()];
		}
		initializeWeights(-0.01,0.01);
	}
	public void classify() {
		HashMap<Integer,int[]> map = new HashMap<Integer,int[]>();
		Attribute classAtt = testSet.getAttribute(labelIndex);
		for (int n = 0; n < 10; n++) {
			List<Instance> baggingInstances = bagging();
			tune(baggingInstances);
			List<Instance> testInstances = testSet.getInstances();			
			for (int i = 0; i < testInstances.size(); i++) {
				Instance instance = testInstances.get(i);
				double sum = bestBiasWeight;
				for (int j = 0; j < testSet.numAttributes()-1; j++) {
					Attribute att = testSet.getAttribute(j);
					for (int k = 0; k < att.getNumValues(); k++) {
						if (instance.getValue(j).equals(att.getValue(k))) {
							sum += bestWeights[j][k];
						}
					}
				}
				double sigmoid = (double)1/(1+Math.exp(-sum));
				if (sigmoid < 0.5){
					if (map.containsKey(i)) {
						int[] arr = map.get(i);
						arr[0] = arr[0]+1;
					}
					else {
						int[] arr = new int[2];
						arr[0] = 1;
						map.put(i, arr);
					}
				}
				else {
					if (map.containsKey(i)) {
						int[] arr = map.get(i);
						arr[1] = arr[1]+1;
					}
					else {
						int[] arr = new int[2];
						arr[1] = 1;
						map.put(i, arr);
					}
				}
			}
		}
		int count = 0;
		for (int i = 0; i < testSet.numInstances(); i++) {
			int predictedLabel = 1;
			int[] arr = map.get(i);
			if (arr[0] > arr[1])
				predictedLabel = 0;
			if (classAtt.getValue(predictedLabel).equals(testSet.getInstance(i).getValue(labelIndex)))
				count++;
		}
		double accuracy = ((double) count/testSet.numInstances())*100;
		System.out.println("Accuracy : "+String.valueOf(accuracy)+"\t");
	}

	public List<Instance> bagging() {
		List<Instance> trainInstances = trainSet.getInstances();
		List<Instance> baggingInstances = new ArrayList<Instance>();
		int size = trainInstances.size();
		for (int i = 0; i < size-size/5; i++) {
			int index = (int)(Math.random() * (trainInstances.size()-1));
			baggingInstances.add(trainInstances.get(index));
		}
		return baggingInstances;
	}
	public void train(List<Instance> trainInstances) {
		for (int e = 0; e < 5; e++) {
			long seed = System.nanoTime();
			Collections.shuffle(trainInstances, new Random(seed));
			for (int i = 0; i < trainInstances.size(); i++) {
				Instance instance = trainInstances.get(i);
				double sum = biasWeight;
				for (int j = 0; j < trainSet.numAttributes()-1; j++) {
					Attribute att = trainSet.getAttributes().get(j);
					for (int k = 0; k < att.getNumValues(); k++) {
						if (instance.getValue(j).equals(att.getValue(k))) {
							sum += weights[j][k];
							break;
						}
					}
				}
				double sigmoid = (double)1/(1+Math.exp(-sum));
				String label = instance.getValue(labelIndex);
				Attribute att = trainSet.getAttributes().get(labelIndex);
				double y = 0;
				for (int k = 0; k < att.getNumValues(); k++) {
					if (label.equals(att.getValue(k))) {
						y = k;
						break;
					}
				}
				biasWeight = biasWeight- learningRate*(sigmoid-y);
				for (int m = 0; m < trainSet.numAttributes()-1; m++) {
					Attribute a = trainSet.getAttribute(m);
					for (int n = 0; n < a.getNumValues(); n++) {
						if(instance.getValue(m).equals(a.getValue(n))) {
							weights[m][n] = weights[m][n] - learningRate*(sigmoid-y);
						}
					}
				}
			}
		}
	}
	public void tune(List<Instance> trainInstances) {
		List<Instance> tuneInstances = tuneSet.getInstances();
		double error = Double.MAX_VALUE;
		double tuneError = 0;
		int e = 0;
		int steps = 0;
		int count = 0;
		Attribute classAtt = tuneSet.getAttribute(labelIndex);
		while (e < 100) {
			train(trainInstances);
			steps += 2;
			for (int i = 0; i < tuneInstances.size(); i++) {
				Instance instance = tuneInstances.get(i);
				double sum = biasWeight;
				for (int j = 0; j < trainSet.numAttributes()-1; j++) {
					Attribute att = trainSet.getAttributes().get(j);
					for (int k = 0; k < att.getNumValues(); k++) {
						if (instance.getValue(j).equals(att.getValue(k))) {
							sum += weights[j][k];
							break;
						}
					}
				}
				double sigmoid = (double)1/(1+Math.exp(-sum));
				int predictedLabel = 1;
				if (sigmoid < 0.5)
					predictedLabel = 0;
				if (!classAtt.getValue(predictedLabel).equals(instance.getValue(labelIndex)))
					count++;
			}
			tuneError = (double) count/tuneInstances.size();
			if (tuneError < error) {
				bestWeights = weights;
				bestBiasWeight = biasWeight;
				numsteps = steps;
				error = tuneError;
				e = 0;
			}
			else
				e++;
		}
	}
	public void initializeWeights(double min, double max) {
		biasWeight = (Math.random() * (max - min)) + min;
		for (int i = 0; i < weights.length; i++) {
			for (int j = 0; j < weights[i].length; j++) {
				weights[i][j] = (Math.random() * (max - min)) + min;
			}
		}
	}
}