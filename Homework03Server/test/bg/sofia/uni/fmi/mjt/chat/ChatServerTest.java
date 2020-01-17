package bg.sofia.uni.fmi.mjt.chat;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atMostOnce;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.junit.Before;
import org.junit.Test;

public class ChatServerTest {
    private ChatServer server;
    private SocketChannel firstClient;
    
    @Before
    public void initialize() throws IOException {
        server = new ChatServer();
        firstClient = mock(SocketChannel.class);
        server.executeCommand("nick kolyo", firstClient);
    }

    @Test
    public void testExecuteCommandWithRegisteringNewUser() throws IOException {
        final int activeUsers = 1;
        String user = "kolyo";

        verify(firstClient, never()).write(any(ByteBuffer.class));

        assertEquals(activeUsers, server.getActiveUsers().size());
        assertTrue(server.getActiveUsers().contains(user));
    }

    @Test
    public void testExecuteCommandWithRegisteringUserWithAlreadyTakeUsername() throws IOException {
        String command = "nick kolyo";
        final int activeUsers = 1;
        String user = "kolyo";

        SocketChannel secondClient = mock(SocketChannel.class);

        server.executeCommand(command, secondClient);

        verify(firstClient, never()).write(any(ByteBuffer.class));
        verify(secondClient, atMostOnce()).write(any(ByteBuffer.class));

        assertEquals(activeUsers, server.getActiveUsers().size());
        assertTrue(server.getActiveUsers().contains(user));
    }

    @Test
    public void testExecuteCommandWithRegisteringAlreadyRegisteredUser() throws IOException {
        String command = "nick kolyo";
        final int activeUsers = 1;
        String user = "kolyo";

        server.executeCommand(command, firstClient);

        verify(firstClient, atMostOnce()).write(any(ByteBuffer.class));

        assertEquals(activeUsers, server.getActiveUsers().size());
        assertTrue(server.getActiveUsers().contains(user));
    }

    @Test
    public void testExecuteCommandWithSendingMessageToAnotherClient() throws IOException {
        String command = "send kolyo hi";
        final int activeUsers = 2;

        SocketChannel secondClient = mock(SocketChannel.class);

        server.executeCommand("nick gosho", secondClient);
        server.executeCommand(command, secondClient);

        verify(firstClient, atMostOnce()).write(any(ByteBuffer.class));
        verify(secondClient, never()).write(any(ByteBuffer.class));

        assertEquals(activeUsers, server.getActiveUsers().size());
    }

    @Test
    public void testExecuteCommandWithSendingMessageToOfflineUser() throws IOException {
        String command = "send gosho hi";
        final int activeUsers = 1;

        server.executeCommand(command, firstClient);

        verify(firstClient, atMostOnce()).write(any(ByteBuffer.class));

        assertEquals(activeUsers, server.getActiveUsers().size());
    }
    
    @Test
    public void testExecuteCommandWithSendingMessageToAllActiveUsers() throws IOException {
        String command = "send-all hi";
        final int activeUsers = 2;

        SocketChannel secondClient = mock(SocketChannel.class);

        server.executeCommand("nick gosho", secondClient);
        server.executeCommand(command, secondClient);

        verify(firstClient, atMostOnce()).write(any(ByteBuffer.class));
        verify(secondClient, never()).write(any(ByteBuffer.class));

        assertEquals(activeUsers, server.getActiveUsers().size());
    }
    
    @Test
    public void testExecuteCommandWithListingAllActiveUsers() throws IOException {
        String command = "list-users hi";
        final int activeUsers = 1;

        server.executeCommand(command, firstClient);

        verify(firstClient, atMostOnce()).write(any(ByteBuffer.class));

        assertEquals(activeUsers, server.getActiveUsers().size());
    }

}
