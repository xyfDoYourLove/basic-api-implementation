package com.thoughtworks.rslist.api.service;

import com.thoughtworks.rslist.domain.User;

import java.util.List;

public interface UserService {

    void registerUser(User user);

    List<User> getUserList();

}
