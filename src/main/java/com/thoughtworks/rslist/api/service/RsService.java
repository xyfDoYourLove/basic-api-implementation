package com.thoughtworks.rslist.api.service;

import com.thoughtworks.rslist.domain.RsEvent;

import java.util.List;

public interface RsService {

    List<RsEvent> getRsListBetween(Integer start, Integer end);

    RsEvent getRsListIndex(int index);

    void addRsEvent(RsEvent rsEvent);

    void updateRsEventIndex(int index, RsEvent rsEvent);

    void deleteRsEventIndex(int index);

}
