package bg.sofia.uni.fmi.mjt.stylechecker;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StyleCheckerTest {
    private StyleChecker checker;
    private Reader input;
    private Writer output;

    @Before
    public void initialize() {
        checker = new StyleChecker();
        output = new StringWriter();
    }

    @After
    public void close() throws IOException {
        input.close();
        output.close();
    }

    @Test
    public void testCheckStyleWithTooMuchStatementsOnASingleLine() {
        String line = "doSomething(); counter++; doSomethingElse(counter);";
        input = new StringReader(line);

        checker.checkStyle(input, output);

        String expected = Warning.STATEMENTS_IN_LINE.getMessage() + System.lineSeparator() + line;

        assertEquals(expected, output.toString());
    }

    @Test
    public void testCheckStyleWithOneStatementOnASingleLine() {
        String line = "doSomething();";
        input = new StringReader(line);

        checker.checkStyle(input, output);

        assertEquals(line, output.toString());
    }

    @Test
    public void testCheckStyleWithOCornerCaseStatementOnASingleLine() {
        String line = "doSomething;;;;";
        input = new StringReader(line);

        checker.checkStyle(input, output);

        assertEquals(line, output.toString());
    }

    @Test
    public void testCheckStyleWithImportStatementWithWildCard() {
        String line = "import java.util.*;";
        input = new StringReader(line);

        checker.checkStyle(input, output);

        String expected = Warning.IMPORT_STATEMENT.getMessage() + System.lineSeparator() + line;

        assertEquals(expected, output.toString());
    }

    @Test
    public void testCheckStyleWithImportStatementWithNoWildCard() {
        String line = "import java.util.Date;";
        input = new StringReader(line);

        checker.checkStyle(input, output);

        assertEquals(line, output.toString());
    }

    @Test
    public void testCheckStyleWithOpeningBracketOnNewLine() {
        String line = "{";
        input = new StringReader(line);

        checker.checkStyle(input, output);

        String expected = Warning.OPENING_BRACKET.getMessage() + System.lineSeparator() + line;

        assertEquals(expected, output.toString());
    }

    @Test
    public void testCheckStyleWithCorrectOpeningBracket() {
        String line = "public static void main(String... args) {";
        input = new StringReader(line);

        checker.checkStyle(input, output);

        assertEquals(line, output.toString());
    }

    @Test
    public void testCheckStyleWithIncorrectPackageStatementWithUpperCase() {
        String line = "package gov.nasa.deepSpace;";
        input = new StringReader(line);

        checker.checkStyle(input, output);

        String expected = Warning.PACKAGE_STATEMENT.getMessage() + System.lineSeparator() + line;

        assertEquals(expected, output.toString());
    }

    @Test
    public void testCheckStyleWithIncorrectPackageStatementWithUnderscore() {
        String line = "package gov.nasa.deep_space;";
        input = new StringReader(line);

        checker.checkStyle(input, output);

        String expected = Warning.PACKAGE_STATEMENT.getMessage() + System.lineSeparator() + line;

        assertEquals(expected, output.toString());
    }

    @Test
    public void testCheckStyleWithIncorrectPackageStatementWithUpperCaseLetters() {
        String line = "package gov.NASA.deepSpace;";
        input = new StringReader(line);

        checker.checkStyle(input, output);

        String expected = Warning.PACKAGE_STATEMENT.getMessage() + System.lineSeparator() + line;

        assertEquals(expected, output.toString());
    }

    @Test
    public void testCheckStyleWithCorrectPackageStatement() {
        String line = "package gov.nasa.deepspace;";
        input = new StringReader(line);

        checker.checkStyle(input, output);

        assertEquals(line, output.toString());
    }

    @Test
    public void testCheckStyleWithOutSizeLine() {
        String line = "Hey, it's Hannah, Hannah Baker. That's right. Don't adjust your... "
                + "whatever device you're listening to this on.It's me, live and in stereo.";
        input = new StringReader(line);

        checker.checkStyle(input, output);

        String expected = Warning.LINE_LENGTH.getMessage() + System.lineSeparator() + line;

        assertEquals(expected, output.toString());
    }

    @Test
    public void testCheckStyleWithLineWithSeveralWarnings() {
        String line = "sayHello();sayHello();sayHello();sayHello();sayHello();"
                + "sayHello();sayHello();sayHello();sayHello();sayHello();sayHello();";
        input = new StringReader(line);

        checker.checkStyle(input, output);

        String expected = Warning.STATEMENTS_IN_LINE.getMessage() + System.lineSeparator()
                + Warning.LINE_LENGTH.getMessage() + System.lineSeparator() + line;

        assertEquals(expected, output.toString());
    }

    @Test
    public void testCheckStyleWith2Statements() {
        String line = System.lineSeparator() + "doSomething();" + System.lineSeparator()
                + "counter++;";
        input = new StringReader(line);

        checker.checkStyle(input, output);

        assertEquals(line, output.toString());
    }
}
