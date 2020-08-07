package com.thoughtworks.rslist.api.service.impl;

import com.thoughtworks.rslist.api.repository.RsEventRepository;
import com.thoughtworks.rslist.api.repository.UserRepository;
import com.thoughtworks.rslist.api.repository.VoteRepository;
import com.thoughtworks.rslist.api.service.RsService;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.param.RsEventInputParam;
import com.thoughtworks.rslist.param.VoteInputParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.thoughtworks.rslist.common.method.DataInitMethod.rsEvents;
import static com.thoughtworks.rslist.common.method.DataInitMethod.userList;

@Service
public class RsServiceImpl implements RsService {

    @Autowired
    RsEventRepository rsEventRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    VoteRepository voteRepository;

    @Override
    public List<RsEvent> getRsList() {
        List<RsEvent> all = rsEventRepository.findAll().stream().map(
                item -> item.convert2RsEvent()
        ).collect(Collectors.toList());
        return all;
    }

    @Override
    public RsEvent getRsEventById(int id) {
        return rsEventRepository.findById(id).get().convert2RsEvent();
    }

    @Override
    public Integer addRsEvent(RsEvent rsEvent) {
        List<UserDto> allUserBefore = userRepository.findAll();
        RsEventDto rsEventDto;
        if (userRepository.findByUserName(rsEvent.getUser().getUserName()) == null) {
            UserDto userDto = rsEvent.getUser().convent2UserDtoNoId();
            userRepository.save(userDto);
            rsEventDto = rsEvent.convert2RsEventNoId(userDto);
            rsEventRepository.save(rsEventDto);
        }else {
            rsEventDto = rsEvent.convert2RsEventNoId(userRepository.findByUserName(rsEvent.getUser().getUserName()));
            rsEventRepository.save(rsEventDto);
        }
        List<UserDto> allUserAfter = userRepository.findAll();

        if (allUserAfter.size() - allUserBefore.size() > 0) {
            return rsEventDto.getId();
        }
        return null;
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
    public void deleteRsEventIndex(int id) {
        rsEventRepository.deleteById(id);
    }

    @Override
    public ResponseEntity updateRsEventWhenUserMatch(int rsEventId, RsEventInputParam rsEventInputParam) {
        RsEventDto rsEventDto = rsEventRepository.findById(rsEventId).get();
        if (rsEventDto.getUserDto().getId() == Integer.valueOf(rsEventInputParam.getUserId())) {
            if (rsEventInputParam.getEventName() != null && rsEventInputParam.getEventName() != "") {
                rsEventDto.setEventName(rsEventInputParam.getEventName());
            }
            if (rsEventInputParam.getKeyWord() != null && rsEventInputParam.getKeyWord() != "") {
                rsEventDto.setKeyWord(rsEventInputParam.getKeyWord());
            }
            rsEventRepository.save(rsEventDto);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @Override
    public ResponseEntity voteToRsEvent(int rsEventId, VoteInputParam voteInputParam) {
        UserDto userDto = userRepository.findById(voteInputParam.getUserId()).get();
        RsEventDto rsEventDto = rsEventRepository.findById(rsEventId).get();

        if (userDto.getVoteNum() < voteInputParam.getVoteNum()) {
            return ResponseEntity.badRequest().build();
        }

        userDto.setVoteNum(userDto.getVoteNum() - voteInputParam.getVoteNum());
        userRepository.save(userDto);
        rsEventDto.setVotedNum(rsEventDto.getVotedNum() + voteInputParam.getVoteNum());
        rsEventRepository.save(rsEventDto);

        VoteDto build = VoteDto.builder()
                .voteNum(voteInputParam.getVoteNum())
                .voteDateTime(voteInputParam.getVoteTime())
                .userDto(userDto)
                .rsEventDto(rsEventDto)
                .build();
        voteRepository.save(build);
        return ResponseEntity.created(null).build();
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
