package bg.sofia.uni.fmi.mjt.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.google.gson.Gson;

import bg.sofia.uni.fmi.mjt.api.objects.BrandedFood;
import bg.sofia.uni.fmi.mjt.api.objects.Food;
import bg.sofia.uni.fmi.mjt.api.objects.FoodDetails;
import bg.sofia.uni.fmi.mjt.api.objects.FoodSearchResponse;

public class FoodDataAPIClient {
    private static String INVALID_FOOD_ID = "The entered food ID is invalid!";
    private static String FOOD_NOT_FOUND = "I could not find food with such name";
    
    private static int NO_MATCH = 0;
    private static final int INTERNAL_SERVER_ERROR = 500;
    
    private static Gson gson = new Gson();
    
    private static String scheme = "https";
    private static String host = "api.nal.usda.gov";

    private HttpClient client;
    private String apiKey;

    public FoodDataAPIClient(HttpClient client, String apiKey) {
        this.client = client;
        this.apiKey = apiKey;
    }
    
    public List<Food> searchFood(String foodName) throws IOException, InterruptedException, NoMatchException {
        HttpRequest initialRequest = HttpRequest.newBuilder(getURI(foodName)).build();
        
        HttpResponse<String> initialResponse = client.send(initialRequest, BodyHandlers.ofString());
        
        FoodSearchResponse foodSearchResponse = gson.fromJson(initialResponse.body(), FoodSearchResponse.class);
        
        if (foodSearchResponse.getTotalHits() == NO_MATCH) {
            throw new NoMatchException(FOOD_NOT_FOUND);
        }
        
        return getFoodsFromCurrentSearch(foodName, foodSearchResponse);
    }
    
    public FoodDetails getFoodDetails(long foodId) throws IOException, InterruptedException, InvalidFoodIdException {
        return gson.fromJson(getFoodDetailsById(foodId), FoodDetails.class);
    }
    
    public BrandedFood getBrandedFood(long brandedFoodId) throws InvalidFoodIdException, IOException, InterruptedException {
        return gson.fromJson(getFoodDetailsById(brandedFoodId), BrandedFood.class);
    }
    
    private URI getURI(String foodName) {
        final int currentPage = 1;
        return getURI(foodName, currentPage);
    }
    
    private URI getURI(String foodName, int pageNumber) {
        StringBuilder uri = new StringBuilder();
        
        uri.append(scheme)
            .append("://").append(host)
            .append("/fdc/v1/search")
            .append('?').append("generalSearchInput=").append(foodName)
            .append("&requireAllWords=true")
            .append("&pageNumber=").append(pageNumber)
            .append("&api_key=").append(apiKey);
             
        return URI.create(uri.toString());
    }
    
    private URI getURI(long foodId) {
        StringBuilder uri = new StringBuilder();
        
        uri.append(scheme)
            .append("://").append(host)
            .append("/fdc/v1/").append(foodId)
            .append('?').append("api_key=").append(apiKey);
        
        return URI.create(uri.toString());
    }
    
    private List<Food> getFoodsFromCurrentSearch(String foodName, FoodSearchResponse response) {
        List<Food> foods = new ArrayList<>();
        List<CompletableFuture<String>> responses = new ArrayList<>();
        
        for (int currentPage = 1; currentPage <= 200 && currentPage <= response.getTotalPages(); currentPage++) {
            HttpRequest request = HttpRequest.newBuilder(getURI(foodName, currentPage)).build();
            
            responses.add(client.sendAsync(request, BodyHandlers.ofString()).thenApply(HttpResponse::body));
        }
        
        CompletableFuture.allOf(responses.toArray(CompletableFuture[]::new)).join();

        for (CompletableFuture<String> currentPageResponse : responses) {
            String responseBody = currentPageResponse.getNow(null);
            foods.addAll(getFoodsFromCurrentPage(responseBody));
        }
        
        return foods;
    }
    
    private List<Food> getFoodsFromCurrentPage(String responseBody) {
        return gson.fromJson(responseBody, FoodSearchResponse.class).getFoods();
    }
    
    private String getFoodDetailsById(long foodId) throws InvalidFoodIdException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(getURI(foodId)).build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        
        if (response.statusCode() == INTERNAL_SERVER_ERROR) {
            throw new InvalidFoodIdException(INVALID_FOOD_ID);
        }
        
        return response.body();
    }
    
}
