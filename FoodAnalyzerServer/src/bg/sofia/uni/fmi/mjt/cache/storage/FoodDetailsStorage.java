package bg.sofia.uni.fmi.mjt.cache.storage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import bg.sofia.uni.fmi.mjt.api.objects.FoodDetails;

public class FoodDetailsStorage extends Storage {

	public FoodDetailsStorage(String foodDetailsStorageName) throws IOException {
		super(foodDetailsStorageName);
	}

	public Map<Long, FoodDetails> loadFoodDetailsData() throws IOException {
		Map<Long, FoodDetails> foodsDetails = new HashMap<>();

		try (var reader = new BufferedReader(new FileReader(getStorage()))) {
			String line = reader.readLine();

			while (line != null) {
				FoodDetails currentFoodDetails = GSON.fromJson(line, FoodDetails.class);
				foodsDetails.put(currentFoodDetails.getFdcId(), currentFoodDetails);
				line = reader.readLine();
			}
		} catch (IOException e) {
			throw new IOException("Could not load food details storage", e);
		}

		return foodsDetails;
	}

}
