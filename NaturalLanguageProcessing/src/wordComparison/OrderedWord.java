package wordComparison;

public class OrderedWord implements Comparable<OrderedWord> {
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
	
	@Override
	public int compareTo(OrderedWord o) {
		
		return Double.compare(this.value, o.value);
	}
	
}
