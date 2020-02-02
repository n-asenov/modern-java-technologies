package bg.sofia.uni.fmi.mjt.api.objects;

import java.io.Serializable;

public class FoodDetails implements Serializable {
    private static final long serialVersionUID = -4001006994250353821L;

    private String description;
    private String ingredients;
    private LabelNutrients labelNutrients;
    private long fdcId;
    
    public FoodDetails(String description, String ingredients, LabelNutrients labelNutrients, long fdcId) {
        this.description = description;
        this.ingredients = ingredients;
        this.labelNutrients = labelNutrients;
        this.fdcId = fdcId;
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
    
    public long getFdcId() {
        return fdcId;
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
