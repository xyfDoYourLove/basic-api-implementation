package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.api.repository.RsEventRepository;
import com.thoughtworks.rslist.api.repository.UserRepository;
import com.thoughtworks.rslist.common.method.DataInitMethod;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    RsEventDto rsEvent;

    UserDto user;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RsEventRepository rsEventRepository;

    @BeforeEach
    public void should_init_user_list() {

        DataInitMethod.initUserTable();

        userRepository.deleteAll();
        user = UserDto.builder()
                .userName("xyf")
                .gender("male")
                .age(19)
                .email("xiao@thought.com")
                .phone("19999999999")
                .voteNum(10)
                .build();
        userRepository.save(user);

        rsEventRepository.deleteAll();
        rsEvent = RsEventDto.builder()
                .eventName("第一条事件")
                .keyWord("无标签")
                .userDto(user)
                .build();
        rsEventRepository.save(rsEvent);
    }

    @Test
    public void should_register_user() throws Exception {
        User user = new User("xyf1", "male", 18, "x@y.com", "18888888888", 10);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user/register").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void user_name_should_less_then_8() throws Exception {
        User user = new User("xyf12122121212", "male", 18, "x@y.com", "18888888888", 10);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user/register").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void age_should_between_18_and_100() throws Exception {
        User user = new User("xyf", "male", 17, "x@y.com", "18888888888", 10);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user/register").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void email_should_suit_format() throws Exception {
        User user = new User("xyf", "male", 19, "xy.com", "18888888888", 10);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user/register").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void phone_should_suit_format() throws Exception {
        User user = new User("xyf", "male", 19, "x@y.com", "188888888881", 10);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user/register").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_return_with_index_in_header_post() throws Exception {
        User user = new User("xyf", "male", 18, "x@y.com", "18888888888", 10);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user/register").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(header().exists("id"))
                .andExpect(header().string("id", "-1"));
    }

    @Test
    void should_get_all_user_with_expect_format() throws Exception {
        mockMvc.perform(get("/get/users"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userName", is("xyf")))
                .andExpect(jsonPath("$[0].gender", is("male")))
                .andExpect(jsonPath("$[0].age", is(19)))
                .andExpect(jsonPath("$[0].email", is("xiao@thought.com")))
                .andExpect(jsonPath("$[0].phone", is("19999999999")))
                .andExpect(status().isOk());
    }

    @Test
    void should_register_user_to_database() throws Exception {
        User user = new User("xyf1", "male", 18, "x@y.com", "18888888888", 10);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user/register").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<UserDto> all = userRepository.findAll();
        assertEquals(2, all.size());
    }

    @Test
    void should_get_user_by_id() throws Exception {
        UserDto userDto = userRepository.findAll().get(0);
        mockMvc.perform(get("/get/user").param("id", String.valueOf(userDto.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_name", is("xyf")));
    }

    @Test
    void should_delete_user_by_id() throws Exception {
        UserDto userDto = userRepository.findAll().get(0);
        mockMvc.perform(delete("/delete/" + userDto.getId()))
                .andExpect(status().isOk());

        List<UserDto> all = userRepository.findAll();
        assertEquals(0, all.size());
    }

    @Test
    void should_delete_rs_event_when_delete_user() throws Exception {
        mockMvc.perform(delete("/delete/" + user.getId()))
                .andExpect(status().isOk());

        List<RsEventDto> all = rsEventRepository.findAll();
        assertEquals(0, all.size());
    }
}