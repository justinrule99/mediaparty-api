package com.mediaparty.api.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import com.mediaparty.api.models.*;

@Entity
public class Playlist{

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long sessionId;

    private String videoId;

    private String title;

    public Playlist() {

    }

    public Playlist (long sessionId, String videoId, String title){
        this.sessionId = sessionId;
        this.videoId = videoId;
        this.title = title;
    }

    public long getSessionId(){
        return sessionId;
    }

    public String getVideo(){
        return videoId;
    }

    public String getVideoTitle() { return title; }

    public void setSessionId(long sessionId){
        this.sessionId = sessionId;
    }

}
