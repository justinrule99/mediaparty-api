package com.mediaparty.api.sockets;

import com.mediaparty.api.models.Message;
import com.mediaparty.api.repositories.MessageRepository;
import com.mediaparty.api.repositories.WatchSessionRepository;
import com.mediaparty.api.models.*;
import com.mediaparty.api.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Controls chatting within the context of a watch session
 */
@Controller
@ServerEndpoint(value = "/chat/{username}/{joinCode}", encoders = VideoTextEncoder.class, decoders = VideoTextDecoder.class)
public class ChatSocket {
    //Check if merged correctly

    private static MessageRepository msgRepo;
    private static WatchSessionRepository sessionRepository;
    private int joinCode;
    // needs to be sent this by frontend
    private Video video;
    private static ArrayList<String> youtubeId = new ArrayList<>();
    private static ArrayList<String> youtubeTitle= new ArrayList<>();
    private static ArrayList<String> youtubeDesc= new ArrayList<>();
    private static ArrayList<String> youtubeThumb= new ArrayList<>();


    @Autowired
    public void setMessageRepository(MessageRepository repo) {
        msgRepo = repo;
    }
    @Autowired
    public void setSessionRepository(WatchSessionRepository sessionRepo) {
        sessionRepository = sessionRepo;
    }

    // Store all socket session and their corresponding username.
    private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
    private static Map<String, Session> usernameSessionMap = new Hashtable<>();
    private static Map<String, Integer> usernameJoinCodeMap = new Hashtable<>();


    // also handles video synchronization
    // on pause, play, new video, scrub, send Video object to ALL in session
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username, @PathParam("joinCode") String strJoinCode)
            throws IOException {

        // exception when opening a session that doesn't exist
        joinCode = Integer.parseInt(strJoinCode);
        // need to get current video (can we keep updated in db?)
        System.out.println(video);
        youtubeId = new ArrayList<>();
        youtubeDesc = new ArrayList<>();
        youtubeThumb = new ArrayList<>();
        youtubeTitle = new ArrayList<>();

        WatchSession curSession = sessionRepository.findByJoinCode(joinCode);
        if (curSession == null) {
            throw new IOException("Error: Session not found");
        }

        // store connecting user information
        // need to get elsewhere
        // store join code in a map? key=username, value=session
        sessionUsernameMap.put(session, username);
        usernameSessionMap.put(username, session);
        usernameJoinCodeMap.put(username, joinCode);

        // is this correct for many sessions?
        List<String> usrs = getConnectedUsers(joinCode);
        // send this on entry
        StringBuilder usersMessage = new StringBuilder("USERLIST:");
        for (String user : usrs) {
            usersMessage.append(user);
            usersMessage.append(",");
        }
        // delete the last comma
        usersMessage.deleteCharAt(usersMessage.length()-1);
        broadcast(usersMessage.toString());

        // broadcast that new user joined
        String message = "User:" + username + " has Joined the Chat";
        broadcast(message);

        // send history only to new user
        usernameSessionMap.get(username).getBasicRemote().sendText(getSessionChatHistory());
    }

    // is this called for all types of send? (yes)
    // need one video object active for each session
    @OnMessage
    public void onMessage(Session session, String message) throws IOException, DecodeException {
        String username = sessionUsernameMap.get(session);

        // need to get room code
        // do nothing if username doesn't match joinCode
        int jcFromMessage = usernameJoinCodeMap.get(username);
        System.out.println("jc from message: "+jcFromMessage);
        System.out.println("jc from class: "+joinCode);
        // if username isn't in session

        if (message.charAt(0) == '{') {

            video = new VideoTextDecoder().decode(message);
            if (video.getYoutubeId() == null) {
                System.out.println("Not sending incomplete video");
                return;
            }
            // who sent this message?
            sendVideoToAll(video, username);
            System.out.println(video);
            return;
        }

        broadcast(username + ": " + message);

        // Saving chat
        msgRepo.save(new Message(username, joinCode, message));
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        System.out.println("session closed");
        // remove the user connection information
        String username = sessionUsernameMap.get(session);
        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);
        usernameJoinCodeMap.remove(username);

        // broadcast that the user disconnected
        System.out.println("session closed and removed user");
        String message = username + " disconnected";
        if (!usernameSessionMap.isEmpty()) {
            broadcast(message);
        }
    }


    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
        System.out.println("error");
        throwable.printStackTrace();
    }

    public boolean canSendToUser(String username) {
        // matches sessions to users so we send to correct people
        return usernameJoinCodeMap.get(username) == joinCode;
    }

    public List<String> getConnectedUsers(int httpJoinCode) {

        List<String> list = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : usernameJoinCodeMap.entrySet()) {
            if (entry.getValue() == httpJoinCode) {
                list.add(entry.getKey());
            }
        }

        return list;
    }


    public String getSessionChatHistory() {
        StringBuilder sb = new StringBuilder();

        List<Message> prevMessages = msgRepo.findAllByJoinCode(joinCode);

        if (prevMessages != null && prevMessages.size() != 0) {
            for (Message m : prevMessages) {
                sb.append(m.getSenderName()+": "+m.getMsg()+"\n");
            }
        }

        return sb.toString();
    }

    // this sends a json video object as plain text
    // will not send to a specific user
    private void sendVideoToAll(Video video, String senderUsername) {
        sessionUsernameMap.forEach((session, username) -> {
            try {
                if (!(username.equals(senderUsername))) {
                    if (canSendToUser(username)) {
                        session.getBasicRemote().sendObject(video);
                    }
                }
            }
            catch (IOException | EncodeException e) {
                System.out.println("Error: Could not send video object");
                e.printStackTrace();
            }
        });

    }

    // sends message to ALL connected users
    public void broadcast(String message) {
        sessionUsernameMap.forEach((session, username) -> {
            try {
                // only send if username -> joincode from map matches joinCode
                if (canSendToUser(username)) {
                    session.getBasicRemote().sendText(message);
                }
            }
            catch (IOException e) {
                System.out.println("Error:");
                e.printStackTrace();
            }
        });
    }
}
