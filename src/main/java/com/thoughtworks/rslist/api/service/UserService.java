package com.thoughtworks.rslist.api.service;

import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.domain.User;

import java.util.List;

public interface UserService {

    Integer registerUser(User user);

    List<UserDto> getUserList();

    User getUserById(int id);

    void deleteUserById(int id);
}
