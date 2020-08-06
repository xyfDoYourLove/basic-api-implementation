package com.thoughtworks.rslist.api.controller;

import com.thoughtworks.rslist.common.method.DataInitMethod;
import com.thoughtworks.rslist.api.service.UserService;
import com.thoughtworks.rslist.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/user/register")
    public ResponseEntity registerUser(@RequestBody @Valid User user) {
        userService.registerUser(user);
        return ResponseEntity.created(null).header("index", String.valueOf(DataInitMethod.userList.size() - 1)).build();
    }

    @GetMapping("/get/users")
    public ResponseEntity getUserList() {
        return ResponseEntity.ok(userService.getUserList());
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
