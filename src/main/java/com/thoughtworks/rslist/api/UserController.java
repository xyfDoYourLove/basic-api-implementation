package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
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

    public static List<User> userList = new ArrayList<>();

    public static void initUserList() {
        userList = new ArrayList<>();
        User user = new User("xiaowang", "male", 19, "xiao@thought.com", "19999999999", 10);
        userList.add(user);
    }

    @PostMapping("/user/register")
    public ResponseEntity registerUser(@RequestBody @Valid User user) {
        userList.add(user);
        return ResponseEntity.created(null).header("index", String.valueOf(userList.size() - 1)).build();
    }

    @GetMapping("/user/list")
    public ResponseEntity getUserList() {
        return ResponseEntity.ok(userList);
    }

}
