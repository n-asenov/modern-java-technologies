package bg.sofia.uni.fmi.mjt.cache;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import bg.sofia.uni.fmi.mjt.api.objects.BrandedFood;
import bg.sofia.uni.fmi.mjt.api.objects.Food;
import bg.sofia.uni.fmi.mjt.api.objects.FoodDetails;
import bg.sofia.uni.fmi.mjt.cache.storage.BrandedFoodStorage;
import bg.sofia.uni.fmi.mjt.cache.storage.FoodDetailsStorage;
import bg.sofia.uni.fmi.mjt.cache.storage.FoodStorage;
import bg.sofia.uni.fmi.mjt.commands.exceptions.InternalServerProblemException;

public class ServerCache {
	private FoodStorage foodStorage;
	private BrandedFoodStorage brandedFoodStorage;
	private FoodDetailsStorage foodDetailsStorage;
	private Set<Food> cachedFoods;
	private Map<String, BrandedFood> cachedBrandedFoods;
	private Map<Long, FoodDetails> cachedFoodsDetails;

	public ServerCache(String foodStorageName, String brandedFoodStorageName, String foodDetailsStorageName)
			throws InternalServerProblemException {
		this.foodStorage = new FoodStorage(foodStorageName);
		this.brandedFoodStorage = new BrandedFoodStorage(brandedFoodStorageName);
		this.foodDetailsStorage = new FoodDetailsStorage(foodDetailsStorageName);

		cachedFoods = foodStorage.loadFoodData();
		cachedBrandedFoods = brandedFoodStorage.loadBrandedFoodData();
		cachedFoodsDetails = foodDetailsStorage.loadFoodDetailsData();
	}

	public boolean containsFood(List<String> foodNameWords) {
		return cachedFoods.stream()
				.map(Food::getDescription)
				.anyMatch(foodName -> containsAllWords(foodName, foodNameWords));
	}

	public boolean containsBrandedFood(String brandedFoodBarcode) {
		return cachedBrandedFoods.containsKey(brandedFoodBarcode);
	}

	public boolean containsFoodDetails(long foodId) {
		return cachedFoodsDetails.containsKey(foodId);
	}

	public void saveFoods(List<Food> foods) throws InternalServerProblemException {
		cachedFoods.addAll(foods);
		foodStorage.saveFoodData(foods);
	}

	public void saveBrandedFoods(List<BrandedFood> brandedFoods) throws InternalServerProblemException {
		for (BrandedFood brandedFood : brandedFoods) {
			cachedBrandedFoods.put(brandedFood.getGtinUpc(), brandedFood);
		}

		brandedFoodStorage.saveBrandedFoodData(brandedFoods);
	}

	public void saveFoodDetails(FoodDetails foodDetails) throws InternalServerProblemException {
		cachedFoodsDetails.put(foodDetails.getFdcId(), foodDetails);
		foodDetailsStorage.saveFoodDetails(foodDetails);
	}

	public List<Food> getFood(List<String> foodNameWords) {
		return cachedFoods.stream()
				.filter(food -> containsAllWords(food.getDescription(), foodNameWords))
				.collect(Collectors.toList());
	}

	public BrandedFood getBrandedFood(String brandedFoodBarcode) {
		return cachedBrandedFoods.get(brandedFoodBarcode);
	}

	public FoodDetails getFoodDetails(long foodId) {
		return cachedFoodsDetails.get(foodId);
	}

	private boolean containsAllWords(String foodName, List<String> words) {
		final String foodNameInLowerCase = foodName.toLowerCase();
		final int missingWord = -1;

		return words.stream().allMatch(word -> foodNameInLowerCase.indexOf(word) != missingWord);
	}

}
