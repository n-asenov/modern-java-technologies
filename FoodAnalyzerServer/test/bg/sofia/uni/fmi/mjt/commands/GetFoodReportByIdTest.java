package bg.sofia.uni.fmi.mjt.commands;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import bg.sofia.uni.fmi.mjt.api.FoodDataAPIClient;
import bg.sofia.uni.fmi.mjt.api.InvalidFoodIdException;
import bg.sofia.uni.fmi.mjt.api.NoMatchException;
import bg.sofia.uni.fmi.mjt.api.objects.FoodDetails;
import bg.sofia.uni.fmi.mjt.api.objects.LabelNutrients;
import bg.sofia.uni.fmi.mjt.api.objects.Nutrient;
import bg.sofia.uni.fmi.mjt.cache.ServerCache;
import bg.sofia.uni.fmi.mjt.commands.exceptions.InternalServerProblemException;
import bg.sofia.uni.fmi.mjt.commands.exceptions.InvalidNumberOfArgumentsException;

public class GetFoodReportByIdTest {
    private Command command;
    private ServerCache serverCache;
    private FoodDataAPIClient apiClient;

    @Before
    public void initialize() {
        serverCache = mock(ServerCache.class);
        apiClient = mock(FoodDataAPIClient.class);
        command = new GetFoodReportById(serverCache, apiClient);
    }

    @Test(expected = InvalidNumberOfArgumentsException.class)
    public void testExecuteWithInvalidNumberOfArguments() throws InvalidNumberOfArgumentsException,
            InvalidFoodIdException, InternalServerProblemException, NoMatchException {
        command.execute(List.of());
    }

    @Test(expected = InvalidFoodIdException.class)
    public void testExecuteWithInvalidFoodId() throws InvalidNumberOfArgumentsException,
            InvalidFoodIdException, InternalServerProblemException, NoMatchException {
        command.execute(List.of("test"));
    }

    @Test
    public void testExecuteWithFoodIdIncludedInServerCache()
            throws InvalidNumberOfArgumentsException, InvalidFoodIdException,
            InternalServerProblemException, IOException, InterruptedException, NoMatchException {
        Nutrient nutrient = new Nutrient(1);
        LabelNutrients labelNutrients = new LabelNutrients(nutrient, nutrient, nutrient, nutrient,
                nutrient);
        FoodDetails foodDetails = new FoodDetails("test", "test", labelNutrients, 1);
        long foodId = foodDetails.getFdcId();

        when(serverCache.containsFoodDetails(foodId)).thenReturn(true);
        when(serverCache.getFoodDetails(foodId)).thenReturn(foodDetails);

        String result = command.execute(List.of("1"));

        verify(serverCache).containsFoodDetails(foodId);
        verify(serverCache).getFoodDetails(foodId);
        verify(apiClient, never()).getFoodDetails(foodId);

        assertEquals(foodDetails.toString(), result);
    }

    @Test
    public void testExecuteWithFoodIdNotIncludedInCache()
            throws IOException, InterruptedException, InvalidFoodIdException,
            InvalidNumberOfArgumentsException, InternalServerProblemException, NoMatchException {
        Nutrient nutrient = new Nutrient(1);
        LabelNutrients labelNutrients = new LabelNutrients(nutrient, nutrient, nutrient, nutrient,
                nutrient);
        FoodDetails foodDetails = new FoodDetails("test", "test", labelNutrients, 1);
        long foodId = foodDetails.getFdcId();

        when(serverCache.containsFoodDetails(foodId)).thenReturn(false);
        when(apiClient.getFoodDetails(foodId)).thenReturn(foodDetails);

        String result = command.execute(List.of("1"));
        
        verify(serverCache).containsFoodDetails(foodId);
        verify(serverCache, never()).getFoodDetails(foodId);
        verify(serverCache).saveFoodDetails(foodDetails);
        verify(apiClient).getFoodDetails(foodId);

        assertEquals(foodDetails.toString(), result);
    }
}
