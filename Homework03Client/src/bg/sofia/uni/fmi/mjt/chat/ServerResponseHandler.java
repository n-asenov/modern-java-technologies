package bg.sofia.uni.fmi.mjt.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;

public class ServerResponseHandler implements Runnable {
    private BufferedReader reader;
    private PrintStream output;
    
    public ServerResponseHandler(Reader reader, OutputStream output) {
        this.reader = new BufferedReader(reader);
        this.output = new PrintStream(output);
    }

    @Override
    public void run() {
        try {
            while (true) {
                String serverReply = reader.readLine();
                
                if (serverReply == null) {
                    break;
                }
                
                output.println(serverReply);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                reader.close();
                output.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
