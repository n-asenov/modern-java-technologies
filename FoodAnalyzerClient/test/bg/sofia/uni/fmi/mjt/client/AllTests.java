package bg.sofia.uni.fmi.mjt.client;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import bg.sofia.uni.fmi.mjt.client.request.ClientRequestHandlerTest;
import bg.sofia.uni.fmi.mjt.input.parser.InputParserTest;

@RunWith(Suite.class)
@SuiteClasses({
    ClientRequestHandlerTest.class,
    InputParserTest.class
})
public class AllTests {

}
