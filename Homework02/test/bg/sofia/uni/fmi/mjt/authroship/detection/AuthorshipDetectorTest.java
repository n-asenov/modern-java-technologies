package bg.sofia.uni.fmi.mjt.authroship.detection;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

public class AuthorshipDetectorTest {
    private static final double DELTA = 0.000001;

    private static AuthorshipDetectorImpl detector;

    @BeforeClass
    public static void initialize() {
        final double[] weights = { 11, 33, 50, 0.4, 4 };
        String knownSignatures = 
                "Agatha Christie, 4.40212537354, 0.103719383127, 0.0534892315963, 10.0836888743, 1.90662947161"
                + System.lineSeparator()
                + "Alexandre Dumas, 4.38235547477, 0.049677588873, 0.0212183996175, 15.0054854981, 2.63499369483"
                + System.lineSeparator()
                + "Brothers Grim, 3.96868608302, 0.0529378997714, 0.0208217283571, 22.2267197987, 3.4129614094"
                + System.lineSeparator()
                + "Charles Dickens, 4.34760725241, 0.0803220950584, 0.0390662700499, 16.2613453121, 2.87721723105"
                + System.lineSeparator()
                + "Fyodor Dostoevsky, 4.34066732195, 0.0528571428571, 0.0233414043584, 12.8108273249, 2.16705364781"
                + System.lineSeparator()
                + "James Joyce, 4.52346300961, 0.120109917189, 0.0682315429476, 10.9663296918, 1.79667373227"
                + System.lineSeparator()
                + "Jane Austen, 4.41553119311, 0.0563451817574, 0.02229943808, 16.8869087498, 2.54817097682"
                + System.lineSeparator()
                + "Mark Twain, 4.33272222298, 0.117254215021, 0.0633074228159, 14.3548573631, 2.43716268311"
                + System.lineSeparator()
                + "Sir Arthur Conan Doyle, 4.16808311494, 0.0822989796874, 0.0394458485444, 14.717564466, 2.2220872148"
                + System.lineSeparator()
                + "William Shakespeare, 4.16216957834, 0.105602561171, 0.0575348730848, 9.34707371975, 2.24620146314";
        detector = new AuthorshipDetectorImpl(new ByteArrayInputStream(knownSignatures.getBytes()),
                weights);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculateSignatureWithNullArgument() {
        detector.calculateSignature(null);
    }

    @Test
    public void testCalculateSignatureWithSimpleText() {
        String text = "this is the" + System.lineSeparator() + "first sentence. Isn't"
                + System.lineSeparator() + "it? Yes ! !! This " + System.lineSeparator()
                + System.lineSeparator() + "last bit :) is also a sentence, but "
                + System.lineSeparator() + "without a terminator other than the end of the file";

        LinguisticSignature textLinguisticSignature = detector
                .calculateSignature(new ByteArrayInputStream(text.getBytes()));

        Map<FeatureType, Double> textFeatures = textLinguisticSignature.getFeatures();

        final double expectedAverageWordLength = 4.16;
        double actualAverageWordLength = textFeatures.get(FeatureType.AVERAGE_WORD_LENGTH);
        assertEquals(expectedAverageWordLength, actualAverageWordLength, DELTA);

        final double expectedTypeTokenRatio = 0.8;
        double actualTypeTokenRatio = textFeatures.get(FeatureType.TYPE_TOKEN_RATIO);
        assertEquals(expectedTypeTokenRatio, actualTypeTokenRatio, DELTA);

        final double expectedHapaxLegomenaRatio = 0.6;
        double actualHapaxLegomenaRatio = textFeatures.get(FeatureType.HAPAX_LEGOMENA_RATIO);
        assertEquals(expectedHapaxLegomenaRatio, actualHapaxLegomenaRatio, DELTA);

        final double expectedSentenceLength = 6.25;
        double actualSentenceLength = textFeatures.get(FeatureType.AVERAGE_SENTENCE_LENGTH);
        assertEquals(expectedSentenceLength, actualSentenceLength, DELTA);

        final double expectedSentenceComplexity = 0.75;
        double actualSentenceComplexity = textFeatures.get(FeatureType.AVERAGE_SENTENCE_COMPLEXITY);
        assertEquals(expectedSentenceComplexity, actualSentenceComplexity, DELTA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculateSimilarityWithNullFirstArgument() {
        detector.calculateSimilarity(null, new LinguisticSignature(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculateSimilarityWithNullSecondArgument() {
        detector.calculateSimilarity(new LinguisticSignature(null), null);
    }

    @Test
    public void testCalculateSimilarityWithTwoDifferentSignatures() {
        final double firstSignatureAverageWordLength = 4.4;
        final double firstSignatureTypeTokenRatio = 0.1;
        final double firstSignatureHapaxLegomenaRatio = 0.05;
        final double firstSignatureAverageSentenceLength = 10.0;
        final double firstSignatureAverageSentenceComplexity = 2.0;

        Map<FeatureType, Double> firstSignatureFeatures = new HashMap<>();
        firstSignatureFeatures.put(FeatureType.AVERAGE_WORD_LENGTH,
                firstSignatureAverageWordLength);
        firstSignatureFeatures.put(FeatureType.TYPE_TOKEN_RATIO, firstSignatureTypeTokenRatio);
        firstSignatureFeatures.put(FeatureType.HAPAX_LEGOMENA_RATIO,
                firstSignatureHapaxLegomenaRatio);
        firstSignatureFeatures.put(FeatureType.AVERAGE_SENTENCE_LENGTH,
                firstSignatureAverageSentenceLength);
        firstSignatureFeatures.put(FeatureType.AVERAGE_SENTENCE_COMPLEXITY,
                firstSignatureAverageSentenceComplexity);

        final double secondSignatureAverageWordLength = 4.3;
        final double secondSignatureTypeTokenRatio = 0.1;
        final double secondSignatureHapaxLegomenaRatio = 0.04;
        final double secondSignatureAverageSentenceLength = 16.0;
        final double secondignatureAverageSentenceComplexity = 4.0;

        Map<FeatureType, Double> secondSignatureFeatures = new HashMap<>();
        secondSignatureFeatures.put(FeatureType.AVERAGE_WORD_LENGTH,
                secondSignatureAverageWordLength);
        secondSignatureFeatures.put(FeatureType.TYPE_TOKEN_RATIO, secondSignatureTypeTokenRatio);
        secondSignatureFeatures.put(FeatureType.HAPAX_LEGOMENA_RATIO,
                secondSignatureHapaxLegomenaRatio);
        secondSignatureFeatures.put(FeatureType.AVERAGE_SENTENCE_LENGTH,
                secondSignatureAverageSentenceLength);
        secondSignatureFeatures.put(FeatureType.AVERAGE_SENTENCE_COMPLEXITY,
                secondignatureAverageSentenceComplexity);

        final double expectedSimilarity = 12;
        double actualSimilarity = detector.calculateSimilarity(
                new LinguisticSignature(firstSignatureFeatures),
                new LinguisticSignature(secondSignatureFeatures));

        assertEquals(expectedSimilarity, actualSimilarity, DELTA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindAuthorWithNullArgument() {
        detector.findAuthor(null);
    }

    @Test
    public void testFindAuthorWithParagraphFromCharlesDickensBook() {
        String paragraph = "My father's family name being Pirrip, and my Christian name Philip, my"
                + System.lineSeparator()
                + "infant tongue could make of both names nothing longer or more explicit"
                + System.lineSeparator()
                + "than Pip. So, I called myself Pip, and came to be called Pip."
                + System.lineSeparator()
                + "I give Pirrip as my father's family name, on the authority of his"
                + System.lineSeparator()
                + "tombstone and my sister,--Mrs. Joe Gargery, who married the blacksmith."
                + System.lineSeparator()
                + "As I never saw my father or my mother, and never saw any likeness"
                + System.lineSeparator()
                + "of either of them (for their days were long before the days of"
                + System.lineSeparator()
                + "photographs), my first fancies regarding what they were like were"
                + System.lineSeparator()
                + "unreasonably derived from their tombstones. The shape of the letters on"
                + System.lineSeparator()
                + "my father's, gave me an odd idea that he was a square, stout, dark man,";

        String expectedAuthor = "Charles Dickens";
        String actualAuthor = detector.findAuthor(new ByteArrayInputStream(paragraph.getBytes()));
        assertEquals(expectedAuthor, actualAuthor);
    }

}
