package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class RsEvent {

    @NotNull
    private String eventName;

    @NotNull
    private String keyWord;

    @NotNull
    private Integer votedNum = 0;

    @Valid
    @NotNull
    private User user;

    public RsEvent() {
    }

    public RsEvent(@NotNull String eventName, @NotNull String keyWord, @NotNull Integer votedNum, @Valid @NotNull User user) {
        this.eventName = eventName;
        this.keyWord = keyWord;
        this.votedNum = votedNum;
        this.user = user;
    }

    public RsEventDto convert2RsEventNoId(UserDto userDto) {
        RsEventDto rsEventDto = RsEventDto.builder()
                .eventName(getEventName())
                .keyWord(getKeyWord())
                .votedNum(getVotedNum())
                .userDto(userDto)
                .build();
        return rsEventDto;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    @JsonProperty
    public void setUser(User user) {
        this.user = user;
    }
}
