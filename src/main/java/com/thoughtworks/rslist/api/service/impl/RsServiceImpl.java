package com.thoughtworks.rslist.api.service.impl;

import com.thoughtworks.rslist.api.repository.RsEventRepository;
import com.thoughtworks.rslist.api.repository.UserRepository;
import com.thoughtworks.rslist.api.repository.VoteRepository;
import com.thoughtworks.rslist.api.service.RsService;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.param.RsEventInputParam;
import com.thoughtworks.rslist.param.VoteInputParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    public void deleteRsEventById(int id) {
        rsEventRepository.deleteById(id);
    }

    @Override
    public Boolean updateRsEventWhenUserMatch(int rsEventId, RsEventInputParam rsEventInputParam) {
        RsEventDto rsEventDto = rsEventRepository.findById(rsEventId).get();

        if (Integer.valueOf(rsEventInputParam.getUserId()).compareTo(rsEventDto.getUserDto().getId()) == 0) {
            if (rsEventInputParam.getEventName() != null && rsEventInputParam.getEventName() != "") {
                rsEventDto.setEventName(rsEventInputParam.getEventName());
            }
            if (rsEventInputParam.getKeyWord() != null && rsEventInputParam.getKeyWord() != "") {
                rsEventDto.setKeyWord(rsEventInputParam.getKeyWord());
            }
            rsEventRepository.save(rsEventDto);
            return true;
        }
        return false;
    }

    @Override
    public Boolean voteToRsEvent(int rsEventId, VoteInputParam voteInputParam) {
        UserDto userDto = userRepository.findById(voteInputParam.getUserId()).get();
        RsEventDto rsEventDto = rsEventRepository.findById(rsEventId).get();

        if (userDto.getVoteNum() < voteInputParam.getVoteNum()) {
            return false;
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

        return true;
    }

    @Override
    public List<Vote> getVoteListBetween(Date startTime, Date endTime) {
        List<VoteDto> allByVoteDateTimeBetween = voteRepository.findAllByVoteDateTimeBetween(startTime, endTime);
        List<Vote> voteList = allByVoteDateTimeBetween.stream().map(
                item -> item.convert2Vote()
        ).collect(Collectors.toList());
        return voteList;
    }

}
