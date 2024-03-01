package wordComparison;

public class OrderedWord implements Comparable {
	private String word;
	private double value;
	
	public OrderedWord(String word, double value) {
		this.word = word;
		this.value = value;
	}

	public String getWord() {
		return word;
	}

	public double getValue() {
		return value;
	}
	
}
