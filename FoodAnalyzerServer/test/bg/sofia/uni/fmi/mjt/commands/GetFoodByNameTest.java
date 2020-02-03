package bg.sofia.uni.fmi.mjt.commands;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.any;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import bg.sofia.uni.fmi.mjt.api.FoodDataApiClient;
import bg.sofia.uni.fmi.mjt.api.InvalidFoodIdException;
import bg.sofia.uni.fmi.mjt.api.NoMatchException;
import bg.sofia.uni.fmi.mjt.api.objects.BrandedFood;
import bg.sofia.uni.fmi.mjt.api.objects.Food;
import bg.sofia.uni.fmi.mjt.cache.ServerCache;
import bg.sofia.uni.fmi.mjt.commands.exceptions.InternalServerProblemException;
import bg.sofia.uni.fmi.mjt.commands.exceptions.InvalidNumberOfArgumentsException;

public class GetFoodByNameTest {
    private Command command;
    private ServerCache serverCache;
    private FoodDataApiClient apiClient;

    @Before
    public void initialize() {
        serverCache = mock(ServerCache.class);
        apiClient = mock(FoodDataApiClient.class);
        command = new GetFoodByName(serverCache, apiClient);
    }

    @Test(expected = InvalidNumberOfArgumentsException.class)
    public void testExecuteWithInvalidNumberOfArguments() throws InvalidNumberOfArgumentsException,
            InvalidFoodIdException, InternalServerProblemException, NoMatchException {
        command.execute(List.of());
    }

    @Test
    public void testExecuteWithFoodInServerCache()
            throws InvalidNumberOfArgumentsException, InvalidFoodIdException,
            InternalServerProblemException, NoMatchException, IOException, InterruptedException {
        Food food = new Food(1, "test", "");
        String foodName = food.getDescription();
        List<String> arguments = List.of(foodName);

        when(serverCache.containsFood(arguments)).thenReturn(true);
        when(serverCache.getFood(arguments)).thenReturn(List.of(food));

        final String result = command.execute(arguments);

        verify(serverCache).containsFood(arguments);
        verify(serverCache).getFood(arguments);
        verify(apiClient, never()).searchFood(foodName);
        verify(apiClient, never()).getBrandedFood(food.getFdcId());

        assertEquals(food.toString(), result);
    }

    @Test
    public void testExecuteWithNotBrandedFoodWhichIsNotInCache() throws IOException,
            InterruptedException, NoMatchException, InvalidNumberOfArgumentsException,
            InvalidFoodIdException, InternalServerProblemException {
        Food food = new Food(1, "test", "");
        String foodName = food.getDescription();
        List<String> arguments = List.of(foodName);

        when(serverCache.containsFood(arguments)).thenReturn(false);
        when(apiClient.searchFood(foodName)).thenReturn(List.of(food));

        final String result = command.execute(arguments);

        verify(serverCache).containsFood(arguments);
        verify(serverCache, never()).getFood(arguments);
        verify(serverCache).saveFood(food);
        verify(serverCache, never()).saveBrandedFood(any());
        verify(apiClient).searchFood(foodName);
        verify(apiClient, never()).getBrandedFood(food.getFdcId());

        assertEquals(food.toString(), result);
    }

    @Test
    public void testExecuteWithBrandedFoodWhichIsNotInCache()
            throws IOException, InterruptedException, NoMatchException, InvalidFoodIdException,
            InvalidNumberOfArgumentsException, InternalServerProblemException {
        Food food = new Food(1, "test", new String("Branded"));
        BrandedFood brandedFood = new BrandedFood(1, "", "Branded", "1");
        String foodName = food.getDescription();
        List<String> arguments = List.of(foodName);

        when(serverCache.containsFood(arguments)).thenReturn(false);
        when(apiClient.searchFood(foodName)).thenReturn(List.of(food));
        when(apiClient.getBrandedFood(food.getFdcId())).thenReturn(brandedFood);

        final String result = command.execute(arguments);

        verify(serverCache).containsFood(arguments);
        verify(serverCache, never()).getFood(arguments);
        verify(serverCache).saveFood(food);
        verify(serverCache).saveBrandedFood(brandedFood);
        verify(apiClient).searchFood(foodName);
        verify(apiClient).getBrandedFood(food.getFdcId());

        assertEquals(food.toString(), result);
    }

}
