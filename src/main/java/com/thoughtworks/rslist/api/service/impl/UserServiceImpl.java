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
    public ResponseEntity registerUser(User user) {
        UserDto userDto = UserDto.builder()
                .userName(user.getUserName())
                .gender(user.getGender())
                .age(user.getAge())
                .email(user.getEmail())
                .phone(user.getPhone())
                .voteNum(user.getVoteNum())
                .build();
        userRepository.save(userDto);

        List<UserDto> all = userRepository.findAll();

        return ResponseEntity.created(null).header("index", String.valueOf(all.size() - 1)).build();
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
            return User.builder()
                    .userName(userDto.getUserName())
                    .gender(userDto.getGender())
                    .age(userDto.getAge())
                    .email(userDto.getEmail())
                    .phone(userDto.getPhone())
                    .voteNum(userDto.getVoteNum())
                    .build();
        }
        return null;
    }

    @Override
    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }
}
