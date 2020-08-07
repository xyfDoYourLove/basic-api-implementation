package com.thoughtworks.rslist.dto;

import com.thoughtworks.rslist.domain.User;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @Id
    @GeneratedValue
    @Setter
    @Getter
    private int id;

    @Column(name = "name")
    @Setter
    @Getter
    private String userName;

    @Setter
    @Getter
    private String gender;

    @Setter
    @Getter
    private int age;

    @Setter
    @Getter
    private String email;

    @Setter
    @Getter
    private String phone;

    @Builder.Default
    @Setter
    @Getter
    private int voteNum = 10;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "userDto")
    private List<RsEventDto> rsEventDto;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userDto")
    private List<VoteDto> voteDto;

    public User convent2User() {
        User user = new User();
        user.setUserName(getUserName());
        user.setGender(getGender());
        user.setAge(getAge());
        user.setEmail(getEmail());
        user.setPhone(getPhone());
        user.setVoteNum(getVoteNum());
        return user;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", voteNum=" + voteNum +
                '}';
    }
}
