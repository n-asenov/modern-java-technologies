package bg.sofia.uni.fmi.mjt.cache.storage;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import bg.sofia.uni.fmi.mjt.api.objects.FoodDetails;

public class FoodDetailsStorage extends Storage {

    public FoodDetailsStorage(InputStream input, OutputStream output) throws IOException {
        super(input, output);
    }
    
    public Map<Long, FoodDetails> loadFoodDetailsData() throws ClassNotFoundException, IOException {
        Map<Long, FoodDetails> foodsDetails = new HashMap<>();
        ObjectInputStream input = super.getInput();
        
        try {
            while (true) {
                FoodDetails currentFoodDetails = (FoodDetails) input.readObject();
                foodsDetails.put(currentFoodDetails.getFdcId(), currentFoodDetails);
            }
        } catch (EOFException e) {
            input.close();
            
            return foodsDetails;
        }
    }
    
}
