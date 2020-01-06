package bg.sofia.uni.fmi.mjt.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;

public class ServerResponseHandler implements Runnable {
    private SocketChannel socket;

    public ServerResponseHandler(SocketChannel socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(Channels.newReader(socket, "UTF-8"))) {

            while (true) {
                String reply = reader.readLine();
                
                if (reply == null) {
                    break;
                }
                
                System.out.println(reply);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
