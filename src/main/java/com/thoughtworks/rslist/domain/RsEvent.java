package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thoughtworks.rslist.dto.RsEventDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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

    public RsEventDto convert2RsEventNoId() {
        RsEventDto rsEventDto = RsEventDto.builder()
                .eventName(getEventName())
                .keyWord(getKeyWord())
                .votedNum(getVotedNum())
                .userDto(getUser().convent2UserDtoNoId())
                .build();
        return rsEventDto;
    }

    public RsEvent() {
    }

    public RsEvent(String eventName, String keyWord, User user) {
        this.eventName = eventName;
        this.keyWord = keyWord;
        this.user = user;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    @JsonProperty
    public void setUser(User user) {
        this.user = user;
    }

    public Integer getVotedNum() {
        return votedNum;
    }

    public void setVotedNum(Integer votedNum) {
        this.votedNum = votedNum;
    }
}
