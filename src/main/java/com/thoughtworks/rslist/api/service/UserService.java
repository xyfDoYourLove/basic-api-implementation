package com.thoughtworks.rslist.api.service;

import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.domain.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    ResponseEntity registerUser(User user);

    List<UserDto> getUserList();

    User getUserById(int id);

    void deleteUserById(int id);
}
