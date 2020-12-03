package com.mediaparty.api.controllers;

import com.mediaparty.api.models.User;
import com.mediaparty.api.repositories.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(value = "UserController", description = "REST API for various User operations (Authors: Justin Rule and Ale Groe)")
@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Operation(summary = "Create a new user based on username, password, and email")
    @CrossOrigin
    @PostMapping("/create-user")
    public @ResponseBody User addNewUser (@RequestBody User body) {
        // make object of model type and save in db with userRepository.save()
        // data based on User class
        // must know structure of data before processing
        System.out.println(body);
        User duplicate = userRepository.findByUsername(body.getUsername());
        if(duplicate != null){
            return null;
        }
        userRepository.save(body);
        return body;
    }

    @Operation(summary = "Attempt to login a user given a username and password")
    @CrossOrigin
    @PostMapping("/login")
    public @ResponseBody User loginUser(@RequestBody User body) {
        User validate = userRepository.findByUsername(body.getUsername());
        if(validate == null){
            return null;
        }

        if(!(validate.getHashedPassword().equals(body.getHashedPassword()))){
            return null;
        }

        return validate;
    }

    @Operation(summary = "Return a list of all registered users")
    @GetMapping("/get-all-users")
    public Iterable<User> getUser() {
        return userRepository.findAll();
    }

    @Operation(summary = "Get a specific user based on a userId")
    @GetMapping("/get-user/{user}")
    public User getUser(@PathVariable("user") long id) {
        // look up username in db and return more info
        return userRepository.findById(id);
    }

    @Operation(summary = "Get a specific user based on a username")
    @GetMapping("/get-by-username/{user}")
    public User getByUsername(@PathVariable("user") String username) {
        return userRepository.findByUsername(username);
    }

    @Operation(summary = "Delete a user from a username")
    @DeleteMapping("/delete-user/{user}")
    public String deleteUser(@PathVariable("user") String username) {
        userRepository.deleteByUsername(username);
        return "User successfully deleted.";
    }
}
