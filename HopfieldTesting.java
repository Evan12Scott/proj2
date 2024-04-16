/*
Authors: Evan Scott, Kieran Kennedy, Sean Pala
Last Date Modified: 4/14/24
Description: HopfieldTesting handles the testing for the Hopfield net
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Random;
import java.util.Arrays;

public class HopfieldTesting {
	String readWeightsFile, readDataFile, writeFile;
	BufferedReader reader;
	BufferedWriter writer;
	int inputDimension, numImages, numCols;
	double[][] weights = null;
	
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

		try{
			// get initial base values from testing file
			reader = new BufferedReader(new FileReader(readDataFile));
			String[] dimensionParse = reader.readLine().split("\\s+");
			inputDimension = Integer.parseInt(dimensionParse[0]);
			String[] numImageParse = reader.readLine().split("\\s+");
			numImages = Integer.parseInt(numImageParse[0]);
			writer = new BufferedWriter(new FileWriter(writeFile));

			//Read in weights
			BufferedReader weightsReader = new BufferedReader(new FileReader(readWeightsFile));
			weights = new double[inputDimension][inputDimension];
			for (int i = 0; i < inputDimension; i++) {
                String[] values = weightsReader.readLine().trim().split("\\s+");
				if(values.length != inputDimension){System.out.println("ERROR: IMPROPER SIZE");} //REMOVE LATER
                for (int j = 0; j < values.length; j++) {
                    weights[i][j] = Integer.parseInt(values[j]);
                }
            }
			weightsReader.close();

		}catch(Exception e){
			System.out.println("ERROR: " + e);
		}
	}
	
	/*
	Description: implements testing of the hopfield net. Calls a number of helper methods for one task per function methodology.
	PARAMS: None
	RETURN: None
	*/
	public void Test(){
		Random rand = new Random();

		//Loop through all image vectors	
		for(int i = 0; i < numImages; i++){
			int[] inputArr = getInputArr();
			int[] yArr = new int[inputArr.length];
			int epochsToConverge = 0;

			//Array of all possible indicies, to be used for random order
			int[] randomOrder = new int[inputArr.length];
			
			while(true){
				boolean change = false;
				epochsToConverge++;
				//Set y = x
				for(int j = 0; j < inputArr.length; j++){
					yArr[j] = inputArr[j];
					randomOrder[j] = j; //Sets each element equal to its index
				}
	
				//Randomize order of indicies (Ensures no index is repeated)
				randomize(randomOrder, rand);
	
				for(int randNum: randomOrder){
					int yIn = calcYin(inputArr, randNum, yArr);
					//Activation function
					if(yIn < 0){
						if(yArr[randNum] != -1){
							change = true;
						}
						yArr[randNum] = -1;
					}else if(yIn > 0){
						if(yArr[randNum] != 1){
							change = true;
						}
						yArr[randNum] = 1;
					}
				}
				
				//Check for convergence
				if(!change){
					writeToFile(yArr, i, epochsToConverge);
					break;
				}else{
					//Set x = y
					for(int j = 0; j < inputArr.length; j++){
						inputArr[j] = yArr[j];
					}
				}
			}
		}

		try{ //Close the file
			reader.close();
			writer.close();
		}catch(Exception e){
			System.out.println("ERROR: " + e);
		}

		System.out.println("\nTesting has finished. View the results of the hopfield net in the testResults subdirectory!\n");
		
	}

	/*
	Description: Writes the generated output to the given file
	PARAMS: int[] yArr - the generated ouput
			int epochsToConverge - number of iterations that was required for convergence
	RETURN: None
	*/
	private void writeToFile(int[] yArr, int curr, int epochsToConverge){
		try{
			writer.write("Image " + curr + " : Took " + epochsToConverge + " Epochs To Converge\n");
			String out = "";
			for(int i = 0; i < numCols; i++){
				for(int j = 0; j < numCols; j++){
					out += (yArr[i*numCols+j] == 1) ? "O" : " ";
				}
				writer.write(out);
				writer.write("\n");
				out = "";
			}
			writer.write("\n\n");
			writer.flush();
		}catch(Exception e){
			System.out.println("ERROR: " + e);
		}
	}


	/*
	Description: Randomizes the order of an array using the Fisher-Yates shuffle
	PARAMS: int[] inputArr - array conisiting of the input values
			int index - the current index
			int[] yArr - array containing the current y values
	RETURN: int yIn - the calculated yIn
	*/
	private int calcYin(int[] inputArr, int index, int[] yArr){
        int yIn = 0;
        yIn += inputArr[index];
        for(int i = 0; i < weights.length; i++){
            yIn += yArr[i] * weights[i][index];
        }
        return yIn;
    }


	/*
	Description: Randomizes the order of an array using the Fisher-Yates shuffle
	PARAMS: int[] randomOrder - array consisting of integers 1-inputSize
			Random rand - a Random object
	RETURN: None
	*/
    private void randomize(int[] randomOrder, Random rand){
        for(int i = randomOrder.length - 1; i > 0; i--){
            int j = rand.nextInt(i + 1);
            int temp = randomOrder[i];
            randomOrder[i] = randomOrder[j];
            randomOrder[j] = temp;
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
				numCols = inputs.length;
				for(int i = 0; i < inputs.length; i++) {
                	inputArr[readIn++] = (inputs[i] == 'O') ? 1 : -1; //Convert 'O' to 1,'' to 0 from image vector
            	}
			}
		}catch(Exception e){
			System.out.println("ERROR: " + e);
		}
		return inputArr;
	}

}