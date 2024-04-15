/*
Authors: Evan Scott, Kieran Kennedy, Sean Pala
Last Date Modified: 4/14/24
Description: HopfieldTraining handles the training for the discrete Hopfield net
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;

public class HopfieldTraining {
	int inputDimension, numImages;
	BufferedReader reader;
	String readFile, writeFile;
	
	/*
	Description: constructor which initializes and gets necessary starter values from param file reading
	PARAMS: readFile: String (filename storing data for training)
			writeFile: String (filename for trained weights to be written to after training)
	RETURN: None
	*/
	public HopfieldTraining(String readFile, String writeFile) {
		this.readFile = readFile;
		this.writeFile = writeFile;

		// get initial base values from file
		try{
			reader = new BufferedReader(new FileReader(readFile));
			String[] dimensionParse = reader.readLine().split("\\s+");
			inputDimension = Integer.parseInt(dimensionParse[0]);
			String[] numImageParse = reader.readLine().split("\\s+");
			numImages = Integer.parseInt(numImageParse[0]);
		}catch(Exception e){
			System.out.println("ERROR: " + e);
		}

	}

	/*
	Description: implements algorithm for training the Hopfield net. Calls a number of helper methods for one task per function methodology.
	PARAMS: None
	RETURN: None
	*/
	public void Train() {
		boolean flag = true;
		int[][] weights = null;

		// loop through training image vectors
		for(int i = 0; i < numImages; i++){

        	int[] inputArr = getInputArr();

			// initializes weight matrix because know appropriate dimensions with first image vector
			if(flag){
				weights = new int[inputArr.length][inputArr.length];
				flag = false;
			}

			updateWeightMatrix(inputArr, weights);
		}

		// close the file
		try{ 
			reader.close();
		}catch(Exception e){
			System.out.println("ERROR: " + e);
		}
		
		// zero out the diagonals in weight matrix
		zeroDiagonals(weights);

		// write weights out to provided file
		writeToFile(weights);
	}

	/*
	Description: writes the trained weights of Hopfield net out to specified file.
	PARAMS: weights: int[][] (2D array storing the trained weights)
	RETURN: None
	*/
	private void writeToFile(int[][] weights){
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter(writeFile));
			for(int i = 0; i < weights.length; i++){
				for(int j = 0; j < weights[i].length; j++){
					writer.write(weights[i][j] + " ");
				}
				if(i+1 != weights.length){
					writer.write("\n");
				}
			}
			writer.flush();
			writer.close();
		}catch(Exception e){
			System.out.println("ERROR: " + e);
		}
	}

	/*
	Description: updates the training weights by matrix multiplication
	PARAMS: weights: int[][] (2D array storing the trained weights)
			inputArr: int[] (1D array storing data to be transposed and matrix multiplied)
	RETURN: None
	*/
	private void updateWeightMatrix(int[] inputArr, int[][] weights){
		int dimensions = inputArr.length;

		for (int i = 0; i < dimensions; i++) {
        	for (int j = 0; j < dimensions; j++) {
            	weights[i][j] += inputArr[i] * inputArr[j];
        	}
    	}
	}

	/*
	Description: sets the diagonals of the weight matrix to 0
	PARAMS: weights: int[][] (2D array storing the trained weights)
	RETURN: NONE
	*/
	private void zeroDiagonals(int[][] weights){
		for(int i = 0; i < weights.length; i++){
			for(int j = 0; j < weights[i].length; j++){
				if(i == j){
					weights[i][j] = 0;
				}
			}
		}
	}

	/*
	Description: retrieves the data in testing file line by line
	PARAMS: None
	RETURN: int[] - array containing testing data
	*/
	private int[] getInputArr(){
		int[] inputArr = new int[inputDimension];

		try{
			int readIn = 0;
			reader.readLine(); //remove blank line
			while(readIn < inputDimension){
				String currLine = reader.readLine();
				char[] inputs = currLine.toCharArray();
				for(int i = 0; i < inputs.length; i++) {
                	inputArr[readIn++] = (inputs[i] == 'O') ? 1 : 0; //Convert 'O' to 1,'' to 0 from image vector
            	}
			}
		}catch(Exception e){
			System.out.println("ERROR: " + e);
		}
		
		return inputArr;
	}
}

