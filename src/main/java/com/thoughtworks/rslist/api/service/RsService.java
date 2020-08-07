package com.thoughtworks.rslist.api.service;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.param.RsEventInputParam;
import com.thoughtworks.rslist.param.VoteInputParam;

import java.util.List;

public interface RsService {

    List<RsEvent> getRsList();

    RsEvent getRsEventById(int index);

    Integer addRsEvent(RsEvent rsEvent);

    void deleteRsEventById(int id);

    Boolean updateRsEventWhenUserMatch(int rsEventId, RsEventInputParam rsEventInputParam);

    Boolean voteToRsEvent(int rsEventId, VoteInputParam voteInputParam);
}
