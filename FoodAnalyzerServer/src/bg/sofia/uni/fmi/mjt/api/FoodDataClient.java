package bg.sofia.uni.fmi.mjt.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import com.google.gson.Gson;

import bg.sofia.uni.fmi.mjt.api.objects.FoodDetails;

public class FoodDataClient {
    private static String INVALID_FOOD_ID = "The entered food ID is invalid!";
    
    private static final int INTERNAL_SERVER_ERROR = 500;
    
    private static Gson gson = new Gson();
    
    private static String scheme = "https";
    private static String host = "api.nal.usda.gov";

    private HttpClient client;
    private String apiKey;

    public FoodDataClient(HttpClient client, String apiKey) {
        this.client = client;
        this.apiKey = apiKey;
    }

    public FoodDetails getFoodDetails(long foodId) throws IOException, InterruptedException, InvalidFoodIdException {
        HttpRequest request = HttpRequest.newBuilder(getURI(foodId)).build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        
        if (response.statusCode() == INTERNAL_SERVER_ERROR) {
            throw new InvalidFoodIdException(INVALID_FOOD_ID);
        }

        return gson.fromJson(response.body(), FoodDetails.class);
    }

    private URI getURI(long foodId) {
        StringBuilder uri = new StringBuilder();
        
        uri.append(scheme)
            .append("://").append(host)
            .append("/fdc/v1/").append(foodId)
            .append('?').append("api_key=").append(apiKey);
        
        return URI.create(uri.toString());
    }
    
}
