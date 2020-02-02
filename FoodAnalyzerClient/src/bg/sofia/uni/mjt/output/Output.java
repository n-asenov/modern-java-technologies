package bg.sofia.uni.mjt.output;

import java.io.OutputStream;
import java.io.PrintStream;

public class Output {
    private PrintStream output;
    
    public Output(OutputStream output) {
        this.output = new PrintStream(output);
    }
    
    public void printLine(String message) {
        output.println(message);
    }
    
}
