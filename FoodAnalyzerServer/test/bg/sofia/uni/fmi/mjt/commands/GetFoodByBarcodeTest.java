package bg.sofia.uni.fmi.mjt.commands;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import bg.sofia.uni.fmi.mjt.api.InvalidFoodIdException;
import bg.sofia.uni.fmi.mjt.api.NoMatchException;
import bg.sofia.uni.fmi.mjt.api.objects.BrandedFood;
import bg.sofia.uni.fmi.mjt.cache.ServerCache;
import bg.sofia.uni.fmi.mjt.commands.exceptions.InternalServerProblemException;
import bg.sofia.uni.fmi.mjt.commands.exceptions.InvalidNumberOfArgumentsException;

public class GetFoodByBarcodeTest {
    private Command command;
    private ServerCache serverCache;

    @Before
    public void initialize() {
        serverCache = mock(ServerCache.class);
        command = new GetFoodByBarcode(serverCache);
    }

    @Test
    public void testExecuteWithCorrectBarcode() throws InvalidNumberOfArgumentsException,
            InvalidFoodIdException, InternalServerProblemException, NoMatchException {
        BrandedFood food = new BrandedFood(1, "", "", "123");
        String barcode = food.getGtinUpc();

        when(serverCache.containsBrandedFood(barcode)).thenReturn(true);
        when(serverCache.getBrandedFood(barcode)).thenReturn(food);

        String result = command.execute(List.of(barcode));

        verify(serverCache).containsBrandedFood(barcode);
        verify(serverCache).getBrandedFood(barcode);

        assertEquals(food.toString(), result);
    }

    @Test
    public void testExecuteWithBarcodeWhichIsNotInCache() throws InvalidNumberOfArgumentsException,
            InvalidFoodIdException, InternalServerProblemException, NoMatchException {
        BrandedFood food = new BrandedFood(1, "", "", "123");
        String barcode = food.getGtinUpc();
        final String expectedResult = "There is no food with such barcode.";

        when(serverCache.containsBrandedFood(barcode)).thenReturn(false);

        String result = command.execute(List.of(barcode));

        verify(serverCache).containsBrandedFood(barcode);
        verify(serverCache, never()).getBrandedFood(barcode);

        assertEquals(expectedResult, result);
    }

    @Test(expected = InvalidNumberOfArgumentsException.class)
    public void testExecuteWithInvalidNumberOfArguments() throws InvalidNumberOfArgumentsException,
            InvalidFoodIdException, InternalServerProblemException, NoMatchException {
        command.execute(List.of());
    }

}
