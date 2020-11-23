package com.mediaparty.api.repositories;

import com.mediaparty.api.models.WatchSession;
import org.springframework.data.repository.CrudRepository;

public interface WatchSessionRepository extends CrudRepository<WatchSession, Long> {

    WatchSession findByJoinCode(int joinCode);
    WatchSession findBySessionId(long sessionId);

    void deleteSessionBySessionId(long sessionId);


}
