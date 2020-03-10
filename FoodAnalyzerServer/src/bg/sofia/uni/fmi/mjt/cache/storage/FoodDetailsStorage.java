package bg.sofia.uni.fmi.mjt.cache.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import bg.sofia.uni.fmi.mjt.api.objects.FoodDetails;
import bg.sofia.uni.fmi.mjt.commands.exceptions.InternalServerProblemException;

public class FoodDetailsStorage {
	private static final String NEW_LINE = System.lineSeparator();
	private static final Gson GSON = new Gson();

	private File foodDetailsStorage;

	public FoodDetailsStorage(String foodDetailsStorageName) throws InternalServerProblemException {
		foodDetailsStorage = new File(foodDetailsStorageName);
		
		try {
			foodDetailsStorage.createNewFile();
		} catch (IOException e) {
			throw new InternalServerProblemException("Could not create food details storage file", e);
		}
	}

	public Map<Long, FoodDetails> loadFoodDetailsData() throws InternalServerProblemException {
		Map<Long, FoodDetails> foodsDetails = new HashMap<>();

		try (var reader = new BufferedReader(new FileReader(foodDetailsStorage))) {
			String line = reader.readLine();

			while (line != null) {
				FoodDetails currentFoodDetails = GSON.fromJson(line, FoodDetails.class);
				foodsDetails.put(currentFoodDetails.getFdcId(), currentFoodDetails);
				line = reader.readLine();
			}
		} catch (IOException e) {
			throw new InternalServerProblemException("Could not load food details storage", e);
		}

		return foodsDetails;
	}
	
	public void saveFoodDetails(FoodDetails foodDetails) throws InternalServerProblemException {
		try (FileWriter writer = new FileWriter(foodDetailsStorage, true)) {
			writer.write(GSON.toJson(foodDetails));
			writer.write(NEW_LINE);
			writer.flush();
		} catch (IOException e) {
			throw new InternalServerProblemException("Could not save food details", e);
		}
	}

}
