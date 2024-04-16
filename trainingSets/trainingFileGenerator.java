import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Scanner;
import java.lang.Math;

public class trainingFileGenerator {
    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);
        System.out.println("Please enter dimensions followed by a space followed by number of patterns stored");
        String input = kb.nextLine();
        String params [] = input.split(" ");
        
        int dimensions = Integer.parseInt(params[0]);
        double mDimensions = Double.parseDouble(params[0]);
        int patternNumber = Integer.parseInt(params[1]);
        String fileName = "trainingFile" + dimensions + "-" + patternNumber+".txt";

        String testing10 = "../testingSets/testingFile" + dimensions + "-" + patternNumber + "-10"+".txt";
        String testing25 = "../testingSets/testingFile" + dimensions + "-" + patternNumber + "-25"+".txt";
        String testing50 = "../testingSets/testingFile" + dimensions + "-" + patternNumber + "-50"+".txt";
        String testing75 = "../testingSets/testingFile" + dimensions + "-" + patternNumber + "-75"+".txt";


        // Set to store unique matrices
        Set<String> uniqueMatrices = new HashSet<>();
        
        // Random object for generating 'O' or ' '
        Random random = new Random();
        double dimension = Math.sqrt((double)(mDimensions));
        // Generate matrices until we have 30 unique ones
        while (uniqueMatrices.size() < patternNumber) {
            StringBuilder matrix = new StringBuilder();
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    // Append 'O' or ' ' randomly
                    matrix.append(random.nextBoolean() ? 'O' : ' ');
                }
                matrix.append("\n"); // New line after each row
            }
            uniqueMatrices.add(matrix.toString());
        }

        // Writing the matrices to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(dimensions + " (dimension of the image vectors)\n"+patternNumber+" (number of the image vectors)\n\n");
            for (String matrix : uniqueMatrices) {
                writer.write(matrix);
                writer.write("\n"); // Extra newline to separate matrices
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testing10))) {
            writer.write(dimensions + " (dimension of the image vectors)\n"+patternNumber+" (number of the image vectors)\n\n");
            for (String matrix : uniqueMatrices) {
                char[] chars = matrix.toCharArray();
                int numChanges = (int) (mDimensions*0.1);
                for (int i = 0; i < numChanges; i++) {
                    int index;
                    do {
                    index = random.nextInt(chars.length);
                } while (chars[index] == '\n'); // Ensure we don't change newline characters

            // Flip 'O' to ' ' and vice versa
            chars[index] = chars[index] == 'O' ? ' ' : 'O';
        }

                writer.write(chars);
                writer.write("\n"); // Extra newline to separate matrices
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }


            try (BufferedWriter writer = new BufferedWriter(new FileWriter(testing25))) {
            writer.write(dimensions + " (dimension of the image vectors)\n"+patternNumber+" (number of the image vectors)\n\n");
            for (String matrix : uniqueMatrices) {
                char[] chars = matrix.toCharArray();
                int numChanges = (int) (mDimensions*0.25);
                for (int i = 0; i < numChanges; i++) {
                    int index;
                    do {
                    index = random.nextInt(chars.length);
                } while (chars[index] == '\n'); // Ensure we don't change newline characters

            // Flip 'O' to ' ' and vice versa
            chars[index] = chars[index] == 'O' ? ' ' : 'O';
        }

                writer.write(chars);
                writer.write("\n"); // Extra newline to separate matrices
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(testing50))) {
            writer.write(dimensions + " (dimension of the image vectors)\n"+patternNumber+" (number of the image vectors)\n\n");
            for (String matrix : uniqueMatrices) {
                char[] chars = matrix.toCharArray();
                int numChanges = (int) (mDimensions*0.5);
                for (int i = 0; i < numChanges; i++) {
                    int index;
                    do {
                    index = random.nextInt(chars.length);
                } while (chars[index] == '\n'); // Ensure we don't change newline characters

            // Flip 'O' to ' ' and vice versa
            chars[index] = chars[index] == 'O' ? ' ' : 'O';
        }

                writer.write(chars);
                writer.write("\n"); // Extra newline to separate matrices
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(testing75))) {
            writer.write(dimensions + " (dimension of the image vectors)\n"+patternNumber+" (number of the image vectors)\n\n");
            for (String matrix : uniqueMatrices) {
                char[] chars = matrix.toCharArray();
                int numChanges = (int) (mDimensions*0.75);
                for (int i = 0; i < numChanges; i++) {
                    int index;
                    do {
                    index = random.nextInt(chars.length);
                } while (chars[index] == '\n'); // Ensure we don't change newline characters

            // Flip 'O' to ' ' and vice versa
            chars[index] = chars[index] == 'O' ? ' ' : 'O';
        }

                writer.write(chars);
                writer.write("\n"); // Extra newline to separate matrices
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }

        
    }
}