
public class DNAAnalyzer {
	public static String longestRepeatingSequence(String dna) {
		String result = "";
		
		for (int startIndex = 0; startIndex < dna.length(); startIndex++) {
				for (int wordLength = 1; wordLength < dna.length() - startIndex; wordLength++) {
					String subString = dna.substring(startIndex, startIndex + wordLength);
					String remainingString = dna.substring(startIndex + wordLength);
					if (remainingString.contains(subString) && subString.length() > result.length()) {
						result = subString;
					}
				}
		}
		
		return result;
	}
}
