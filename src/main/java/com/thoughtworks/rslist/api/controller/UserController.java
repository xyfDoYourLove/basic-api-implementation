package com.thoughtworks.rslist.api.controller;

import com.thoughtworks.rslist.api.service.UserService;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/user/register")
    public ResponseEntity registerUser(@RequestBody @Valid User user) {
        ResponseEntity responseEntity = userService.registerUser(user);
        return responseEntity;
    }

    @GetMapping("/get/users")
    public ResponseEntity getUserList() {
        List<UserDto> userList = userService.getUserList();
        return ResponseEntity.ok(userList);
    }

    @GetMapping("/get/user")
    public ResponseEntity<User> getUserById(@RequestParam int id) {
        User userById = userService.getUserById(id);
        if (userById != null) {
            return ResponseEntity.ok(userById);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteUserById(@PathVariable int id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok().build();
    }

}
