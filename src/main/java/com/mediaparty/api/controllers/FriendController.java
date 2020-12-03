package com.mediaparty.api.controllers;


import com.mediaparty.api.models.Friend;
import com.mediaparty.api.models.User;
import com.mediaparty.api.repositories.FriendRepository;
import com.mediaparty.api.repositories.UserRepository;
import com.mediaparty.api.util.Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "FriendController", description = "REST API for various Friend operations (Author: Justin Rule)")
@RestController
public class FriendController {

    @Autowired
    FriendRepository friendRepository;

    @Autowired
    UserRepository userRepository;

    @Operation(summary = "Create a pending friendship between two users")
    @CrossOrigin
    @PostMapping("/add-friend/{username1}/{username2}")
    public @ResponseBody
    Friend addFriend (@PathVariable("username1") String username1, @PathVariable("username2") String username2) {

        if (username1.equals(username2)) {
            System.out.println("Error: Cannot add yourself as a friend");
            return null;
        }

        // get both based on username
        User user1 = userRepository.findByUsername(username1);
        User user2 = userRepository.findByUsername(username2);

        if (user1 == null || user2 == null) {
            System.out.println("Error: Users not found");
            return null;
        }

        // don't add friend multiple times
        Friend existingFriend = friendRepository.findByUser1IdAndUser2Id(user1.getUserId(), user2.getUserId());
        if (existingFriend != null) {
            System.out.println("Friend already exists");
            return null;
        }

        Friend existingFriendReverse = friendRepository.findByUser1IdAndUser2Id(user2.getUserId(), user1.getUserId());
        if (existingFriendReverse != null) {
            System.out.println("Friend already exists (other way)");
            return null;
        }

        Friend friend = new Friend(user1.getUserId(), user1.getUsername(), user2.getUserId(), user2.getUsername(), Utils.FRIEND_STATUS.PENDING);

        friendRepository.save(friend);

        return friend;
    }

    @Operation(summary = "Accept a pending friend request")
    @CrossOrigin
    @PutMapping("/accept-friend/{username1}/{username2}")
    public Friend updateFriend(@PathVariable("username1") String username1, @PathVariable("username2") String username2) {
        User user1 = userRepository.findByUsername(username1);
        User user2 = userRepository.findByUsername(username2);

        Friend friend = friendRepository.findByUser1IdAndUser2Id(user1.getUserId(), user2.getUserId());
        if (friend == null) {
            friend = friendRepository.findByUser1IdAndUser2Id(user2.getUserId(), user1.getUserId());
        }

        friend.setStatus(Utils.FRIEND_STATUS.ACCEPTED);
        return friendRepository.save(friend);
    }

    @Operation(summary = "Delete a friend relationship between two users")
    @CrossOrigin
    @DeleteMapping("/remove-friend/{username1}/{username2}")
    public String deleteFriend(@PathVariable("username1") String username1, @PathVariable("username2") String username2) {

        User user1 = userRepository.findByUsername(username1);
        User user2 = userRepository.findByUsername(username2);

        Friend friend = friendRepository.findByUser1IdAndUser2Id(user1.getUserId(), user2.getUserId());
        if (friend == null) {
            friend = friendRepository.findByUser1IdAndUser2Id(user2.getUserId(), user1.getUserId());
        }

        if (friend == null) return "Error: Users are not friends";

        friendRepository.deleteById(friend.getFriendId());

        return "Friend successfully deleted";
    }

    @Operation(summary = "Get all friends for one user")
    @CrossOrigin
    @GetMapping("/{username}/get-friends")
    public List<Friend> getFriends(@PathVariable("username") String username) {

        User user = userRepository.findByUsername(username);
        long userId = user.getUserId();

        // get all friend objects
        List<Friend> friends1 =  friendRepository.findAllByUser1Id(userId);
        List<Friend> friends2 =  friendRepository.findAllByUser2Id(userId);

        // join lists
        friends1.addAll(friends2);
        return friends1;
    }
}
