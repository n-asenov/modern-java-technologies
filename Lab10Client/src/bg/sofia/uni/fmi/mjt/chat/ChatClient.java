package bg.sofia.uni.fmi.mjt.chat;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class ChatClient {
    private static final String SERVER_NAME = "localhost";
    private static final int SERVER_PORT = 10_000;
    private static final String ENCODING = "UTF-8";
    private static final String DISCONNECT_COMMAND = "disconnect";
    
    public static void main(String[] args) {
        ChatClient client = new ChatClient();
        client.run();
    }
    
    private void run() {
        try (SocketChannel socketChannel = SocketChannel.open();
                PrintWriter writer = new PrintWriter(Channels.newWriter(socketChannel, ENCODING), true);
                Scanner scanner = new Scanner(System.in)) {
            
            socketChannel.connect(new InetSocketAddress(SERVER_NAME, SERVER_PORT));
            
            Thread serverResponseHandler = new Thread(new ServerResponseHandler(socketChannel));
            serverResponseHandler.start();
            
            while (true) {
                String command = scanner.nextLine();

                writer.println(command);

                if (command.equals(DISCONNECT_COMMAND)) {
                    serverResponseHandler.join();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
