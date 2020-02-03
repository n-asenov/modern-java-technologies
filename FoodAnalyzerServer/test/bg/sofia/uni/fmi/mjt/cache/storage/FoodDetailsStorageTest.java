package bg.sofia.uni.fmi.mjt.cache.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

import org.junit.Test;

import bg.sofia.uni.fmi.mjt.api.objects.FoodDetails;

public class FoodDetailsStorageTest {

    @Test
    public void testLoadFoodDetailsDataFromEmptyInput() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream emptyInput = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(emptyInput);
        ByteArrayInputStream input = new ByteArrayInputStream(emptyInput.toByteArray());

        FoodDetailsStorage foodDetailsStorage = new FoodDetailsStorage(input, output);

        Map<Long, FoodDetails> foodDetails = foodDetailsStorage.loadFoodDetailsData();

        assertEquals(0, foodDetails.size());
    }

    @Test
    public void testLoadFoodDetailsDataFromInputWithOneObject()
            throws IOException, ClassNotFoundException {
        ByteArrayOutputStream testInput = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(testInput);
        FoodDetails expectedFoodDetails = new FoodDetails("test", "test", null, 1);
        output.writeObject(expectedFoodDetails);
        ByteArrayInputStream input = new ByteArrayInputStream(testInput.toByteArray());

        FoodDetailsStorage foodDetailsStorage = new FoodDetailsStorage(input, output);

        Map<Long, FoodDetails> foodDetails = foodDetailsStorage.loadFoodDetailsData();

        assertEquals(1, foodDetails.size());
        assertTrue(foodDetails.containsKey(expectedFoodDetails.getFdcId()));

        FoodDetails actualFoodDetails = foodDetails.get(expectedFoodDetails.getFdcId());
        assertEquals(expectedFoodDetails.getDescription(), actualFoodDetails.getDescription());
        assertEquals(expectedFoodDetails.getIngredients(), actualFoodDetails.getIngredients());
        assertEquals(expectedFoodDetails.getLabelNutrients(),
                actualFoodDetails.getLabelNutrients());
    }

}
