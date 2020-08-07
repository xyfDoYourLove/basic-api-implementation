package com.thoughtworks.rslist.param;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class VoteInputParam {

    @NotNull
    private int voteNum;

    @NotNull
    private int userId;

    @NotNull
    private Date voteTime;

}
