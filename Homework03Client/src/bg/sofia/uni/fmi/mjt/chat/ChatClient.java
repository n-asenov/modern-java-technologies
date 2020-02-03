package bg.sofia.uni.fmi.mjt.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;

public class ChatClient {
    private static final String SERVER_NAME = "localhost";
    private static final int SERVER_PORT = 10_000;

    private static final String ENCODING = "UTF-8";
    private static final String DISCONNECT_COMMAND = "disconnect";

    private SocketChannel socketChannel;
    private InputStream input;
    private Thread serverResponseHandler;

    public ChatClient(SocketChannel socketChannel, InputStream input) {
        this.socketChannel = socketChannel;
        this.input = input;
        serverResponseHandler = new Thread(
               new ServerResponseHandler(Channels.newReader(socketChannel, "UTF-8"), System.out));
        serverResponseHandler.start();
    }

    public static void main(String[] args) {
        try (SocketChannel socketChannel = SocketChannel.open()) {
            socketChannel.connect(new InetSocketAddress(SERVER_NAME, SERVER_PORT));

            ChatClient client = new ChatClient(socketChannel, System.in);

            client.run();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void run() {
        try (var reader = new BufferedReader(new InputStreamReader(input));
                var writer = new PrintWriter(Channels.newWriter(socketChannel, ENCODING), true)) {

            while (true) {
                String command = reader.readLine();
                
                if (command == null) {
                    break;
                }
                
                writer.println(command);

                if (command.equals(DISCONNECT_COMMAND)) {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
