package com.mediaparty.api.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class WatchSession {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long sessionId;

    private int chatroomId;
    private int joinCode;
    private int timestamp;
    private String hostUsername;
    private long prevWatchedVideoId;

    public WatchSession(long sessionId, int chatroomId, int joinCode, int timestamp, String hostUsername, long prevWatchedVideoId){
        this.sessionId = sessionId;
        this.chatroomId = chatroomId;
        this.joinCode = joinCode;
        this.timestamp = timestamp;
        this.hostUsername = hostUsername;
        this.prevWatchedVideoId = prevWatchedVideoId;
    }

    public WatchSession() {

    }

    public long getSessionId() {
        return sessionId;
    }

    public int getChatroomId() {
        return chatroomId;
    }

    public int getJoinCode() {
        return joinCode;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public String getHostUsername() {
        return hostUsername;
    }

    public long getPrevWatched() { return prevWatchedVideoId; }

    public void setChatroomId(int chatroomId) {
        this.chatroomId = chatroomId;
    }

    public void setJoinCode(int joinCode) {
        this.joinCode = joinCode;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public void setHostUsername(String hostUsername) {
        this.hostUsername = hostUsername;
    }

    public void setPrevWatched(long video) { prevWatchedVideoId = video ;}

}
