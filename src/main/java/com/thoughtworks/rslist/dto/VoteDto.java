package com.thoughtworks.rslist.dto;

import com.thoughtworks.rslist.domain.Vote;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "vote_record")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoteDto {

    @Id
    @GeneratedValue
    @Setter
    @Getter
    private int id;

    @Setter
    @Getter
    private int voteNum;

    @Setter
    @Getter
    private Date voteDateTime;

    @ManyToOne
    @Setter
    @Getter
    private UserDto userDto;

    @ManyToOne
    @Setter
    @Getter
    private RsEventDto rsEventDto;

    public Vote convert2Vote() {
        Vote vote = new Vote();
        vote.setVoteNum(getVoteNum());
        vote.setVoteDateTime(getVoteDateTime());
        vote.setUser(getUserDto().convent2User());
        vote.setRsEvent(getRsEventDto().convert2RsEvent());
        return vote;
    }
}
