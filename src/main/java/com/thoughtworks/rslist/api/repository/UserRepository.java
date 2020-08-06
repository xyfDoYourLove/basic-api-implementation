package com.thoughtworks.rslist.api.repository;

import com.thoughtworks.rslist.dto.UserDto;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<UserDto, Integer>{
    @Override
    List<UserDto> findAll();

    UserDto findByUserName(String userName);
}
