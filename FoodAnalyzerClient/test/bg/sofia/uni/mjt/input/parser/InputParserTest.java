package bg.sofia.uni.mjt.input.parser;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class InputParserTest {

    private InputParser parser;

    @Before
    public void initialize() {
        parser = new InputParser();
    }

    @Test
    public void testGetCommandWithEmptyInput() {
        String emptyInput = "";
        assertEquals(emptyInput, parser.getCommand(emptyInput));
    }

    @Test
    public void testGetArgumentsWithEmptyInput() {
        String emptyInput = "";

        assertEquals(List.of(), parser.getArguments(emptyInput));
    }

    @Test
    public void testGetCommandWithInputWithOneWord() {
        String input = "test";
        assertEquals(input, parser.getCommand(input));
    }

    @Test
    public void testGetCommandWithInputWithSeveralWords() {
        String input = "test test";
        String expectedResult = "test";
        assertEquals(expectedResult, parser.getCommand(input));
    }

    @Test
    public void testGetArgumentsWithInputWithOneWord() {
        String input = "test";
        assertEquals(List.of(), parser.getArguments(input));
    }

    @Test
    public void testGetArgumentsWithInputWithSeveralWords() {
        String input = "test test test";
        List<String> expectedResult = List.of("test", "test");
        assertEquals(expectedResult, parser.getArguments(input));
    }

}
