/*
Authors: Evan Scott, Kieran Kennedy, Sean Pala
Last Date Modified: 4/10/24
Description: validateInput ensures the inputs provided by user (passed from testingInput/trainingInput) are appropriate otherwise it will continue to prompt user(function will return true) until a valid entry is given 
*/

import java.io.*;

public class validateInput {
	private int userAction;
	private String file;

	/*
	Description: checks whether the action inputted by user is one of the valid options
	PARAMS: action: String
	RETURN: boolean - true: invalid so prompt user again
					  false: valid value entry
	*/
	public boolean checkAction(String action) {
		try {
			userAction = Integer.parseInt(action);
			if(userAction == 1 || userAction == 2){
				return false;
			}
			else {
				return true;
			}
		} catch(NumberFormatException e) {
			System.out.println("Invalid input. Please enter an integer.");
			return true;
		}	
	}

	/*
	Description: validates the ability to open data file for reading from training/testing subdir provided by user
	PARAMS: file: String (filename)
		    typeAct: int (determines whether the path is through training or testing subdirectory)
	RETURN: boolean - true: invalid so prompt user again
					  false: valid value entry
	*/
	public boolean checkReadFile(String file, int typeAct){
		try {
			String readFile = "";
                	if(typeAct == 1) {
                        	readFile = "./trainingSets/" + file;
                }
                	else {
                        	readFile = "./testingSets/" + file;
			}
			BufferedReader reader = new BufferedReader(new FileReader(readFile));
			return false;
		} catch (IOException e) {
            		System.err.println("An error occurred while reading the data file: " + e.getMessage());
			return true;
        	}
	}

	/*
	Description: validates the ability to open file for reading by user from the trainedWeights subdir 
	PARAMS: file: String (filename)
	RETURN: boolean - true: invalid so prompt user again
					  false: valid value entry
	*/
	public boolean checkReadWeightFile(String file){
		try {
			String readFile = "./trainedWeights/" + file;
			BufferedReader reader = new BufferedReader(new FileReader(readFile));
			return false;
		} catch (IOException e) {
            System.err.println("An error occurred while reading the trained weights file: " + e.getMessage());
			return true;
        	}
		}

	/*
	Description: validates the ability to write results to file specified by user in testResults subdir 
	PARAMS: file: String (filename)
	RETURN: boolean - true: invalid so prompt user again
					  false: valid value entry
	*/
	public boolean validateWriteTestResultFile(String file){
		String writeFile = "./testResults/" + file;
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(writeFile))) {
			return false;
		} catch (IOException e) {
			System.err.println("An error occurred while writing to the test file: " + e.getMessage());
			return true;
        	}
	}

	/*
	Description: validates the ability to write converged weights to file specified by user in trainedWeights subdir 
	PARAMS: file: String (filename)
	RETURN: boolean - true: invalid so prompt user again
					  false: valid value entry
	*/
	public boolean validateWriteWeightsFile(String file){
		String writeFile = "./trainedWeights/" + file;
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(writeFile))) {
			return false;
		} catch (IOException e) {
			System.err.println("An error occurred while writing to the test file: " + e.getMessage());
			return true;
        	}
	}
}

