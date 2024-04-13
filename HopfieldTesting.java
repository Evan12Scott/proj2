/*
Authors: Evan Scott, Kieran Kennedy, Sean Pala
Last Date Modified: 3/7/24
Description: PerceptronTraining handles the testing for the perceptron net
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class HopfieldTesting {
	String readWeightsFile, readDataFile, writeFile;
	BufferedReader reader;
	double theta;
	int inputSize, outputSize, numPairs;
	double[][] weights = null;
	double[] weightBias = null;	
	
	/*
	Description: constructor which gets the initial necessary values from param files reading
	PARAMS: readWeightsFile: String (filename storing trained weights)
		    readDataFile: String (filename storing the data to be tested on)
			writeFile: String (filename for results of testing to be written to)
	RETURN: None
	*/
	public HopfieldTesting(String readWeightsFile, String readDataFile, String writeFile) {
		this.readWeightsFile = readWeightsFile;
		this.readDataFile = readDataFile;
		this.writeFile = writeFile;

		// get initial base values from file
		try{
			reader = new BufferedReader(new FileReader(readWeightsFile));
			inputSize = Integer.parseInt(reader.readLine());
			outputSize = Integer.parseInt(reader.readLine());
			theta = Double.parseDouble(reader.readLine());

			reader.readLine();

			weights = new double[inputSize][outputSize];
			for (int i = 0; i < inputSize; i++) {
                String[] values = reader.readLine().trim().split("\\s+");
                for (int j = 0; j < outputSize; j++) {
                    weights[i][j] = Double.parseDouble(values[j]);
                }
            }
			reader.readLine();

			String[] biasValues = reader.readLine().trim().split("\\s+");
            weightBias = new double[outputSize];
            for (int i = 0; i < biasValues.length; i++) {
                weightBias[i] = Double.parseDouble(biasValues[i]);
            }

			reader.close();
			reader = new BufferedReader(new FileReader(readDataFile));
			// already have input/output dimensions saved so pass these in the data file
			reader.readLine();
			reader.readLine();
			numPairs = Integer.parseInt(reader.readLine());

		}catch(Exception e){
			System.out.println("ERROR: " + e);
		}
	}
	
	/*
	Description: implements testing of the perceptron net. Calls a number of helper methods for one task per function methodology.
	PARAMS: None
	RETURN: None
	*/
	public void Test(){
		int[] resultValues = new int[outputSize];
		String resultAnswer = "Undecided";
		String[] potentialAnswers = {"A", "B", "C", "D", "E", "J", "K"};
		
		int numCorrect = 0;
		for(int i = 0; i < numPairs; i++){
			int count1s = 0;
			int[] inputArr = getInputArr();
			int[] expectedValues = getExpectedValues();
			String expectedAnswer = getExpectedLetter();
			
			for(int j = 0; j < outputSize; j++){ 
				double yj = calcYj(weightBias[j], weights, inputArr, j);
				resultValues[j] = (int)yj;
			}
			for(int j = 0; j < outputSize; j++){
				if(resultValues[j] == 1){
					resultAnswer = potentialAnswers[j];
					count1s += 1;
				}
			}
			if(count1s == 0 || count1s > 1){
				resultAnswer = "Undecided";
			}
				
			writeToFile(resultValues, resultAnswer, expectedValues, expectedAnswer);
			if(resultAnswer.equals(expectedAnswer)){
				numCorrect += 1;
			}
		}

		try{ //Close the file
			reader.close();
		}catch(Exception e){
			System.out.println("ERROR1: " + e);
		}

		System.out.println("\nTesting has finished. View the results of the perceptron net in the testResults subdirectory!\n");

		try{
			double perCorrect = (numCorrect/(double)numPairs) * 100;
			BufferedWriter writer = new BufferedWriter(new FileWriter(writeFile, true));
			writer.write("Overall classification accuracy for the testing set: " + Math.round(perCorrect) + "%\n");
			writer.flush();
			writer.close();
		}catch(Exception e){
			System.out.println("ERROR: " + e);
		}
		
	}

	/*
	Description: writes the actual vs expected results out to specified file.
	PARAMS: resultValues: int[] (array storing the actual output values decided by net)
		    resultAnswer: String (actual character representation decided by net)
			expectedValues: int[] (array storing the expected output values found in data file)
			expectedAnswer: String (expected character representation)
	RETURN: None
	*/
	private void writeToFile(int[] resultValues, String resultAnswer, int[] expectedValues, String expectedAnswer) {
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter(writeFile, true));
			writer.write("Actual Output:\n");
			writer.write(resultAnswer + "\n");
			for(int i = 0; i < resultValues.length; i++){
				writer.write(resultValues[i] + " ");
			}
			writer.write("\n");
			writer.write("Expected Output:\n");
			writer.write(expectedAnswer + "\n");
			for(int i = 0; i < expectedValues.length; i++){
				writer.write(expectedValues[i] + " ");
			}
			writer.write("\n\n");
			
			writer.flush();
			writer.close();
		}catch(Exception e){
			System.out.println("ERROR2: " + e);
		}
	}

	/*
	Description: computes activation of each output unit.
	PARAMS: weightBias: double (bias value calculated from training)
		    weights: double[][] (2D array storing the trained weights)
			inputArr: int[] (array storing testing data)
			j: int
	RETURN: double - activation
	*/
	private double calcYj(double weightBias, double[][] weights, int[] inputArr, int j){
		double yIn = weightBias;
		for(int i = 0; i < inputSize; i++){
			yIn += inputArr[i] * weights[i][j];
		}

		if(yIn > theta){
			yIn = 1;
		}else {
			yIn = -1;
		}

		return yIn;
	}

	/*
	Description: retrieves the data in testing file line by line
	PARAMS: None
	RETURN: int[] - array containing testing data
	*/
	private int[] getInputArr(){
		int[] inputArr = new int[inputSize];
		try{
			int readIn = 0;
			reader.readLine(); //remove blank line
			while(readIn < inputSize){
				String currLine = reader.readLine();
				String[] inputs = currLine.split(" ");
				for(String input: inputs){
					inputArr[readIn] = Integer.parseInt(input);
					readIn++;
				}
			}
		}catch(Exception e){
			System.out.println("ERROR3: " + e);
		}
		
		return inputArr;
	}

	/*
	Description: retrieves the correct output unit values from data file
	PARAMS: None
	RETURN: int[] - array containing correct output units
	*/
	private int[] getExpectedValues(){
		int[] expected = new int[outputSize];
		try{
			reader.readLine(); // remove blank line
			String expectedStr = reader.readLine();
			String[] expectedArr = expectedStr.split(" ");
			for(int i = 0; i < outputSize; i++){
				expected[i] = Integer.parseInt(expectedArr[i]);
			}
		}catch(Exception e){
			System.out.println("ERROR4: " + e);
		}

		return expected;
	}

	/*
	Description: retrieves the correct letter representation from data file
	PARAMS: None
	RETURN: String - correct letter representation
	*/
	private String getExpectedLetter() {
		String expectedLetter = "";
		try {
			expectedLetter = reader.readLine();
		} catch(Exception e){
			System.out.println("ERROR5: " + e);
		}

		return expectedLetter;
	}
}

