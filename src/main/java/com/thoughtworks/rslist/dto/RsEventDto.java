package com.thoughtworks.rslist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "rs_event")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RsEventDto {

    @Id
    @GeneratedValue
    private int id;

    private String eventName;

    private String keyWord;

    @ManyToOne
    private UserDto userDto;
}
