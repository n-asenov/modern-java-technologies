package bg.sofia.uni.fmi.mjt.authroship.detection;

import java.util.HashMap;
import java.util.Map;

public class LinguisticSignature {
    private Map<FeatureType, Double> features;
    
    public LinguisticSignature(Map<FeatureType, Double> features) {
        this.features = features;
    }
    
    public Map<FeatureType, Double> getFeatures() {
        return new HashMap<>(features);
    }
}
