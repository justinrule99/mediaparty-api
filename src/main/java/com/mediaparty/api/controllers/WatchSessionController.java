package com.mediaparty.api.controllers;

import com.mediaparty.api.models.WatchSession;
import com.mediaparty.api.models.User;
import com.mediaparty.api.repositories.WatchSessionRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import com.mediaparty.api.models.*;
import com.mediaparty.api.repositories.*;

// this may become deprecated, not useful as of 11/22/20
@Api(value = "WatchSessionController", description = "REST API for various WatchSession operations (Author: Alessandra Groe)")
@RestController
public class WatchSessionController {

    @Autowired
    WatchSessionRepository sessionRepository;

    @Autowired
    PlaylistRepository playlistRepository;

    @Operation(summary = "Get the session information")
    @GetMapping("/get-session/{sessionId}")
    public WatchSession getSession(@PathVariable("sessionId") long id) {
        return sessionRepository.findBySessionId(id);
    }

    @Operation(summary = "Create a new session")
    @PostMapping("/create-session")
    public @ResponseBody
    WatchSession createSession(@RequestBody WatchSession session) {
        int joinCode = (int) (Math.random() * 9000) + 1000;

        session.setJoinCode(joinCode);

        System.out.println(session);

        sessionRepository.save(session);
        return session;
    }

    @Operation(summary = "Update a session's information")
    @PutMapping("/update-session/{sessionId}")
    public @ResponseBody
    WatchSession updateSession(@PathVariable("sessionId") long id, @RequestBody WatchSession session) {
        WatchSession newSession = sessionRepository.findBySessionId(id);
        if (newSession == null) {
            return null;
        }

        newSession.setChatroomId(session.getChatroomId());
        newSession.setHostUsername(session.getHostUsername());
        newSession.setJoinCode(session.getJoinCode());
        newSession.setTimestamp(session.getTimestamp());

        return newSession;
    }

    @Operation(summary = "Delete a session")
    @DeleteMapping("/delete-session/{sessionId}")
    public String endSession(@PathVariable("sessionId") long id) {
        WatchSession check = sessionRepository.findBySessionId(id);
        if (check == null) {
            return "Session not found.";
        }
        sessionRepository.deleteSessionBySessionId(id);
        return "Session deleted successfully.";
    }
    @Operation(summary = "Get a session's previously watched video")
    @GetMapping("/get-prev-watched/{sessionId}")
    public long getPrevWatchedId(@PathVariable("sessionId") long id) {
        WatchSession check = sessionRepository.findBySessionId(id);
        if (check == null) {
            return 0;
        }

        return check.getPrevWatched();
    }

    @Operation(summary = "Set a session's previously watched video")
    @PostMapping("/set-prev-watched/{sessionId}")
    public @ResponseBody
    Video setPrevWatched(@PathVariable("sessionId") long id, @RequestBody Video video) {
        WatchSession check = sessionRepository.findBySessionId(id);
        if (check == null) {
            return null;
        }
        check.setPrevWatched(video.getVideoId());
        return video;
    }

    @Operation(summary = "Get a session's playlist")
    @GetMapping("/get-playlist/{sessionId}")
    public List<Playlist> getPlaylist(@PathVariable("sessionId") long sessionId) {
        List<Playlist> playlist = playlistRepository.findAllBySessionId(sessionId);

        return playlist;
    }

    @Operation(summary = "Add a video to a session's playlist")
    @PostMapping("/add-video-to-playlist/{sessionId}/{video}/{title}")
    public @ResponseBody
    Playlist addVideoToPlaylist(@PathVariable("sessionId") long sessionId, @PathVariable("video") String video, @PathVariable("title") String title) { //find better way to store a playlist/key for it
        if (video == null) {
            return null;
        }

        Playlist playlist = new Playlist(sessionId, video, title);
        playlistRepository.save(playlist);
        return playlist;
    }

    @Operation(summary = "Delete a video from a session's playlist")
    @DeleteMapping("/delete-video-from-playlist/{sessionId}/{video}")
    public @ResponseBody
    String deleteVideoFromPlaylist(@PathVariable("sessionId") long sessionId, @PathVariable("video") String video){
        if(video == null){
            return null;
        }

        playlistRepository.deletePlaylistBySessionIdAndVideoId(sessionId, video);
        return "Video Successfully deleted";
    }

}
