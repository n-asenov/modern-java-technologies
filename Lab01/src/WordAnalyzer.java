
public class WordAnalyzer {
	public static String getSharedLetters(String word1, String word2) {
		word1 = word1.toLowerCase();
		word2 = word2.toLowerCase();
		
		int[] counter = new int[26];
		
		for (int index = 0; index < word1.length(); index++) {
			if (counter[word1.charAt(index) - 'a'] == 0) {
				counter[word1.charAt(index) - 'a']++;
			}
		}

		for (int index = 0; index < word2.length(); index++) {
			if (counter[word2.charAt(index) - 'a'] == 1) {
				counter[word2.charAt(index) - 'a']++;
			}
		}
		
		StringBuilder result = new StringBuilder();
		
		for (int index = 0; index < counter.length; index++) {
			if(counter[index] == 2) {
				result.append((char)('a' + index));
			}
		} 
		
		return result.toString();
	}
}
