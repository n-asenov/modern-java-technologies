package bg.sofia.uni.fmi.mjt.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.http.HttpClient;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import bg.sofia.uni.fmi.mjt.api.FoodDataApiClient;
import bg.sofia.uni.fmi.mjt.api.InvalidFoodIdException;
import bg.sofia.uni.fmi.mjt.api.NoMatchException;
import bg.sofia.uni.fmi.mjt.cache.ServerCache;
import bg.sofia.uni.fmi.mjt.commands.Command;
import bg.sofia.uni.fmi.mjt.commands.CommandFactory;
import bg.sofia.uni.fmi.mjt.commands.exceptions.InternalServerProblemException;
import bg.sofia.uni.fmi.mjt.commands.exceptions.InvalidCommandException;
import bg.sofia.uni.fmi.mjt.commands.exceptions.InvalidNumberOfArgumentsException;
import bg.sofia.uni.fmi.mjt.parser.ClientMessageParser;

public class FoodAnalyzerServer {
	public static final int SERVER_PORT = 10_000;
	private static final String SERVER_HOST = "localhost";

	private static final int SLEEP_MILLIS = 500;
	private static int BUFFER_SIZE = 1024;
	private static int MORE_BYTES_TO_READ = 0;

	private ServerCache serverCache;
	private FoodDataApiClient apiClient;
	private CommandFactory commandFactory;
	private ClientMessageParser parser;
	private ByteBuffer inputBuffer;

	public FoodAnalyzerServer(String apiKey, String foodStorageName, String brandedFoodStorageName,
			String foodDetailsStorageName) throws IOException {
		serverCache = new ServerCache(foodStorageName, brandedFoodStorageName, foodDetailsStorageName);
		apiClient = new FoodDataApiClient(HttpClient.newHttpClient(), apiKey);
		commandFactory = new CommandFactory(serverCache, apiClient);
		parser = new ClientMessageParser();
		inputBuffer = ByteBuffer.allocate(BUFFER_SIZE);
	}

	public static void main(String[] args) {
		final String apiKey = "YOUR_API_KEY_HERE";

		try {
			FoodAnalyzerServer server = new FoodAnalyzerServer(apiKey, "foodStorage", "brandedFoodStorage",
					"foodDetailsStorage");

			server.run();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void run() {
		try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
			serverSocketChannel.bind(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
			serverSocketChannel.configureBlocking(false);

			Selector selector = Selector.open();
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

			while (true) {
				int readyChannels = selector.select();

				if (readyChannels == 0) {
					sleep();
					continue;
				}

				handleReadyChannels(selector);
			}
		} catch (IOException e) {
			System.out.println("Couldn't start the server");
			e.printStackTrace();
		}
	}

	private void handleReadyChannels(Selector selector) {
		Set<SelectionKey> selectedKeys = selector.selectedKeys();
		Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

		while (keyIterator.hasNext()) {
			SelectionKey key = keyIterator.next();

			if (key.isReadable()) {
				SocketChannel client = (SocketChannel) key.channel();
				String clientRequest = getRequestFromClient(client);
				String response = handleClientRequest(clientRequest);
				sendResponse(client, response);
			} else if (key.isAcceptable()) {
				ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
				try {
					SocketChannel accept = sockChannel.accept();
					accept.configureBlocking(false);
					accept.register(selector, SelectionKey.OP_READ);
				} catch (IOException e) {
					System.out.println("Couldn't establish new connection");
					e.printStackTrace();
				}
			}

			keyIterator.remove();
		}
	}

	private String getRequestFromClient(SocketChannel client) {
		StringBuilder command = new StringBuilder();
		int readBytes = 1;

		try {
			while (readBytes > MORE_BYTES_TO_READ) {
				inputBuffer.clear();
				readBytes = client.read(inputBuffer);
				inputBuffer.flip();
				if (readBytes > MORE_BYTES_TO_READ) {
					byte[] cmd = new byte[readBytes];
					inputBuffer.get(cmd);
					command.append(new String(cmd));
				}
			}
		} catch (IOException e) {
			System.out.println("Couldn't get client request");
			e.printStackTrace();
		}

		return command.toString();
	}

	private String handleClientRequest(String clientRequest) {
		try {
			Command command = commandFactory.make(parser.getCommand(clientRequest));
			return command.execute(parser.getArguments(clientRequest));
		} catch (InvalidCommandException | InvalidNumberOfArgumentsException | InvalidFoodIdException
				| NoMatchException e) {
			return e.getMessage();
		} catch (InternalServerProblemException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	private void sendResponse(SocketChannel client, String message) {
		message = message + System.lineSeparator();
		byte[] messageInBytes = message.getBytes();

		ByteBuffer response = ByteBuffer.allocate(messageInBytes.length);
		response.put(messageInBytes);
		response.flip();
		try {
			client.write(response);
		} catch (IOException e) {
			try {
				client.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void sleep() {
		try {
			Thread.sleep(SLEEP_MILLIS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
