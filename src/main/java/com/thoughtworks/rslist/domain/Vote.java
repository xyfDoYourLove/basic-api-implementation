package com.thoughtworks.rslist.domain;

import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.VoteDto;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class Vote {
    @NotNull
    private int voteNum;

    @NotNull
    private Date voteDateTime;

    @NotNull
    @Valid
    private User user;

    @NotNull
    @Valid
    private RsEvent rsEvent;

    public VoteDto convert2VoteDtoNoId(UserDto userDto, RsEventDto rsEventDto) {
        return VoteDto.builder()
                .voteNum(getVoteNum())
                .voteDateTime(getVoteDateTime())
                .userDto(userDto)
                .rsEventDto(rsEventDto)
                .build();
    }
}
