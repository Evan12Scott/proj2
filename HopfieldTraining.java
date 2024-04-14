/*
Authors: Evan Scott, Kieran Kennedy, Sean Pala
Last Date Modified: 4/11/24
Description: HopfieldTraining handles the training for the discrete Hppfield net
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
			inputDimension = Integer.parseInt(reader.readLine());
			numImages = Integer.parseInt(reader.readLine());
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

		for(int i = 0; i < numImages; i++){
			SimpleEntry<int[], Integer> result = getInputArr();

        	int[] inputArr = result.getKey();
        	int numCols = result.getValue();
			int numRow = inputDimension/numCols;
			
			int[][] transpose = transposeMatrix(inputArr, numCols, numRow);

			if(flag){
				int numRows = transpose.length;
				weights = new int[numRows][numCols];
				flag = false;
			}
			else{
				updateWeightMatrix(transpose, inputArr, weights);
			}
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
			writer.write(inputDimension + "\n\n");
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
	private void updateWeightMatrix(int[][] matrix1, int[] matrix2, int[][] weights){
		int numRows = matrix1.length;
        int numCols = matrix2.length;

		for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                weights[i][j] = 0;
                for (int k = 0; k < matrix1[i].length; k++) {
                    weights[i][j] += matrix1[i][k] * matrix2[k];
                }
            }
        }
	}

	/*
	Description: transposes the matrix which is stored in an int[] by converting to int[][] then transpose
	PARAMS: numRows: int - number of columns test data matrix
			numCols: int - number of rows in test data matrix
			array: int[] - array containing the testing data which will be converted into matrix
	RETURN: int[][] - transposed matrix
	*/
	private int[][] transposeMatrix(int[] array, int numRows, int numCols){
		int[][] originalMatrix = new int[numRows][numCols];
        
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                originalMatrix[i][j] = array[i * numCols + j];
            }
        }

        int[][] transposedMatrix = new int[numCols][numRows];
        for (int i = 0; i < numCols; i++) {
            for (int j = 0; j < numRows; j++) {
                transposedMatrix[i][j] = originalMatrix[j][i];
            }
        }
        return transposedMatrix;
	}

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
				String[] inputs = currLine.split(" ");
				numCol = inputs.length;
				for(String input: inputs){
					inputArr[readIn] = Integer.parseInt(input);
					readIn++;
				}
			}
		}catch(Exception e){
			System.out.println("ERROR: " + e);
		}
		
		return new SimpleEntry<>(inputArr, numCol);
	}
}

