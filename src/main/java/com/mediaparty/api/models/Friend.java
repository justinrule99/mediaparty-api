package com.mediaparty.api.models;

import com.mediaparty.api.util.Utils;

import javax.persistence.*;

@Entity
public class Friend {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long friendId;

    private long user1Id;
    private String user1username;
    private long user2Id;
    private String user2username;
    private Utils.FRIEND_STATUS status;

    public Friend() {

    }

    public Friend(long user1Id, String user1username, long user2Id, String user2username, Utils.FRIEND_STATUS status) {
        this.user1Id = user1Id;
        this.user1username = user1username;
        this.user2Id = user2Id;
        this.user2username = user2username;
        this.status = status;
    }

    public Friend(String user1username, String user2username, Utils.FRIEND_STATUS status) {
        this.user1username = user1username;
        this.user2username = user2username;
        this.status = status;
    }

    public String getUser1username() {
        return user1username;
    }

    public String getUser2username() {
        return user2username;
    }

    public void setUser1username(String user1username) {
        this.user1username = user1username;
    }

    public void setUser2username(String user2username) {
        this.user2username = user2username;
    }

    public long getFriendId() {
        return friendId;
    }

    public void setFriendId(long friendId) {
        this.friendId = friendId;
    }

    public long getUser1Id() {
        return user1Id;
    }

    public Utils.FRIEND_STATUS getStatus() {
        return status;
    }

    public void setUser1Id(long user1Id) {
        this.user1Id = user1Id;
    }

    public long getUser2Id() {
        return user2Id;
    }

    public void setUser2Id(long user2Id) {
        this.user2Id = user2Id;
    }

    public void setStatus(Utils.FRIEND_STATUS status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object friend) {
        // cast
        Friend f = (Friend) friend;

        return user1Id == f.user1Id && user1username.equals(f.user1username)
                && user2Id == f.user2Id && user2username.equals(f.user2username);
    }
}
