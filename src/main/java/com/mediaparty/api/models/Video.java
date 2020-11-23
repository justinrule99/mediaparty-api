package com.mediaparty.api.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediaparty.api.util.Utils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Video {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long videoId;

    private String title;
    private String youtubeId;
    // where was video at last event?
    private int seconds;
    private Utils.VIDEO_STATE playState;


    public Video(String title, String youtubeId){
        this.title = title;
        this.youtubeId = youtubeId;
    }

    public Video(String title, String youtubeId, int seconds, Utils.VIDEO_STATE state) {
        this.title = title;
        this.youtubeId = youtubeId;
        this.seconds = seconds;
        playState = state;
    }

    public Video() {

    }

    public long getVideoId() {
        return videoId;
    }

    public String getTitle() { return title; }

    public String getYoutubeId() { return youtubeId; }

    public void setTitle(String title) { this.title = title; }

    public void setYoutubeId(String youtubeId) { this.youtubeId = youtubeId; }

    public int getSeconds() {
        return seconds;
    }

    public Utils.VIDEO_STATE getPlayState() {
        return playState;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public void setPlayState(Utils.VIDEO_STATE playState) {
        this.playState = playState;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Error: Could not convert video object to String";
        }
    }
}
