package com.thoughtworks.rslist.api.service.impl;

import com.thoughtworks.rslist.Data.Data;
import com.thoughtworks.rslist.api.service.UserService;
import com.thoughtworks.rslist.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    @Override
    public void registerUser(User user) {
        Data.userList.add(user);
    }

    @Override
    public List<User> getUserList() {
        return Data.userList;
    }
}
