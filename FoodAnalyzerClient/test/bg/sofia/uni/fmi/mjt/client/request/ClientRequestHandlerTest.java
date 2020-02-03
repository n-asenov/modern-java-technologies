package bg.sofia.uni.fmi.mjt.client.request;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import bg.sofia.uni.fmi.mjt.reader.BarcodeReader;
import bg.sofia.uni.fmi.mjt.reader.InvalidBarcodeImageException;

public class ClientRequestHandlerTest {
    private ClientRequestHandler clientRequestHandler;
    private BarcodeReader barcodeReader;

    @Before
    public void initialize() {
        barcodeReader = mock(BarcodeReader.class);
        clientRequestHandler = new ClientRequestHandler(null, null, barcodeReader);
    }

    @Test
    public void testGetServerRequestMessageWithGetFoodRequest()
            throws InvalidBarcodeImageException {
        String clientRequest = "get-food raffaello treat";

        assertEquals(clientRequest, clientRequestHandler.getServerRequestMessage(clientRequest));
    }

    @Test
    public void testGetServerRequestMessageWithBarcodeCommandWithCodeOption()
            throws InvalidBarcodeImageException {
        String clientRequest = "get-food-by-barcode --code=100";
        String expectedResult = "get-food-by-barcode 100";
        String actualResult = clientRequestHandler.getServerRequestMessage(clientRequest);

        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = InvalidBarcodeImageException.class)
    public void testGetServerRequestMessageWithBarcodeCommandWithNoOptions()
            throws InvalidBarcodeImageException {
        String clientRequest = "get-food-by-barcode";

        clientRequestHandler.getServerRequestMessage(clientRequest);
    }
    
    @Test
    public void testGetServerRequestMessageWithBarcodeCommandWithImageOption()
            throws InvalidBarcodeImageException {
        String clientRequest = "get-food-by-barcode --img=test.png";
        String imagePath = "test.png";
        
        when(barcodeReader.decodeBarcode(imagePath)).thenReturn("100");
        
        String expectedResult = "get-food-by-barcode 100";
        String actualResult = clientRequestHandler.getServerRequestMessage(clientRequest);
        
        verify(barcodeReader).decodeBarcode(imagePath);
        
        assertEquals(expectedResult, actualResult);
    }
}