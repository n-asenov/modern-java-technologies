package bg.sofia.uni.mjt.client.response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;

public class ServerResponseHandler implements Runnable {
    private Reader input;
    private OutputStream output;
    
    public ServerResponseHandler(Reader input, OutputStream output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public void run() {
        try (var reader = new BufferedReader(input);
                var writer = new PrintStream(output)) {
            while (true) {
                String serverResponse = reader.readLine();
                
                writer.println(serverResponse);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}