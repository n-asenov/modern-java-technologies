package bg.sofia.uni.fmi.mjt.api.objects;

public class LabelNutrients {
    private Nutrient fat;
    private Nutrient carbohydrates;
    private Nutrient fiber;
    private Nutrient protein;
    private Nutrient calories;
    
    public LabelNutrients(Nutrient fat, Nutrient carbohydrates, Nutrient fiber, Nutrient protein, Nutrient calories ) {
        this.fat = fat;
        this.carbohydrates = carbohydrates;
        this.fiber = fiber;
        this.protein = protein;
        this.calories = calories;
    }

    public Nutrient getFat() {
        return fat;
    }

    public Nutrient getCarbohydrates() {
        return carbohydrates;
    }

    public Nutrient getFiber() {
        return fiber;
    }

    public Nutrient getProtein() {
        return protein;
    }

    public Nutrient getCalories() {
        return calories;
    }
    
}
