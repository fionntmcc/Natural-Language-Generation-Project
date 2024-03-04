package wordComparison;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class WordComparison {
	
	private static final int NUM_COMPARISONS = 50;
	private static Scanner kb = new Scanner(System.in);
	
	static long startTime = System.nanoTime();
	
	static Map<String, double[]> getWords() throws FileNotFoundException {
		
		File wordFile = new File("./word-embeddings.txt");
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
		System.out.println("Time taken to instantiate Map: " + (System.nanoTime() - startTime) + "ns\n");
		
		return map;
	}
	
	// Prompts user for word
	private static String[] getUserWord(Map<String, double[]> map) {
		
		System.out.println("Please enter a word or short phrase: ");
		String userInput;
		String[] userInputArray;
		boolean isAllWordsKeys;
		
		
		do {
			isAllWordsKeys = true;
			userInput = kb.nextLine().toLowerCase();
			userInputArray = userInput.split(" ");
			
			/* For testing purposes
			System.out.println(userInput);
			for (int i = 0; i < userInputArray.length; i++) {
				System.out.println(userInputArray[i]);
			}
			*/
			
			// Checks if all words in array are in map
			for (int i = 0; i < userInputArray.length; i++) {
				if (!map.containsKey(userInputArray[i])) { isAllWordsKeys = false; }
			}
			
		} while(!isAllWordsKeys);
		return userInputArray;
	}
	
	private static int getNumEntries(int size) {
		int input = 0;
		do {
			try {
				System.out.println("Please enter number of entries: ");
				input = kb.nextInt();
			} catch (Exception InputMismatchException) {
				System.out.println("Not a valid input\n");
				input = 0;
			}
		} while(input < 0 || input >= size);
		return input;
		
	}
	
	private static void menu() throws FileNotFoundException {
		System.out.println("\n-------------- Word Similarity Lookup --------------\n");
		
		HashMap<String, double[]> map = new HashMap<>(getWords());

		String contin = "";
		
		do {
			
			// Initialize list to order words with respect to closeness
			List<OrderedWord> topWords = new ArrayList<>();
			
			// Prompt for number of entries and words
			int numEntries = getNumEntries(map.size());
			String[] userWordArray = getUserWord(map);
			
			// Array for the average values of words entered
			double[] userInputValueArray = new double[50];
			
			for (int j = 0; j < userWordArray.length; j++) {
				double[] wordValueArray = map.get(userWordArray[j]);
				for (int k = 0; k < NUM_COMPARISONS; k++) {
					userInputValueArray[k] += wordValueArray[k];
				}
			}
			for (int j = 0; j < NUM_COMPARISONS; j++) {
				userInputValueArray[j] /= userWordArray.length;
			}
			
			double currentWordArray[];
			double difference;
			
			
			for (String word : map.keySet()) {
				
				if (!(userWordArray.length == 1 && userWordArray[0].equals(word))) {
					currentWordArray = map.get(word);
					difference = 0;
					for (int i = 0; i < NUM_COMPARISONS; i++) {
						difference += Math.abs(currentWordArray[i] - userInputValueArray[i]);
					}
					topWords.add(new OrderedWord(word, difference));
				}
				
			}
			topWords.sort((o1, o2) -> o1.compareTo(o2));
			
			displayTopWords(numEntries, topWords);
			
			System.out.println("Do you wish to continue? (Enter \"n\" to exit):\n");
			contin = kb.next();
			
		} while (!contin.equalsIgnoreCase("n"));
	}
	
	public static void displayTopWords(int numEntries, List<OrderedWord> topWords) {
		System.out.println("\n------TOP WORDS------");
		for (int i = 0; i < numEntries; i++) {
			System.out.println((i+1) + ") " + topWords.get(i).getWord());
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		
		menu();
		
		System.exit(0);
		
	}

}
