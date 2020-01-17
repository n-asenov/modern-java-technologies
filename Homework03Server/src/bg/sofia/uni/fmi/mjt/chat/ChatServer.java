package bg.sofia.uni.fmi.mjt.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ChatServer {
    private static final String SERVER_HOST = "localhost";
    public static final int SERVER_PORT = 10_000;
    private static final int BUFFER_SIZE = 1024;
    private static final int SLEEP_MILLIS = 500;

    private static final String USERNAME_IS_TAKEN = "Username is already taken. Please enter another username!";
    private static final String CLIENT_ALREADY_REGISTERED = "You have already registered!";
    private static final String OFFLINE_USER = "User seems to be offline!";
    private static final String DISCONNECTED = "You have been disconnected!";

    private Map<String, SocketChannel> activeUsers;
    private Map<SocketChannel, String> activeSockets;
    private ByteBuffer buffer;

    public ChatServer() {
        activeUsers = new HashMap<>();
        activeSockets = new HashMap<>();
        buffer = ByteBuffer.allocate(BUFFER_SIZE);
    }

    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        server.run();
    }

    public Set<String> getActiveUsers() {
        return new HashSet<String>(activeUsers.keySet());
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

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();

                    if (key.isReadable()) {
                        SocketChannel client = (SocketChannel) key.channel();
                        String command = getCommand(client);
                        executeCommand(command, client);
                    } else if (key.isAcceptable()) {
                        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
                        SocketChannel accept = sockChannel.accept();
                        accept.configureBlocking(false);
                        accept.register(selector, SelectionKey.OP_READ);
                    }

                    keyIterator.remove();
                }
            }
        } catch (IOException e) {
            System.out.println("There is a problem with the server socket");
            e.printStackTrace();
        }
    }
    
    public void executeCommand(String line, SocketChannel client) throws IOException {
        String[] tokens = parseLine(line);

        String command = tokens[0];

        if (command.equals("nick")) {
            registerUser(tokens[1], client);
        } else if (command.equals("send")) {
            sendMessage(getMessage(tokens, 2), tokens[1], client);
        } else if (command.equals("send-all")) {
            sendMessageToAllActiveUsers(getMessage(tokens, 1), client);
        } else if (command.equals("list-users")) {
            listActiveUsers(client);
        } else if (command.equals("disconnect")) {
            disconnectUser(client);
        }
    }

    private void sleep() {
        try {
            Thread.sleep(SLEEP_MILLIS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String getCommand(SocketChannel client) throws IOException {
        buffer.clear();
        client.read(buffer);
        buffer.flip();
        byte[] command = new byte[buffer.limit()];
        buffer.get(command);

        return new String(command);
    }

    private String[] parseLine(String line) {
        return line.strip().split(" ");
    }

    private String getMessage(String[] tokens, int index) {
        StringBuilder message = new StringBuilder();

        for (int i = index; i < tokens.length; i++) {
            message.append(tokens[i]).append(" ");
        }

        return message.toString();
    }

    private void registerUser(String username, SocketChannel client) throws IOException {
        if (activeSockets.containsKey(client)) {
            sendReply(CLIENT_ALREADY_REGISTERED, client);
        } else if (activeUsers.containsKey(username)) {
            sendReply(USERNAME_IS_TAKEN, client);
        } else {
            activeUsers.put(username, client);
            activeSockets.put(client, username);
        }
    }

    private void sendMessage(String message, String receiverUsername, SocketChannel sender)
            throws IOException {
        if (!activeUsers.containsKey(receiverUsername)) {
            sendReply(OFFLINE_USER, sender);
        } else {
            SocketChannel receiver = activeUsers.get(receiverUsername);
            String receiverMessage = "[" + LocalDateTime.now() + "] " + activeSockets.get(sender)
                    + ": " + message;
            sendReply(receiverMessage, receiver);
        }
    }

    private void sendMessageToAllActiveUsers(String message, SocketChannel sender)
            throws IOException {
        for (String activeUser : activeUsers.keySet()) {
            if (!activeSockets.get(sender).equals(activeUser)) {
                sendMessage(message, activeUser, sender);
            }
        }
    }

    private void listActiveUsers(SocketChannel client) throws IOException {
        sendReply(activeUsers.keySet().toString(), client);
    }

    private void disconnectUser(SocketChannel client) throws IOException {
        activeUsers.remove(activeSockets.get(client));
        activeSockets.remove(client);
        sendReply(DISCONNECTED, client);
        client.close();
    }

    private void sendReply(String message, SocketChannel client) throws IOException {
        message = message + System.lineSeparator();
        byte[] messageInBytes = message.getBytes();

        buffer.clear();
        buffer.put(messageInBytes);
        buffer.flip();
        client.write(buffer);
    }
}
