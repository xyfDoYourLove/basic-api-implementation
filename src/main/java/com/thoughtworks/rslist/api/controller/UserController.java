package com.thoughtworks.rslist.api.controller;

import com.thoughtworks.rslist.Data.Data;
import com.thoughtworks.rslist.api.service.UserService;
import com.thoughtworks.rslist.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/user/register")
    public ResponseEntity registerUser(@RequestBody @Valid User user) {
        userService.registerUser(user);
        return ResponseEntity.created(null).header("index", String.valueOf(Data.userList.size() - 1)).build();
    }

    @GetMapping("/get/users")
    public ResponseEntity getUserList() {
        return ResponseEntity.ok(userService.getUserList());
    }

}
