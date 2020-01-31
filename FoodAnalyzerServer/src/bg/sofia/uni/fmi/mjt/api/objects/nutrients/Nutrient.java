package bg.sofia.uni.fmi.mjt.api.objects.nutrients;

public abstract class Nutrient {
    private double value;
    
    public Nutrient(double value) {
        this.value = value;
    }
    
    public double getValue() {
        return value;
    }
}
