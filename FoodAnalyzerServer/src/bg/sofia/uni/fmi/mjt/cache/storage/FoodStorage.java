package bg.sofia.uni.fmi.mjt.cache.storage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import bg.sofia.uni.fmi.mjt.api.objects.Food;

public class FoodStorage extends Storage {

    public FoodStorage(String foodStorageName) throws IOException {
    	super(foodStorageName);
    }
    
    public Set<Food> loadFoodData() throws IOException {
        Set<Food> foods = new HashSet<>();
        
        try (var reader = new BufferedReader(new FileReader(getStorage()))){
            String line = reader.readLine();
            
            while (line != null) {
            	Food food = GSON.fromJson(line, Food.class);
            	foods.add(food);
            	line = reader.readLine();
            }
        } catch(IOException e) {
        	throw new IOException("Could not load food storage", e);
        }
        
        return foods;
    }
}
