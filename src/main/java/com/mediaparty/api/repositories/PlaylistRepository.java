package com.mediaparty.api.repositories;

import com.mediaparty.api.models.*;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface PlaylistRepository extends CrudRepository<Playlist, Long>  {

    List<Playlist> findAllBySessionId(long sessionId);

    void deletePlaylistBySessionIdAndVideoId(long sessionId, String video);

}
