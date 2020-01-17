package bg.sofia.uni.fmi.mjt.chat;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;

import org.junit.Test;

public class ServerResponseHandlerTest {

    @Test
    public void testRunWithSimpleMessage() {
        String message = "Hello, User";
        Reader reader = new StringReader(message);
        OutputStream output = new ByteArrayOutputStream();
        ServerResponseHandler serverResponseHandler = new ServerResponseHandler(reader, output);
        
        serverResponseHandler.run();

        assertEquals(message + System.lineSeparator(), output.toString());
    }

}
