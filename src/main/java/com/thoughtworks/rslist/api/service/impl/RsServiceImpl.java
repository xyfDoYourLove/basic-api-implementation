package com.thoughtworks.rslist.api.service.impl;

import com.thoughtworks.rslist.api.service.RsService;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.thoughtworks.rslist.Data.Data.rsEvents;
import static com.thoughtworks.rslist.Data.Data.userList;

@Service
public class RsServiceImpl implements RsService {

    @Override
    public List<RsEvent> getRsListBetween(Integer start, Integer end) {
        if (start == null || end == null) {
            return rsEvents;
        }
        if (!isStartAndEndValid(start, end)) {
            throw new RsEventNotValidException("invalid request param");
        }
        return rsEvents.subList(start - 1, end);
    }

    @Override
    public RsEvent getRsListIndex(int index) {
        if (index < 1 || index > rsEvents.size()) {
            throw new RsEventNotValidException("invalid index");
        }
        return rsEvents.get(index - 1);
    }

    @Override
    public void addRsEvent(RsEvent rsEvent) {
        rsEvents.add(rsEvent);
        if (!isAlreadyRegistered(rsEvent.getUser())) {
            userList.add(rsEvent.getUser());
        }
    }

    @Override
    public void updateRsEventIndex(int index, RsEvent rsEvent) {
        RsEvent rsEventToUpdate = rsEvents.get(index - 1);
        if (rsEvent.getEventName() != null && rsEvent.getEventName() != "") {
            rsEventToUpdate.setEventName(rsEvent.getEventName());
        }
        if (rsEvent.getKeyWord() != null && rsEvent.getKeyWord() != "") {
            rsEventToUpdate.setKeyWord(rsEvent.getKeyWord());
        }
        rsEvents.set(index - 1, rsEventToUpdate);
        if (!isAlreadyRegistered(rsEvent.getUser())) {
            userList.add(rsEvent.getUser());
        }
    }

    @Override
    public void deleteRsEventIndex(int index) {
        rsEvents.remove(index - 1);
    }

    public boolean isStartAndEndValid(int start, int end) {
        if (start < 1 || start > rsEvents.size() - 1) {
            return false;
        }
        if (end < 2 || end > rsEvents.size()) {
            return false;
        }
        if (start >= end) {
            return false;
        }
        return true;
    }

    public Boolean isAlreadyRegistered(User user) {

        for (int i = 0; i < userList.size(); i++) {
            if (user.getUserName().equals(userList.get(i).getUserName())) {
                return true;
            }
        }
        return false;
    }
}
