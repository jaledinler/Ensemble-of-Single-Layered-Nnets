import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Lab1 {
	public static void main (String[] args) {
		if (args.length != 3) {
			System.err.println("Usage : " +
					"<trainSet> <tuneSet> <testSet>");
			System.exit(1);
		}
		
		List<Attribute> trainAttributes = new ArrayList<Attribute>();
		List<Instance> trainInstances = new ArrayList<Instance>();
		trainingFile(args[0], trainAttributes, trainInstances);
		Instances trainSet = new Instances(trainAttributes,trainInstances);
		
		List<Attribute> tuneAttributes = new ArrayList<Attribute>();
		List<Instance>tuneInstances = new ArrayList<Instance>();
		trainingFile(args[1], tuneAttributes, tuneInstances);
		Instances tuneSet = new Instances(tuneAttributes,tuneInstances);
		
		List<Attribute> testAttributes = new ArrayList<Attribute>();
		List<Instance> testInstances = new ArrayList<Instance>();
		trainingFile(args[2], testAttributes, testInstances);
		Instances testSet = new Instances(testAttributes,testInstances);
		
		Nnet nnet = new Nnet(trainSet,tuneSet,testSet);
		nnet.classify();
	}

	public static void trainingFile(String trainFile, List<Attribute> attributes, List<Instance>instances) {


		// Try creating a scanner to read the input file.
		Scanner fileScanner = null;
		try {
			fileScanner = new Scanner(new File(trainFile));
		} catch(FileNotFoundException e) {
			System.err.println("Could not find file '" +  trainFile + 
					"'.");
			System.exit(1);
		}

		// Iterate through each line in the file.
		int numOfAtts = -1;
		while(fileScanner.hasNext()) {
			String line = fileScanner.nextLine().trim();

			// Skip blank lines.
			if(line.length() == 0) {
				continue;
			}
			// Use another scanner to parse each word from the line
			Scanner lineScanner = new Scanner(line);
			if (!line.contains("//")) {
				numOfAtts = Integer.parseInt(lineScanner.next());
				break;
			}
			lineScanner.close();
		}
		String line = null;
		while(fileScanner.hasNext()) {
			line = fileScanner.nextLine().trim();
			if(line.length() == 0) {
				continue;
			}
			if (!line.contains("//")) {
				break;
			}			
		}
		int count = 0;
		//while(fileScanner.hasNext()) {
		while (count < numOfAtts) {
			if (line.contains("//") || line.length() == 0) {
				line = fileScanner.nextLine().trim();
				continue;
			}
			Scanner lineScanner = new Scanner(line);
			List<String> values = new ArrayList<String>();
			String attName = lineScanner.next();
			while (lineScanner.hasNext()) {
				String value = lineScanner.next();
				if(!value.equals("-")) {
					values.add(value);
				}				
			}
			Attribute att = new Attribute(attName,values);
			attributes.add(att);
			line = fileScanner.nextLine().trim();
			lineScanner.close();
			count++;
		}
		while(fileScanner.hasNext()) {
			if (line.contains("//") || line.length() == 0) {
				line = fileScanner.nextLine().trim();
				continue;
			}
			else
				break;
			//line = fileScanner.nextLine().trim();
		}
		List<String> values = new ArrayList<String>();
		while(fileScanner.hasNext()) {
			if (line.contains("//"))
				break;
			if(line.length() == 0) {
				line = fileScanner.nextLine().trim();
				continue;
			}
			Scanner lineScanner = new Scanner(line);
			String label = lineScanner.next();
			values.add(label);
			line = fileScanner.nextLine().trim();
			lineScanner.close();
		}
		Attribute label = new Attribute ("label", values);
		attributes.add(label);
		int numOfInstances = -1;
		while(fileScanner.hasNext()) {
			if(line.length() == 0) {
				line = fileScanner.nextLine().trim();
				continue;
			}
			Scanner lineScanner = new Scanner(line);
			if (!line.contains("//")) {
				numOfInstances = Integer.parseInt(lineScanner.next());
				break;
			}
			line = fileScanner.nextLine().trim();
			lineScanner.close();
		}
		int i = 0;
		while(fileScanner.hasNext() && i < numOfInstances) {
			line = fileScanner.nextLine().trim();
			if(line.length() == 0 || line.contains("//")) {
				continue;
			}
			Scanner lineScanner = new Scanner(line);
			List<String> features = new ArrayList<String>();
			String tmp = lineScanner.next();
			String instLabel = lineScanner.next();
			while (lineScanner.hasNext()) {
				features.add(lineScanner.next());
			}
			features.add(instLabel);
			Instance instance = new Instance(numOfAtts,features);
			instances.add(instance);
			i++;
			lineScanner.close();
		}
		fileScanner.close();

	}
	
}