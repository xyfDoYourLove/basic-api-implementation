package com.thoughtworks.rslist.dto;

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
    private UserDto userDto;

    @ManyToOne
    private RsEventDto rsEventDto;
}
