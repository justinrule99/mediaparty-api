package com.mediaparty.api.repositories;

import com.mediaparty.api.models.Friend;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FriendRepository extends CrudRepository<Friend, Long> {

    Friend findByUser1IdAndUser2Id(long user1Id, long user2Id);

    List<Friend> findAllByUser1Id(long user1Id);
    List<Friend> findAllByUser2Id(long user2Id);
}
