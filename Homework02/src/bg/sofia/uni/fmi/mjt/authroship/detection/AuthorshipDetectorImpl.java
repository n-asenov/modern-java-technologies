package bg.sofia.uni.fmi.mjt.authroship.detection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class AuthorshipDetectorImpl implements AuthorshipDetector {
    private Map<LinguisticSignature, String> authorsLinguisticSignatures;
    private double[] weights;

    public AuthorshipDetectorImpl(InputStream signaturesDataset, double[] weights) {
        this.weights = weights;
        authorsLinguisticSignatures = new HashMap<>();
        getAuthorsSignatures(signaturesDataset);
    }

    @Override
    public LinguisticSignature calculateSignature(InputStream mysteryText) {
        if (mysteryText == null) {
            throw new IllegalArgumentException();
        }

        String text = getText(mysteryText);

        return new LinguisticSignatureCalculator(text).calculateLinguisticSignature();
    }

    @Override
    public double calculateSimilarity(LinguisticSignature firstSignature,
            LinguisticSignature secondSignature) {
        if (firstSignature == null || secondSignature == null) {
            throw new IllegalArgumentException();
        }

        double similarity = 0;
        FeatureType[] features = FeatureType.values();
        Map<FeatureType, Double> firstSignatureFeatures = firstSignature.getFeatures();
        Map<FeatureType, Double> secondSignatureFeatures = secondSignature.getFeatures();

        for (int index = 0; index < features.length; index++) {
            FeatureType feature = features[index];

            similarity += Math
                    .abs(firstSignatureFeatures.get(feature) - secondSignatureFeatures.get(feature))
                    * weights[index];
        }

        return similarity;
    }

    @Override
    public String findAuthor(InputStream mysteryText) {
        LinguisticSignature textLinguisticSignature = calculateSignature(mysteryText);

        String author = null;
        double bestSimilarity = Double.MAX_VALUE;
        
        for (LinguisticSignature authorLinguisticSignature : authorsLinguisticSignatures.keySet()) {
            double currentSimilarity = calculateSimilarity(authorLinguisticSignature, textLinguisticSignature);
            
            if (currentSimilarity < bestSimilarity) {
                bestSimilarity = currentSimilarity;
                author = authorsLinguisticSignatures.get(authorLinguisticSignature);
            }
        }

        return author;
    }

    private String getText(InputStream mysteryText) {
        StringBuilder text = new StringBuilder();

        try (var reader = new BufferedReader(new InputStreamReader(mysteryText))) {
            String line = reader.readLine();

            while (line != null) {
                text.append(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return text.toString();
    }

    private void getAuthorsSignatures(InputStream signaturesDataset) {
        try (var reader = new BufferedReader(new InputStreamReader(signaturesDataset))) {
            String line = reader.readLine();

            while (line != null) {
                parseSignaturesDatesetLine(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void parseSignaturesDatesetLine(String line) {
        String[] tokens = line.split(",");
        int index = 1;
        Map<FeatureType, Double> features = new HashMap<>();

        for (FeatureType feature : FeatureType.values()) {
            features.put(feature, Double.parseDouble(tokens[index]));
            index++;
        }

        authorsLinguisticSignatures.put(new LinguisticSignature(features), tokens[0]);
    }

}
