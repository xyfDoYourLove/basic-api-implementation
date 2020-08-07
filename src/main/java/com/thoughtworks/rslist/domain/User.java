package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thoughtworks.rslist.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @NotNull
    @Size(max = 8)
    @JsonProperty("user_name")
    @JsonAlias("userName")
    private String userName;

    @NotNull
    @JsonProperty("user_gender")
    @JsonAlias("gender")
    private String gender;

    @NotNull
    @Min(18)
    @Max(100)
    @JsonProperty("user_age")
    @JsonAlias("age")
    private Integer age;

    @Email
    @JsonProperty("user_email")
    @JsonAlias("email")
    private String email;

    @NotNull
    @Pattern(regexp = "^1\\d{10}$")
    @JsonProperty("user_phone")
    @JsonAlias("phone")
    private String phone;

    @JsonProperty("user_voteNum")
    @JsonAlias("voteNum")
    @Builder.Default
    private int voteNum = 10;

    public UserDto convent2UserDtoNoId() {
        UserDto userDto = UserDto.builder()
                .userName(getUserName())
                .gender(getGender())
                .age(getAge())
                .email(getEmail())
                .phone(getPhone())
                .voteNum(getVoteNum())
                .build();
        return userDto;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(int voteNum) {
        this.voteNum = voteNum;
    }
}
