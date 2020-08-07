package com.thoughtworks.rslist.api.repository;

import com.thoughtworks.rslist.dto.VoteDto;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VoteRepository extends CrudRepository<VoteDto, Integer> {

    @Override
    List<VoteDto> findAll();
}
