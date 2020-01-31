package bg.sofia.uni.fmi.mjt.api.objects;

public class FoodDetails {
    private String description;
    private String ingredients;
    private LabelNutrients labelNutrients;
    
    public FoodDetails(String description, String ingredients, LabelNutrients labelNutrients) {
        this.description = description;
        this.ingredients = ingredients;
        this.labelNutrients = labelNutrients;
    }

    public String getDescription() {
        return description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public LabelNutrients getLabelNutrients() {
        return labelNutrients;
    }
    
    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        String newLine = System.lineSeparator();
        
        string.append("Name: ").append(description).append(newLine)
            .append("Ingredients: ").append(ingredients).append(newLine)
            .append(labelNutrients.toString());
        
        return string.toString();
    }
}
