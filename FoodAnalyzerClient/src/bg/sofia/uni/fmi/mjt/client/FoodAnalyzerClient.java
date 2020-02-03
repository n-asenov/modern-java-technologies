package bg.sofia.uni.fmi.mjt.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;

import bg.sofia.uni.fmi.mjt.client.request.ClientRequestHandler;
import bg.sofia.uni.fmi.mjt.client.response.ServerResponseHandler;
import bg.sofia.uni.fmi.mjt.reader.BarcodeReader;

public class FoodAnalyzerClient {
    private static final String SERVER_NAME = "localhost";
    private static final int SERVER_PORT = 10_000;

    private static final String ENCODING = "UTF-8";

    private Runnable clientRequestHandler;
    private Runnable serverResponseHandler;

    public FoodAnalyzerClient(SocketChannel socketChannel, InputStream input, OutputStream output) {
        clientRequestHandler = new ClientRequestHandler(input,
                Channels.newWriter(socketChannel, ENCODING),
                new BarcodeReader());
        serverResponseHandler = new ServerResponseHandler(
                Channels.newReader(socketChannel, ENCODING), output);
    }

    public static void main(String[] args) {
        try (SocketChannel socketChannel = SocketChannel.open()) {
            socketChannel.connect(new InetSocketAddress(SERVER_NAME, SERVER_PORT));

            FoodAnalyzerClient client = new FoodAnalyzerClient(socketChannel, System.in,
                    System.out);

            client.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        new Thread(serverResponseHandler).start();
        clientRequestHandler.run();
    }
}
