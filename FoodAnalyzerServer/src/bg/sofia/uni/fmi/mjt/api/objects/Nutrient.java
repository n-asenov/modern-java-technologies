package bg.sofia.uni.fmi.mjt.api.objects;

import java.io.Serializable;

public class Nutrient implements Serializable {
    private static final long serialVersionUID = 5143919254223329571L;
    
    private double value;
    
    public Nutrient(double value) {
        this.value = value;
    }
    
    public double getValue() {
        return value;
    }
}
