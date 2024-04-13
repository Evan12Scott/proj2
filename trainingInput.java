/*
Authors: Evan Scott, Kieran Kennedy, Sean Pala
Last Date Modified: 4/10/24
Description: trainingInput handles the retrieval of all necessary input from user that is required for training the hopfield net. Makes use of functions located in validateInput.java to ensure the validity of the input. 
*/

import java.util.Scanner;

public class trainingInput {
    validateInput userInput = new validateInput();
	String readFile, writeFile;
	
        /*
	Description: prompts user for all necessary information to train the hopfield net
	PARAMS: input: Scanner object
	 	action: String convert to int
	RETURN: None
	*/
	public void promptUser(Scanner input, String action) {
		int typeAction = Integer.parseInt(action);

		do {
			System.out.println("Enter the training data file name and ensure it is located in the TRAININGSETS subdirectory:");
                        readFile = input.nextLine();
		} while(userInput.checkReadFile(readFile, typeAction));

                do {
                        System.out.println("Enter a file name to save the trained weight settings:");
                        writeFile = input.nextLine();
                        } while(userInput.validateWriteWeightsFile(writeFile));
                
                // add appropriate paths to the files provided
                String readDataFile = "./trainingSets/" + readFile;
                String writeWeightFile = "./trainedWeights/" + writeFile;

                // all necessary inputs have been acquired so begin training of hopfield net
		HopfieldTraining hopfieldTrain = new HopfieldTraining(readDataFile, writeWeightFile);
		hopfieldTrain.Train();
	}
}


