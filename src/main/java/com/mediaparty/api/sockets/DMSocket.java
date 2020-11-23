package com.mediaparty.api.sockets;

import com.mediaparty.api.models.Message;
import com.mediaparty.api.models.User;
import com.mediaparty.api.repositories.MessageRepository;
import com.mediaparty.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;

/**
 * Controls direct messages between two friends
 * Open this connection on client when a user clicks on a friend
 * Then it will show their chat history and allow for chatting
 */


@Controller
@ServerEndpoint(value = "/direct-message/{sender}/{recipient}")
public class DMSocket {

    private static MessageRepository msgRepo;
    private static UserRepository userRepository;
    private String senderUsername;
    private String recUsername;

    @Autowired
    public void setMessageRepository(MessageRepository repo) {
        msgRepo = repo;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepo) {
        userRepository = userRepo;
    }

    // Store all socket session and their corresponding username.
    private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
    private static Map<String, Session> usernameSessionMap = new Hashtable<>();


    @OnOpen
    public void onOpen(Session session, @PathParam("sender") String sender, @PathParam("recipient") String recipient)
            throws IOException {

        // get sender and recipient
        User senderUser = userRepository.findByUsername(sender);
        User recUser = userRepository.findByUsername(recipient);

        if (senderUser == null || recUser == null) {
            throw new IOException("ERROR: Invalid username. Cannot chat");
        }

        recUsername = recipient;
        senderUsername = sender;

        // store connecting user information
        sessionUsernameMap.put(session, sender);
        usernameSessionMap.put(sender, session);

        // get chat history for user that just joined
        usernameSessionMap.get(sender).getBasicRemote().sendText(getDirectMessageHistory(sender, recipient));
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        // DM endpoint
        if (message == null) return;

        String username = sessionUsernameMap.get(session);

        broadcast(username + ": " + message);

        // recipient: OTHER USER IN SESSION
        msgRepo.save(new Message(username, recUsername, message));
    }


    @OnClose
    public void onClose(Session session) throws IOException {
        System.out.println("session closed");
        // remove the user connection information
        String username = sessionUsernameMap.get(session);
        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);
        System.out.println("end onclose");
    }


    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
        System.out.println("error");
        throwable.printStackTrace();
    }

    // need to get both ways in order
    private String getDirectMessageHistory(String sender, String recipient) {
        StringBuilder sb = new StringBuilder();

        List<Message> messagesSent = msgRepo.findAllBySenderNameAndReceiverName(sender, recipient);
        List<Message> messagesReceived = msgRepo.findAllBySenderNameAndReceiverName(recipient, sender);

        if (messagesSent == null){
            for (Message m : messagesReceived) {
                sb.append(m.getSenderName()+": "+m.getMsg()+"\n");
            }
            return sb.toString();
        }

        // db not necessarily in order, need to reorder
        messagesSent.addAll(messagesReceived);
        Collections.sort(messagesSent);

        for (Message m : messagesSent) {
            sb.append(m.getSenderName()+": "+m.getMsg()+"\n");
        }

        return sb.toString();
    }

    private void broadcast(String message) {
        sessionUsernameMap.forEach((session, username) -> {
            try {
                session.getBasicRemote().sendText(message);
            }
            catch (IOException e) {
                System.out.println("Error:");
                e.printStackTrace();
            }
        });
    }
}
