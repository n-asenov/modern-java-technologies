package bg.sofia.uni.fmi.mjt.cache.storage;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Set;

import org.junit.Test;

import bg.sofia.uni.fmi.mjt.api.objects.Food;

public class FoodStorageTest {
    
    @Test
    public void testLoadFoodDataFromEmptyInput() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream emptyInput = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(emptyInput);
        ByteArrayInputStream input = new ByteArrayInputStream(emptyInput.toByteArray());
       
        FoodStorage foodStorage = new FoodStorage(input, output);
        
        Set<Food> foods = foodStorage.loadFoodData();
        
        assertEquals(0, foods.size());
    }
    
    @Test
    public void testLoadFoodDataFromInputWithOneObject() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream testInput = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(testInput);
        Food expectedFood = new Food(1, "test", "test");
        
        output.writeObject(expectedFood);
        
        ByteArrayInputStream input = new ByteArrayInputStream(testInput.toByteArray());
        FoodStorage foodStorage = new FoodStorage(input, output);
        
        Set<Food> foods = foodStorage.loadFoodData();
        Food actualFood = foods.iterator().next();
        
        assertEquals(1, foods.size());
        assertEquals(expectedFood.getFdcId(), actualFood.getFdcId());
        assertEquals(expectedFood.getDescription(), actualFood.getDescription());
        assertEquals(expectedFood.getDataType(), actualFood.getDataType());
    }
    
    @Test
    public void testSaveObjectDataWithFoodObject() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream stubOutput = new ByteArrayOutputStream();
        ObjectOutputStream stubOutputHelper = new ObjectOutputStream(stubOutput);
        ByteArrayInputStream input = new ByteArrayInputStream(stubOutput.toByteArray());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        FoodStorage foodStorage = new FoodStorage(input, output);
        Food expectedFoodObject = new Food(1, "test", "test");
        
        foodStorage.saveObjectData(expectedFoodObject);

        ObjectInputStream testInput = new ObjectInputStream(new ByteArrayInputStream(output.toByteArray()));
        Food actualFoodObject = (Food) testInput.readObject();
        
        assertEquals(expectedFoodObject.getFdcId(), actualFoodObject.getFdcId());
        assertEquals(expectedFoodObject.getDescription(), actualFoodObject.getDescription());
        assertEquals(expectedFoodObject.getDataType(), actualFoodObject.getDataType());
    }

}