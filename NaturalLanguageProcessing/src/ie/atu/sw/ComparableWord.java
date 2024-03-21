/**
* @author Fionn McCarthy
* @studentID G00414386
* @module Data Structures and Algorithms Assignment
* Year 2
*/
package ie.atu.sw;

/**
 * This class contains getters for words with an associated double value.
 */
public class ComparableWord implements Comparable<ComparableWord> {
	private String word;
	private double value;

	/**
	 * Constructor.
	 * @param word
	 * @param value
	 */
	public ComparableWord(String word, double value) {
		this.word = word;
		this.value = value;
	}
	
	/**
	 * Getter for word.
	 * @return word.
	 */
	public String getWord() {
		return word;
	}
	
	/**
	 * Getter for value.
	 * @return value.
	 */
	public double getValue() {
		return value;
	}
	
	/**
	 * CompareTo method for ComparableWord.
	 * Allows for ordered list of ComparableWord objects.
	 */
	@Override
	public int compareTo(ComparableWord o) {

		return Double.compare(this.value, o.value);
	}

}
