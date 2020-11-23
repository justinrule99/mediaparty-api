package com.mediaparty.api.repositories;

import com.mediaparty.api.models.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface MessageRepository extends CrudRepository<Message, Long>  {

    List<Message> findAllBySenderNameAndReceiverName(String senderName, String receiverName);
    List<Message> findAllByJoinCode(int joinCode);
}
