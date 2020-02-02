package bg.sofia.uni.fmi.mjt.parser;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ClientMessageParserTest {
    private ClientMessageParser parser;
    
    @Before
    public void initialize() {
        parser = new ClientMessageParser();
    }
    
    @Test
    public void testGetCommandWithEmptyMessage() {
        String emptyMessage = "";
        assertEquals(emptyMessage, parser.getCommand(emptyMessage));
    }

    @Test
    public void testGetArgumentsWithEmptyMessage() {
        String emptyMessage = "";
        
        assertEquals(List.of(), parser.getArguments(emptyMessage));
    }
    
    @Test
    public void testGetCommandWithMessageWithOneWord() {
        String message = "test";
        assertEquals(message, parser.getCommand(message));
    }
    
    @Test
    public void testGetCommandWithMessageWithSeveralWords() {
        String message = "test test";
        String expectedResult = "test";
        assertEquals(expectedResult, parser.getCommand(message));
    }
    
    @Test
    public void testGetArgumentsWithMessageWithOneWord() {
        String message = "test";
        assertEquals(List.of(), parser.getArguments(message));
    }
    
    @Test
    public void testGetArgumentsWithMessageWithSeveralWords() {
        String message = "test test test";
        List<String> expectedResult = List.of("test", "test");
        assertEquals(expectedResult, parser.getArguments(message));
    }
    
}
