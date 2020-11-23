package com.mediaparty.api.models;

// User model class

import com.mediaparty.api.util.Utils;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long userId;

    private String username;
    private String email;
    private String hashedPassword;
    private Utils.USER_TYPE userType;

    public User(String username, String email, String hashedPassword, Utils.USER_TYPE userType) {
        this.username = username;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.userType = userType;
    }

    public User() {

    }

    public long getUserId() {
        return userId;
    }

    public Utils.USER_TYPE getUserType() {
        return userType;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    @Override
    public String toString() {
        return "Username: "+username+"\n Email: "+email+"\n";
    }
}