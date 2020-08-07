package com.thoughtworks.rslist.dto;

import com.thoughtworks.rslist.domain.RsEvent;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "rs_event")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RsEventDto {

    @Id
    @GeneratedValue
    @Setter
    @Getter
    private int id;

    @Setter
    @Getter
    private String eventName;

    @Setter
    @Getter
    private String keyWord;

    @Getter
    @Setter
    private Integer votedNum = 0;

    @ManyToOne
    @Setter
    @Getter
    private UserDto userDto;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rsEventDto")
    private List<VoteDto> voteDto;

    public RsEvent convert2RsEvent() {
        RsEvent rsEvent = new RsEvent();
        rsEvent.setId(getId());
        rsEvent.setEventName(getEventName());
        rsEvent.setKeyWord(getKeyWord());
        rsEvent.setVotedNum(getVotedNum());
        rsEvent.setUser(getUserDto().convent2User());
        return rsEvent;
    }

    @Override
    public String toString() {
        return "RsEventDto{" +
                "id=" + id +
                ", eventName='" + eventName + '\'' +
                ", keyWord='" + keyWord + '\'' +
                ", votedNum=" + votedNum +
                ", userDto=" + userDto +
                '}';
    }
}
