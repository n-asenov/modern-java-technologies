package bg.sofia.uni.fmi.mjt.cache.storage;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import bg.sofia.uni.fmi.mjt.api.objects.BrandedFood;

public class BrandedFoodStorage extends Storage {

    public BrandedFoodStorage(InputStream input, OutputStream output) throws IOException {
        super(input, output);
    }
    
    public Map<String, BrandedFood> loadBrandedFoodData() throws ClassNotFoundException, IOException {
        Map<String, BrandedFood> brandedFoods = new HashMap<>();
        ObjectInputStream input = super.getInput();
        
        try {
            while (true) {
                BrandedFood currentBrandedFood = (BrandedFood) input.readObject();
                brandedFoods.put(currentBrandedFood.getGtinUpc(), currentBrandedFood);
            }
        } catch (EOFException e) {
            input.close();
            
            return brandedFoods;
        }
    }
    
}
