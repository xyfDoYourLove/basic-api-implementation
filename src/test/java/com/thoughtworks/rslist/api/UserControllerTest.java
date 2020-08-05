package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    public void should_init_user_list() {
        UserController.initUserList();
    }

    @Test
    public void should_register_user() throws Exception {
        User user = new User("xyf", "male", 18, "x@y.com", "18888888888", 10);
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
                .andExpect(status().isCreated())
                .andExpect(header().exists("index"))
                .andExpect(header().string("index", "1"));
    }

    @Test
    void should_get_all_user_with_expect_format() throws Exception {
        mockMvc.perform(get("/get/users"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].user_name", is("xiaowang")))
                .andExpect(jsonPath("$[0].user_gender", is("male")))
                .andExpect(jsonPath("$[0].user_age", is(19)))
                .andExpect(jsonPath("$[0].user_email", is("xiao@thought.com")))
                .andExpect(jsonPath("$[0].user_phone", is("19999999999")))
                .andExpect(status().isOk());
    }
}