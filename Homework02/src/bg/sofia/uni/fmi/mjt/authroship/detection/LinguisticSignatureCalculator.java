package bg.sofia.uni.fmi.mjt.authroship.detection;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LinguisticSignatureCalculator {
    private List<String> words;
    private List<String> sentences;
    private List<String> phrases;
    
    public LinguisticSignatureCalculator(String text) {
        words = getWords(text);
        sentences = getSentences(text);
        phrases = getPhrases();
    }
    
    public LinguisticSignature calculateLinguisticSignature() {
        Map<FeatureType, Double> features = new HashMap<>();

        features.put(FeatureType.AVERAGE_WORD_LENGTH, getAverageWordLength());
        features.put(FeatureType.TYPE_TOKEN_RATIO, getTypeTokenRatio());
        features.put(FeatureType.HAPAX_LEGOMENA_RATIO, getHapaxLegomenaRatio());
        features.put(FeatureType.AVERAGE_SENTENCE_LENGTH, getAverageSentenceLength());
        features.put(FeatureType.AVERAGE_SENTENCE_COMPLEXITY, getAverageSentenceComplexity());

        return new LinguisticSignature(features);
    }

    private double getAverageWordLength() {
        return words.stream()
                .mapToInt(String::length)
                .average()
                .getAsDouble();
    }

    private double getTypeTokenRatio() {
        double uniqueWords = words.stream()
                .distinct()
                .count();
        int totalWords = words.size();

        return uniqueWords / totalWords;
    }

    private double getHapaxLegomenaRatio() {
        int totalWords = words.size();
        int singleOccuranceWords = getSingleOccurrenceWordsCount();

        return (double) singleOccuranceWords / totalWords;
    }

    private double getAverageSentenceLength() {
        int totalWords = words.size();
        int totalSentences = sentences.size();

        return (double) totalWords / totalSentences;
    }

    private double getAverageSentenceComplexity() {
        int totalPhrases = phrases.size();
        int totalSentences = sentences.size();

        return (double) totalPhrases / totalSentences;
    }

    private List<String> getTokens(String text) {
        return Arrays.asList(text.split("\\s+"));
    }

    private List<String> getWords(String text) {
        return getTokens(text).stream()
                .map(LinguisticSignatureCalculator::cleanUp)
                .filter(word -> !word.isEmpty())
                .collect(Collectors.toList());
    }

    private List<String> getSentences(String text) {
        return Arrays.stream(text.split("[!?.]"))
                .map(String::strip)
                .filter(sentence -> !sentence.isEmpty())
                .collect(Collectors.toList());
    }

    private List<String> getPhrases() {
        return sentences.stream()
                .map(sentence -> sentence.split("[,:;]"))
                .filter(sentence -> sentence.length != 1)
                .flatMap(phrase -> Arrays.stream(phrase))
                .filter(phrase -> !phrase.isEmpty())
                .collect(Collectors.toList());
    }

    private int getSingleOccurrenceWordsCount() {
        Map<String, Integer> wordOccurrencesCounter = new HashMap<>();
        int counter = 0;

        for (String word : words) {
            if (wordOccurrencesCounter.containsKey(word)) {
                wordOccurrencesCounter.put(word, wordOccurrencesCounter.get(word) + 1);
            } else {
                wordOccurrencesCounter.put(word, 1);
            }
        }

        for (Integer value : wordOccurrencesCounter.values()) {
            if (value == 1) {
                counter++;
            }
        }

        return counter;
    }

    private static String cleanUp(String word) {
        return word.toLowerCase().replaceAll(
                "^[!.,:;\\-?<>#\\*\'\"\\[\\(\\]\\n\\t\\\\]+|[!.,:;\\-?<>#\\*\'\"\\[\\(\\]\\n\\t\\\\]+$",
                "");
    }
}
