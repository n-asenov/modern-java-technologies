package bg.sofia.uni.fmi.mjt.cache.storage;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import bg.sofia.uni.fmi.mjt.api.objects.Food;

public class FoodStorage extends Storage {

    public FoodStorage(InputStream input, OutputStream output) throws IOException {
        super(input, output);
    }
    
    public Set<Food> loadFoodData() throws ClassNotFoundException, IOException {
        Set<Food> foods = new HashSet<>();
        ObjectInputStream input = super.getInput();
        
        try {
            while (true) {
                Food currentFood = (Food) input.readObject();
                foods.add(currentFood);
            }
        } catch (EOFException e) {
            input.close();
            
            return foods;
        }
    }
}
