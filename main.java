/*
Authors: Evan Scott, Kieran Kennedy, Sean Pala
Last Date Modified: 4/14/24
Description: Main method which abstracts the logic for the testing and training of hopfield net

To run the program: 
	javac validateInput.java testingInput.java trainingInput.java main.java HopfieldTesting.java  HopfieldTesting.java

	java main

*/

import java.util.Scanner;

public class main {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		String action, runAgain;

		// execute until user specifies program termination
		do {
			validateInput userInput = new validateInput();
			trainingInput train = new trainingInput();
			testingInput test = new testingInput();

			do {
				System.out.println("Enter 1 to train using a training data file, enter 2 to use a trained weight settings data file:");
				action = input.nextLine();
			} while (userInput.checkAction(action));
			
			// train from data file then prompt subsequent testing
			if(Integer.parseInt(action) == 1) {
				train.promptUser(input, action);
				test.promptUser(input, "2");
			}

			// test from data file with trained weights
			else {
				test.promptUser(input,action);
			}

			// program termination condition
			System.out.println("Do you want to run the program again? (yes/no):");
            runAgain = input.nextLine().toLowerCase();
		} while(runAgain.equals("yes"));
	}
}

