package wordComparison;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class WordComparison {
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
			
			double array[] = new double[50];
			for (int i = 0; i < 50; i++) {
				array[i] = Double.parseDouble(fileReader.next().replaceAll(",", ""));
			}
			
			map.put(word, array);
			
		}
		fileReader.close();
		System.out.println(System.nanoTime() - startTime);
		
		return map;
	}
	
	// Prompts user for word
	private static String getUserWord(Map<String, double[]> map) {
		Scanner kb = new Scanner(System.in);
		System.out.println("Please enter a word: ");
		String userWord;
		do { // Ensures that user's input is a valid key
			userWord = kb.nextLine();
		} while (!map.containsKey(userWord));
		return userWord;
	}
	
	private static int getNumEntries(int size) {
		Scanner kb = new Scanner(System.in);
		int input = 0;
		do {
			try {
				input = kb.nextInt();
			} catch (Exception InputMismatchException) {
				System.out.println("Not a valid input\n");
			}
		} while(input > 0 && input <= size);
		
		return input;
		
	}

	public static void main(String[] args) throws FileNotFoundException {
		
		HashMap<String, double[]> map = new HashMap<>(getWords());
		
		// Prompt user to enter a word
		String userWord = getUserWord(map);
		int numEntries = getNumEntries(map.size());
		
		List<OrderedWord> topWords = new ArrayList<>();
		
		
		
		double userWordArray[] = map.get(userWord);
		double currentWordArray[];
		double closest = 1000;
		String closestWord = "";
		double difference;
		
		for (String word : map.keySet()) {
			System.out.println(word);
			
			if (!word.equals(userWord)) {
				currentWordArray = map.get(word);
				difference = 0;
				for (int i = 0; i < userWordArray.length; i++) {
					difference += Math.abs(currentWordArray[i] - userWordArray[i]);
				}
				OrderedWord orderedWord = new OrderedWord(word, difference);
				topWords.add(orderedWord);
				}
			}
			
		}
		System.out.println("closest word: " + closestWord);
		System.out.println("closest word length: " + closestWord.length());
		System.out.println("closest value: " + closest);
	}

}
