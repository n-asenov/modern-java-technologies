package bg.sofia.uni.mjt.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Input {
    BufferedReader input;
    
    public Input(InputStream input) {
        this.input = new BufferedReader(new InputStreamReader(input));
    }
    
    public String readLine() throws IOException {
        return input.readLine();
    }
}
