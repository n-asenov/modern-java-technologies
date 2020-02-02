package bg.sofia.uni.fmi.mjt.cache;

import java.io.IOException;
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

public class ServerCache {
    private FoodStorage foodStorage;
    private BrandedFoodStorage brandedFoodStorage;
    private FoodDetailsStorage foodDetailsStorage;
    private Set<Food> foods;
    private Map<String, BrandedFood> brandedFoods;
    private Map<Long, FoodDetails> foodsDetails;
    
    public ServerCache(FoodStorage foodStorage, BrandedFoodStorage brandedFoodStorage,
            FoodDetailsStorage foodDetailsStorage) throws ClassNotFoundException, IOException {
        this.foodStorage = foodStorage;
        this.brandedFoodStorage = brandedFoodStorage;
        this.foodDetailsStorage = foodDetailsStorage;
        foods = foodStorage.loadFoodData();
        brandedFoods = brandedFoodStorage.loadBrandedFoodData();
        foodsDetails = foodDetailsStorage.loadFoodDetailsData();
    }
    
    public boolean containsFood(List<String> foodNameWords) {
        return foods.stream()
                .map(Food::getDescription)
                .anyMatch(foodName -> containsAllWords(foodName, foodNameWords));
    }
    
    public boolean containsBrandedFood(String brandedFoodGtinUpc) {
        return brandedFoods.containsKey(brandedFoodGtinUpc);
    }
    
    public boolean containsFoodDetails(long foodFdcId) {
        return foodsDetails.containsKey(foodFdcId);
    }
    
    public void saveFood(Food food) throws IOException {
        foods.add(food);
        foodStorage.saveObjectData(food);
    }
    
    public void saveBrandedFood(BrandedFood brandedFood) throws IOException {
        brandedFoods.put(brandedFood.getGtinUpc(), brandedFood);
        brandedFoodStorage.saveObjectData(brandedFood);
    }
    
    public void saveFoodDetails(FoodDetails foodDetails) throws IOException {
        foodsDetails.put(foodDetails.getFdcId(), foodDetails);
        foodDetailsStorage.saveObjectData(foodDetails);
    }
    
    public List<Food> getFood(List<String> foodNameWords) {
        return foods.stream()
                .filter(food -> containsAllWords(food.getDescription(), foodNameWords))
                .collect(Collectors.toList());
    }
    
    public BrandedFood getBrandedFood(String brandedFoodGtinUpc) {
        return brandedFoods.get(brandedFoodGtinUpc);
    }
    
    public FoodDetails getFoodDetails(long foodFdcId) {
        return foodsDetails.get(foodFdcId);
    }
    
    private boolean containsAllWords(String foodName, List<String> words) {
        final String foodNameInLowerCase = foodName.toLowerCase();
        final int missingWord = -1;

        return words.stream().allMatch(word -> foodNameInLowerCase.indexOf(word) != missingWord);
    }
    
}