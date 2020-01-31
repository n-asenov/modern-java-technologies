package bg.sofia.uni.fmi.mjt.api;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.atMostOnce;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import bg.sofia.uni.fmi.mjt.api.objects.FoodDetails;
import bg.sofia.uni.fmi.mjt.api.objects.LabelNutrients;
import bg.sofia.uni.fmi.mjt.api.objects.Nutrient;

public class FoodDataClientTest {
    private static double DELTA  = 0.000001;
   
    private static int OK = 200;
    private static int INTERNAL_SERVER_ERROR = 500;
    
    private static String apiKey = "MY_API_KEY";
    
    private static Gson gson = new Gson();
    
    private FoodDataClient apiClient;
    private HttpClient mockedClient;
    private HttpResponse<Object> mockedResponse;
    
    @Before
    public void initialize() {
        mockedClient = mock(HttpClient.class);
        mockedResponse = mock(HttpResponse.class);
        apiClient = new FoodDataClient(mockedClient, apiKey);
    }
    
    @Test
    public void testGetFoodDetailsWithCorrectFoodId() throws IOException, InterruptedException, InvalidFoodIdException {
        final long foodId = 100;
        final double expectedValue = 6;
        Nutrient nutrient = new Nutrient(expectedValue);
        LabelNutrients labelNutrients = new LabelNutrients(nutrient, nutrient, nutrient, nutrient, nutrient);
        FoodDetails expectedFoodDetails = new FoodDetails("Coconut", "coconut", labelNutrients);
        
        when(mockedClient.send(any(), any())).thenReturn(mockedResponse);
        when(mockedResponse.statusCode()).thenReturn(OK);
        when(mockedResponse.body()).thenReturn(gson.toJson(expectedFoodDetails));
        
        FoodDetails actualfoodDetatils = apiClient.getFoodDetails(foodId);
        
        verify(mockedClient, atMostOnce()).send(any(), any());
        verify(mockedResponse, atMostOnce()).statusCode();
        verify(mockedResponse, atMostOnce()).body();
        
        assertEquals("Coconut", actualfoodDetatils.getDescription());
        assertEquals("coconut", actualfoodDetatils.getIngredients());
        assertEquals(expectedValue, actualfoodDetatils.getLabelNutrients().getFat().getValue(), DELTA);
        assertEquals(expectedValue, actualfoodDetatils.getLabelNutrients().getCarbohydrates().getValue(), DELTA);
        assertEquals(expectedValue, actualfoodDetatils.getLabelNutrients().getCalories().getValue(), DELTA);
        assertEquals(expectedValue, actualfoodDetatils.getLabelNutrients().getFiber().getValue(), DELTA);
        assertEquals(expectedValue, actualfoodDetatils.getLabelNutrients().getProtein().getValue(), DELTA);
    }
    
    @Test (expected = InvalidFoodIdException.class)
    public void testGetFoodDetailsWithIncorrectFoodId() throws IOException, InterruptedException, InvalidFoodIdException {
        final long invalidFoodId = -1;
        
        when(mockedClient.send(any(), any())).thenReturn(mockedResponse);
        when(mockedResponse.statusCode()).thenReturn(INTERNAL_SERVER_ERROR);
        
        apiClient.getFoodDetails(invalidFoodId);
    }
    
}
