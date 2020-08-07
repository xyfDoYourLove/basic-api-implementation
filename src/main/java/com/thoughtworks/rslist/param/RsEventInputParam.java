package com.thoughtworks.rslist.param;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RsEventInputParam {

    private String eventName;

    private String keyWord;

    @NotNull
    private String userId;
}
