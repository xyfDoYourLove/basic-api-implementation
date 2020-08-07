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
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.param.RsEventInputParam;
import com.thoughtworks.rslist.param.VoteInputParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public ResponseEntity addRsEvent(RsEvent rsEvent) {

        UserDto userDto = UserDto.builder()
                .userName(rsEvent.getUser().getUserName())
                .gender(rsEvent.getUser().getGender())
                .age(rsEvent.getUser().getAge())
                .email(rsEvent.getUser().getEmail())
                .phone(rsEvent.getUser().getPhone())
                .voteNum(rsEvent.getUser().getVoteNum())
                .build();

        List<RsEventDto> all = rsEventRepository.findAll();

        if (userRepository.findByUserName(rsEvent.getUser().getUserName()) == null) {
            userRepository.save(userDto);
            RsEventDto rsEventDto = RsEventDto.builder()
                    .eventName(rsEvent.getEventName())
                    .keyWord(rsEvent.getKeyWord())
                    .userDto(userDto)
                    .build();
            rsEventRepository.save(rsEventDto);

            return ResponseEntity.badRequest().header("index", String.valueOf(all.size() - 1)).build();
        }
        RsEventDto rsEventDto = RsEventDto.builder()
                .eventName(rsEvent.getEventName())
                .keyWord(rsEvent.getKeyWord())
                .userDto(userRepository.findByUserName(rsEvent.getUser().getUserName()))
                .build();
        rsEventRepository.save(rsEventDto);

        return ResponseEntity.created(null).header("index", String.valueOf(all.size() - 1)).build();
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
