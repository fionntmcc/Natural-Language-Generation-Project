/**
* @author Fionn McCarthy
* @version 19
* @since 19
* G00414386
* Data Structures and Algorithms Assignment
* Year 2
*/

package ie.atu.sw;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Primary running class.
 */

public class Runner {

	private static final int NUM_COMPARISONS = 50;
	private static Scanner kb = new Scanner(System.in);
	
	/**
	 * Calls menu().
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		menu();
	}
	
	/**
	 * 
	 * Displays menu console and allows user to choose methods.
	 * Continues running until user enters "?".
	 */
	public static void menu() {

		String outputFileName = "./out.txt";
		String embeddingsFileName = "./word-embeddings.txt";
		int numEntries = 10;
		Map<String, double[]> map = new HashMap<>();
		String choice;

		System.out.println(ConsoleColour.YELLOW_BOLD + "*******************************************************");
		System.out.println("*  " + ConsoleColour.WHITE_BOLD + "ATU - Dept. of Computer Science & Applied Physics" + ConsoleColour.YELLOW_BOLD + "  *");
		System.out.println("*                                                     *");
		System.out.println("*                   " + ConsoleColour.WHITE_BOLD + "Fionn McCarthy" + ConsoleColour.YELLOW_BOLD + "                    *");
		System.out.println("*                     " + ConsoleColour.WHITE_BOLD + "G00414386" + ConsoleColour.YELLOW_BOLD + "                       *");
		System.out.println("*                                                     *");
		System.out.println("*        " + ConsoleColour.WHITE_BOLD + "Similarity Search with Word Embeddings" + ConsoleColour.YELLOW_BOLD + "       *");
		System.out.println("*                                                     *");
		System.out.println("*******************************************************\n");

		do {

			System.out.println(ConsoleColour.RED_BOLD + "(1)" + ConsoleColour.WHITE_BOLD + " Specify Embedding File (current: " + embeddingsFileName + ")");
			System.out.println(ConsoleColour.RED_BOLD + "(2)" + ConsoleColour.WHITE_BOLD + " Specify an Output File (current: " + outputFileName + ")");
			System.out.println(ConsoleColour.RED_BOLD + "(3)" + ConsoleColour.WHITE_BOLD + " Calculate and print a word or phrase");
			System.out.println(ConsoleColour.RED_BOLD + "(4)" + ConsoleColour.WHITE_BOLD + " Enter number of outputs (current: " + numEntries + ")");
			System.out.println(ConsoleColour.RED_BOLD + "(?)" + ConsoleColour.WHITE_BOLD + " Quit\n");
			System.out.print("Select Option [1-?]> ");

			choice = kb.next();
			System.out.println();

			if (choice.equals("1")) {
				embeddingsFileName = getEmbeddingsFile();
				System.out.println("Embeddings file directory changed to " + embeddingsFileName + "\n\n");
				System.out.println("Intialising map...\n");
				map = getWordMap(embeddingsFileName);
			}

			else if (choice.equals("2")) {
				outputFileName = getOutputFile();
				System.out.println("Output file directory changed to " + outputFileName + "\n\n");
			}

			else if (choice.equals("3")) {
				// Prints similarWords on screen and to  specified file
				if (map.isEmpty()) {
					System.out.println("Initialising map...\n\n");
					map = getWordMap(embeddingsFileName);
				}
				if (!map.isEmpty()) {
					String similarWords = getSimilarWords(outputFileName, embeddingsFileName, numEntries, map);
					System.out.println("\n\n------Top " + numEntries + " Words ------\n\n");
					System.out.println(similarWords);
					writeToFile(outputFileName, similarWords);

				}
			}

			else if (choice.equals("4")) {
				if (map.isEmpty()) System.out.println("Map is currently empty.\nPlease select a valid embeddings file path before selecting number of outputs.\n\n");
				else {
					numEntries = getNumEntries(map.size());
					System.out.println("Number of output words changed to " + numEntries);
				}
			}

			else if (choice.equals("?")) {
				System.out.println("\nExiting program\n\n");
				System.exit(0);
			}

		} while(true);

	}
	
	public static Map<String, double[]> getWordMap(String embeddingsFileName){

		try {

			File wordFile = new File(embeddingsFileName);
			Scanner fileReader = new Scanner(wordFile);

			//Create HashMap
			Map<String, double[]> map = new HashMap<>();

			// Temporary storage variables
			String word;

			while (fileReader.hasNext()) {
				word = fileReader.next().replaceAll(",", "");

				double array[] = new double[NUM_COMPARISONS];
				for (int i = 0; i < NUM_COMPARISONS; i++) {
					array[i] = Double.parseDouble(fileReader.next().replaceAll(",", ""));
				}

				map.put(word, array);

			}
			fileReader.close();

			return map;
		}catch (Exception e) {
			System.out.println("There was an error with reading the word embeddings from the specified file");
			System.out.println("File name: " + embeddingsFileName);
			System.out.println("Error: " + e + "\n\n");
		}
		return new HashMap<String, double[]>();

	}

	/**
	 * Gets user input for words to use during calculation.
	 * Multiple words may be entered.
	 * Ensures that all words are valid before returning.
	 * @param map of words and embeddings.
	 * @return List<String> of words from user input.
	 */
	public static List<String> getUserWord(Map<String, double[]> map) {

		System.out.println("Please enter a word or short phrase: ");
		String userInput;
		List<String> userInputArray;
		boolean isAllWordsKeys;


		do {
			isAllWordsKeys = true;
			userInput = kb.nextLine().toLowerCase();
			userInputArray = new ArrayList<String>(Arrays.asList(userInput.split(" ")));

			// Checks if all words in array are in map
			for (int i = 0; i < userInputArray.size(); i++) {
				if (!map.containsKey(userInputArray.get(i))) { isAllWordsKeys = false; }
			}

		} while(!isAllWordsKeys);
		return (List<String>) userInputArray;
	}

	/**
	 * 
	 * Ensures that user value is a valid integer before returning.
	 * @return number of entries to be displayed on screen and to file.
	 */
	public static int getNumEntries(int mapSize) {
		String input;
		int num = 0;
		do {

			System.out.println("Please enter number of entries: ");
			input = kb.next();
			try {
				num = Integer.parseInt(input);
			} catch(NumberFormatException e) {

			}
		} while (num < 1 || num > mapSize);
		System.out.println("Number of entries changed to " + num);
		return num;

	}


	/**
	 * 
	 * Calculates and returns the Euclidean distance between 2 double arrays.
	 * @param double[] for w1 embedding.
	 * @param double[] for w2 embedding.
	 * @return Euclidean distance between 2 given arrays.
	 */
	public static double getEuclideanDistance(double[] w1, double[] w2) {
		if (w1.length != NUM_COMPARISONS && w2.length != NUM_COMPARISONS) { return 0; }

		double distance = 0;

		// calculates Euclidean distance
		for (int i = 0; i < NUM_COMPARISONS; i++) {
			distance += Math.pow(w1[i] - w2[i], 2);
		}
		distance = Math.sqrt(distance);

		return distance;

	}

	/**
	 * 
	 * Calculates and returns the Cosine distance between 2 double arrays.
	 * @param double[] for w1 embedding.
	 * @param double[] for w2 embedding.
	 * @return Cosine distance between 2 given arrays.
	 */
	public static double getCosineDistance(double[] w1, double[] w2) {
		if (w1.length != NUM_COMPARISONS && w2.length != NUM_COMPARISONS) { return 0; }

		double distance = getEuclideanDistance(w1, w2);
		double divider = 0;


		// calculates Euclidean distance
		for (int i = 0; i < NUM_COMPARISONS; i++) {
			divider += Math.pow(w1[i], 2) * Math.pow(w2[i], 2);
		}
		divider = Math.sqrt(divider);
		distance /= divider;

		return distance;

	}
	
	/**
	 * 
	 * Prints all words to file at specified file location.
	 * New file is created if file path does not exist
	 * @param outputFileName
	 * @param words
	 */
	public static void writeToFile(String outputFileName, String words) {
		// gets device's Admin path, irrespective of operating system

		try {
			File file = new File(outputFileName);
			FileWriter fileWriter = new FileWriter(file);

			fileWriter.write(words);
			fileWriter.close();
			System.out.println("Words printed to file " + file.getPath() + "\n\n");
		}
		catch(Exception e) {
			System.out.println("There was an issue printing to the file " + outputFileName);
			System.out.println("Error: " + e);
		}
	}
	
	/**
	 * Returns the user's choice of calculation type.
	 * @return <b>true</b> if user enters 1 for Euclidean calculation. Else returns <b>false</b>.
	 */
	public static boolean getCalculationChoice() {
		System.out.println("\nEnter 1 for Euclidean Distance");
		System.out.println("Enter 2 for Cosine Distance\n");
		String userInput;
		while (true) {
			userInput = kb.next();
			if (userInput.equals("1")) { return true; }
			if (userInput.equals("2")) { return false; }
		}
	}

	
	
	/**
	 * 
	 * Initializes <b>map</b> of word embeddings from specified file path.
	 * Calculates a double value to each word based on distance to user's input word.
	 * Orders a list of <b>ComparableWords</b> based on calculated double values.
	 * Returns a String of top 'n' words in ordered list.
	 * @param outputFileName
	 * @param embeddingsFileName
	 * @param numEntries
	 * @return top n most similar words to user's word input.
	 * @throws IOException 
	 */
	public static String getSimilarWords(String outputFileName, String embeddingsFileName, int numEntries, Map<String, double[]> map) {

		// Initialize map with all words and word embeddings - this takes about 5 seconds
		try {
			
			// Initialize list to order words with respect to closeness
			List<ComparableWord> topWords = new ArrayList<>();

			List<String> userWordArray = new ArrayList<>(getUserWord(map));
			boolean isEuclidean = getCalculationChoice();

			// Array for the average values of words entered
			double[] userInputValueArray = getUserInputValueArray(map, userWordArray);

			/*
			 * Loops through map.keySet() and calculates distance based on word embeddings
			 * Adds Comparable ComparableWord object to topWords Array
			 * For N words, this has an O(N) runtime
			 */
			topWords = getComparableWordList(map, userWordArray, userInputValueArray, isEuclidean);


			/* Sort topWords ArrayList. This uses merge sort
			 * Time complexity: O(N(log(n)))
			*/
			topWords.sort((o1, o2) -> o1.compareTo(o2));
			
			// Prints out top N elements in sorted ArrayList topWords
			return topWordsToString(numEntries, topWords);
		}
		catch (Exception e) {
			return "Error creating map.\n\n";
		}

	}
	
	/**
	 * 
	 * Returns average word embedding of all words entered by user
	 * This has an O(N) runtime with respect to the number of words entered by the user
	 * HashMaps display constant runtime with key-based operations such as searching by key
	 * @param map
	 * @param userWordArray
	 * @return
	 */
	public static double[] getUserInputValueArray(Map<String, double[]> map, List<String> userWordArray) {
		double[] userInputValueArray = new double[NUM_COMPARISONS];
		for (String userWord : userWordArray) {
			double[] wordValueArray = map.get(userWord);

			for (int i = 0; i < NUM_COMPARISONS; i++) {
				userInputValueArray[i] += wordValueArray[i];
			}
		}
		for (int i = 0; i < NUM_COMPARISONS; i++) {
			userInputValueArray[i] /= userWordArray.size();
		}
		return userInputValueArray;
	}

	/**
	 * 
	 * Iterates over every word in specified map and assigns a distance double value to each.
	 * Orders List of ComparableWord objects based on distance.
	 * Returns ordered list.
	 * @param map
	 * @param userWordArray
	 * @param userInputValueArray
	 * @param isEuclidean
	 * @return ordered List of ComparableWord objects.
	 */
	public static List<ComparableWord> getComparableWordList(Map<String, double[]> map, List<String> userWordArray, double[] userInputValueArray, boolean isEuclidean) {
		List<ComparableWord> topWords = new ArrayList<>();
		if (isEuclidean) {
			for (String word : map.keySet()) {
				// Make sure that any word entered by the user is not compared
				if (!userWordArray.contains(word)) {
					double currentWordArray[] = map.get(word);

					// Calculates distance
					topWords.add(new ComparableWord(word, getEuclideanDistance(userInputValueArray, currentWordArray)));

				}

			}
		}
		else {
			for (String word : map.keySet()) {
				// Make sure that any word entered by the user is not compared
				if (!userWordArray.contains(word)) {
					double currentWordArray[] = map.get(word);

					// Calculates distance
					topWords.add(new ComparableWord(word, getCosineDistance(userInputValueArray, currentWordArray)));
				}

			}
		}
		return topWords;
	}
	
	/**
	 * Prompts and returns user's embeddings file name specification.
	 * @return String embeddingsFileName based on user input.
	 */
	public static String getEmbeddingsFile() {
		System.out.println("Enter the name of the file to read embeddings from: ");
		return kb.next();
	}
	
	/**
	 * Prompts and returns user's output file name specification.
	 * @return String outputFileName based on user input.
	 */
	public static String getOutputFile() {
		System.out.println("Enter the name of the file to output to: ");
		return kb.next();
	}

	/**
	 * 
	 * Returns String representation of top 'n' words in given list.
	 * @param numEntries
	 * @param topWords
	 * @return string representation of top 'n' words in given list.
	 * @throws IOException
	 */
	public static String topWordsToString(int numEntries, List<ComparableWord> topWords) throws IOException {
		String fileWords = "";
		for (int i = 0; i < numEntries; i++) {
			String spaces = "                        ";
			spaces = spaces.substring(topWords.get(i).getWord().length() + String.valueOf(i).length(), spaces.length() - 1);
			fileWords += "(" + (i+1) + ") " + topWords.get(i).getWord() + spaces + "-     " + topWords.get(i).getValue() + "\n";
		}
		return fileWords;
	}

}
