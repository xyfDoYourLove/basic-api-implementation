package com.thoughtworks.rslist.api.service;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.param.RsEventInputParam;
import com.thoughtworks.rslist.param.VoteInputParam;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RsService {

    List<RsEvent> getRsList();

    RsEvent getRsEventById(int index);

    Integer addRsEvent(RsEvent rsEvent);

    void updateRsEventIndex(int index, RsEvent rsEvent);

    void deleteRsEventIndex(int id);

    ResponseEntity updateRsEventWhenUserMatch(int rsEventId, RsEventInputParam rsEventInputParam);

    ResponseEntity voteToRsEvent(int rsEventId, VoteInputParam voteInputParam);
}
