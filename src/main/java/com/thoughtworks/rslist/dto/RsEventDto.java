package com.thoughtworks.rslist.dto;

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
