package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    private static List<User> userList = new ArrayList<>();

    public static void initUserList() {
        userList.clear();
    }

    @PostMapping("/user/register")
    public void registerUser(@RequestBody @Valid User user) {
        userList.add(user);
    }

}
