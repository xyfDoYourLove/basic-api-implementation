package com.thoughtworks.rslist.api.repository;

import com.thoughtworks.rslist.dto.VoteDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface VoteRepository extends PagingAndSortingRepository<VoteDto, Integer> {

    @Override
    List<VoteDto> findAll();

    @Query("select v from VoteDto v where v.voteDateTime > :startTime and v.voteDateTime < :endTime ")
    List<VoteDto> findAccordingToVoteDateTime(@Param("startTime") Date startTime, @Param("endTime") Date endTime, Pageable pageable);
}
