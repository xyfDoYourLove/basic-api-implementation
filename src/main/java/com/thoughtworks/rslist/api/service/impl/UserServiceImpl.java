package com.thoughtworks.rslist.api.service.impl;

import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.api.repository.UserRepository;
import com.thoughtworks.rslist.api.service.UserService;
import com.thoughtworks.rslist.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Override
    public Integer registerUser(User user) {
        if (userRepository.findByUserName(user.getUserName()) != null) {
            return null;
        }
        UserDto userDto = user.convent2UserDtoNoId();
        userRepository.save(userDto);
        return userDto.getId();
    }

    @Override
    public List<UserDto> getUserList() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(int id) {
        Optional<UserDto> userOption = userRepository.findById(id);
        if (userOption.isPresent()) {
            UserDto userDto = userOption.get();
            return userDto.convent2User();
        }
        return null;
    }

    @Override
    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }
}
