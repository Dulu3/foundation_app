package com.piotrdulewski.foundationapp.rest;


import com.piotrdulewski.foundationapp.entity.BenefitUser;
import com.piotrdulewski.foundationapp.entity.Request;
import com.piotrdulewski.foundationapp.entity.User;
import com.piotrdulewski.foundationapp.service.UserService;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "This method is used to get list of all users",
            authorizations = @io.swagger.annotations.Authorization(value = "Authorization"))
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }


    @ApiOperation(value = "This method is used to user with a given id",
            authorizations = @io.swagger.annotations.Authorization(value = "Authorization"))
    @GetMapping("/users/{userId}")
    public User getUser(@PathVariable long userId) {
        return userService.findById(userId);
    }


    @ApiOperation(value = "This method is used to get list of requests belonging to a given user",
            authorizations = @io.swagger.annotations.Authorization(value = "Authorization"))
    @GetMapping("/userRequests/{userId}")
    public List<Request> getUserRequest(@PathVariable long userId) {
        User user = userService.findById(userId);
        return user.getCreatedRequests();
    }

    @ApiOperation(value = "This method is used to get list of benefits belonging to a given user",
            authorizations = @io.swagger.annotations.Authorization(value = "Authorization"))
    @GetMapping("/userBenefits/{userId}")
    public List<BenefitUser> getUserBenefit(@PathVariable long userId) {
        User user = userService.findById(userId);
        return user.getBenefits();
    }

    @ApiOperation(value = "This method is used to add new user",
            authorizations = @io.swagger.annotations.Authorization(value = "Authorization"))
    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        user.setId(null);
        userService.save(user);
        return user;
    }

    @ApiOperation(value = "This method is used to update user",
            authorizations = @io.swagger.annotations.Authorization(value = "Authorization"))
    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        userService.save(user);
        return user;
    }
}