/*
Authors: Evan Scott, Kieran Kennedy, Sean Pala
Last Date Modified: 4/14/24
Description: HopfieldTraining handles the training for the discrete Hopfield net
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.util.AbstractMap.SimpleEntry;

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
			SimpleEntry<int[], Integer> result = getInputArr();

        	int[] inputArr = result.getKey();
        	int numCols = result.getValue();
			int numRow = inputDimension/numCols;

			int[][] originalMatrix = convertArrToMatrix(inputArr, numRow, numCols);
			
			int[][] transpose = transposeMatrix(originalMatrix);

			// initializes weight matrix because know appropriate dimensions with first image vector
			if(flag){
				weights = new int[originalMatrix.length][transpose[0].length];
				flag = false;
			}

			updateWeightMatrix(originalMatrix, transpose, weights);
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
				writer.write("\n");
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
			matrix1: int[][] - transposed test data matrix
			matrix2: int[] - test data matrix
	RETURN: None
	*/
	private void updateWeightMatrix(int[][] origMatrix, int[][] transposeMatrix, int[][] weights){
		int numRowsOrig = origMatrix.length;
		int numColsOrig = origMatrix[0].length;
        int numRowsTranspose = transposeMatrix.length;

		for (int i = 0; i < numRowsOrig; i++) {
        	for (int j = 0; j < numRowsTranspose; j++) {
            	int sum = 0;
            	for (int k = 0; k < numColsOrig; k++) {
                	sum += origMatrix[i][k] * transposeMatrix[j][k];
            	}	
            	weights[i][j] = sum;
        	}
    	}
	}

	private int[][] convertArrToMatrix(int[] array, int numRows, int numCols) {
		int[][] originalMatrix = new int[numRows][numCols];
        
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                originalMatrix[i][j] = array[i * numCols + j];
            }
        }
		return originalMatrix;
	}

	/*
	Description: transposes the matrix which is stored in an int[] by converting to int[][] then transpose
	PARAMS: numRows: int - number of columns test data matrix
			numCols: int - number of rows in test data matrix
			array: int[] - array containing the testing data which will be converted into matrix
	RETURN: int[][] - transposed matrix
	*/
	private int[][] transposeMatrix(int[][] originalMatrix){
		int numRows = originalMatrix.length;
		int numCols = originalMatrix[0].length; 
        int[][] transposedMatrix = new int[numCols][numRows];
        for (int i = 0; i < numCols; i++) {
            for (int j = 0; j < numRows; j++) {
                transposedMatrix[i][j] = originalMatrix[j][i];
            }
        }
        return transposedMatrix;
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
	RETURN: int[] and Integer - array containing testing data and integer containing the number of columns for matrix
	*/
	private SimpleEntry<int[], Integer> getInputArr(){
		int[] inputArr = new int[inputDimension];
		int numCol = 0;
		try{
			int readIn = 0;
			reader.readLine(); //remove blank line
			while(readIn < inputDimension){
				String currLine = reader.readLine();
				char[] inputs = currLine.toCharArray();
				numCol = inputs.length;
				for(int i = 0; i < inputs.length; i++) {
                	inputArr[readIn++] = (inputs[i] == 'O') ? 1 : 0; //Convert 'O' to 1,'' to 0 from image vector
            	}
			}
		}catch(Exception e){
			System.out.println("ERROR: " + e);
		}
		
		return new SimpleEntry<>(inputArr, numCol);
	}
}

