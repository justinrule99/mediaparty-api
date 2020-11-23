package com.mediaparty.api.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Id;

@Entity
public class Message implements Comparable<Message>{

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long messageId;

    private int joinCode;

    private String senderName;
    // null if to a session
    private String receiverName;

    private String msg;

    @Temporal(TemporalType.TIMESTAMP)
    private Date sent = new Date();

    public Message() {}

    // 2 constructors:
    // one for chatting as a DM, one for chatting to entire session

    // To session
    public Message(String senderName, int joinCode, String msg) {
        this.senderName = senderName;
        this.joinCode = joinCode;
        this.msg = msg;
    }

    // Direct message
    public Message(String senderName, String receiverName, String msg) {
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.msg = msg;
    }

    @Override
    public int compareTo(Message message2) {
        return getDate().compareTo(message2.getDate());
    }

    public long getJoinCode() {
        return joinCode;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getMsg() {
        return msg;
    }

    public Date getDate() {
        return sent;
    }
}
