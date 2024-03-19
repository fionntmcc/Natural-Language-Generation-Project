package wordComparison;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import ie.atu.sw.ConsoleColour;


public class WordComparison {
	
	// private static final int MAP_CAPACITY = 50000;
	private static final int NUM_COMPARISONS = 50;
	private static Scanner kb = new Scanner(System.in);
	
	static long startTime = System.nanoTime();
	
	private static Map<String, double[]> getWordMap(String embeddingsFileName){
		
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
	
	// Prompts user for word
	private static List<String> getUserWord(Map<String, double[]> map) {
		
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
	
	private static int getNumEntries() {
		String input;
		int num = 0;
		do {
			
			System.out.println("Please enter number of entries: ");
			input = kb.next();
			try {
				num = Integer.parseInt(input);
			} catch(NumberFormatException e) {
				
			}
		} while (num < 1 || num > 50000);
		System.out.println("Number of entries changed to " + num);
		return num;
		
	}
	
	
	// returns Euclidean distance between 2 given arrays
	private static double getEuclideanDistance(double[] w1, double[] w2) {
		if (w1.length != NUM_COMPARISONS && w2.length != NUM_COMPARISONS) { return 0; }
			
		double distance = 0;
			
		// calculates Euclidean distance
		for (int i = 0; i < NUM_COMPARISONS; i++) {
			distance += Math.pow(w1[i] - w2[i], 2);
		}
		distance = Math.sqrt(distance);
			
		return distance;
	
	}
		
	// returns Cosine distance between 2 given arrays
	private static double getCosineDistance(double[] w1, double[] w2) {
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
	public static void writeToFile(String outputFileName, String words) {
		// gets device's Admin path, irrespective of operating system
		
		try {
			File file = new File(outputFileName);
			FileWriter fileWriter = new FileWriter(file);
			System.out.println(file.getPath());
			fileWriter.write(words);
			fileWriter.close();
		}
		catch(Exception e) {
			System.out.println("There was an issue printing to the file " + outputFileName);
			System.out.println("Error: " + e);
		}
	}
	
	private static boolean getCalculationChoice() {
		System.out.println("\nEnter 1 for Euclidean Distance");
		System.out.println("Enter 2 for Cosine Distance\n");
		String userInput;
		while (true) {
			userInput = kb.next();
			if (userInput.equals("1")) { return true; }
			if (userInput.equals("2")) { return false; }
		}
	}
	
	private static void menu() throws IOException {
		
		String outputFileName = "./out.txt";
		String embeddingsFileName = "./word-embeddings.txt";
		int numOutputWords = 10;
		String choice;
		
		System.out.println(ConsoleColour.YELLOW_BOLD + "*******************************************************");
		System.out.println("*  " + ConsoleColour.WHITE_BOLD + "ATU - Dept. of Computer Science & Applied Physics" + ConsoleColour.YELLOW_BOLD + "  *");
		System.out.println("*                                                     *");
		System.out.println("*                  " + ConsoleColour.WHITE_BOLD + "Fionn McCarthy" + ConsoleColour.YELLOW_BOLD + "                     *");
		System.out.println("*                    " + ConsoleColour.WHITE_BOLD + "G00414386" + ConsoleColour.YELLOW_BOLD + "                        *");
		System.out.println("*                                                     *");
		System.out.println("*        " + ConsoleColour.WHITE_BOLD + "Similarity Search with Word Embeddings" + ConsoleColour.YELLOW_BOLD + "       *");
		System.out.println("*                                                     *");
		System.out.println("*******************************************************\n");
		
		do {

			System.out.println(ConsoleColour.RED_BOLD + "(1)" + ConsoleColour.WHITE_BOLD + " Specify Embedding File (default: ./word-embeddings.txt)");
			System.out.println(ConsoleColour.RED_BOLD + "(2)" + ConsoleColour.WHITE_BOLD + " Specify an Output File (default: ./out.txt)");
			System.out.println(ConsoleColour.RED_BOLD + "(3)" + ConsoleColour.WHITE_BOLD + " Enter a word or phrase");
			System.out.println(ConsoleColour.RED_BOLD + "(4)" + ConsoleColour.WHITE_BOLD + " Enter number of outputs (default: 10)");
			System.out.println(ConsoleColour.RED_BOLD + "(?)" + ConsoleColour.WHITE_BOLD + " Quit");
			System.out.print("Select Option [1-?]> ");
			
			choice = kb.next();
			
			if (choice.equals("1"))
				embeddingsFileName = getEmbeddingsFile();
			
			else if (choice.equals("2"))
				outputFileName = getOutputFile();
			
			else if (choice.equals("3")) {
				String similarWords = getSimilarWords(outputFileName, embeddingsFileName, numOutputWords);
				System.out.println(similarWords);
				writeToFile(outputFileName, similarWords);
			}
			
			else if (choice.equals("4")) {
				numOutputWords = getNumEntries();
			}
				
			else if (choice.equals("?")) {
				System.out.println("\nExiting program\n\n");
				System.exit(0);
			}
				
		} while(true);

	}

	private static String getSimilarWords(String outputFileName, String embeddingsFileName, int numEntries) throws IOException {
		
		// Initialize map with all words and word embeddings
		HashMap<String, double[]> map = new HashMap<>(getWordMap(embeddingsFileName));
		
		if (map.isEmpty()) return "Error creating map.";
		
		// Initialize list to order words with respect to closeness
		List<OrderedWord> topWords = new ArrayList<>();
					
		List<String> userWordArray = new ArrayList<>(getUserWord(map)); 
		boolean isEuclidean = getCalculationChoice();
					
		// Array for the average values of words entered
		double[] userInputValueArray = getUserInputValueArray(map, userWordArray);
		
		
		/*
		 * Loops through map.keySet() and calculates distance based on word embeddings
		 * Adds Comparable OrderedWord object to topWords Array
		 * For N words, this has an O(N) runtime 
		 */
		topWords = getOrderedWordList(map, userWordArray, userInputValueArray, isEuclidean);
				
		
		/* Sort topWords ArrayList. This uses merge sort
		 * Time complexity: O(N(log(n)))
		*/
		topWords.sort((o1, o2) -> o1.compareTo(o2));
		
		// Prints out top N elements in sorted ArrayList topWords
		return displayTopWords(numEntries, topWords);
		
	}
	
	/*
	 * Returns average word embedding of all words entered by user
	 * This has an O(N) runtime with respect to the number of words entered by the user
	 */
	private static double[] getUserInputValueArray(Map<String, double[]> map, List<String> userWordArray) {
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

	private static List<OrderedWord> getOrderedWordList(Map<String, double[]> map, List<String> userWordArray, double[] userInputValueArray, boolean isEuclidean) {
		List<OrderedWord> topWords = new ArrayList<>();
		if (isEuclidean) {
			for (String word : map.keySet()) {
				// Make sure that any word entered by the user is not compared
				if (!userWordArray.contains(word)) {
					double currentWordArray[] = map.get(word);

					// Calculates distance
					topWords.add(new OrderedWord(word, getEuclideanDistance(userInputValueArray, currentWordArray)));
								
				}
							
			}
		}
		else {
			for (String word : map.keySet()) {
				// Make sure that any word entered by the user is not compared
				if (!userWordArray.contains(word)) {
					double currentWordArray[] = map.get(word);
					
					// Calculates distance
					topWords.add(new OrderedWord(word, getCosineDistance(userInputValueArray, currentWordArray)));		
				}
							
			}
		}
		return topWords;
	}

	private static String getEmbeddingsFile() {
		System.out.println("Enter the name of the file to read embeddings from: ");
		return kb.next();
	}

	private static String getOutputFile() {
		System.out.println("Enter the name of the file to output to: ");
		return kb.next();
	}

	
	// Displays n number of words on screen for the user, then writes words to specified file
	public static String displayTopWords(int numEntries, List<OrderedWord> topWords) throws IOException {
		String fileWords = "\n\n------Top " + numEntries + " Words ------\n\n";
		for (int i = 0; i < numEntries; i++) {
			String spaces = "                        ";
			spaces = spaces.substring(topWords.get(i).getWord().length() + String.valueOf(i).length(), spaces.length() - 1);
			fileWords += "(" + (i+1) + ") " + topWords.get(i).getWord() + spaces + "-     " + topWords.get(i).getValue() + "\n";
		}
		return fileWords;
	}
	
	
	public static void main(String[] args) throws IOException {
		
		menu();
		
	}

}
