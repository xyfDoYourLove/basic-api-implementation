package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.api.repository.RsEventRepository;
import com.thoughtworks.rslist.api.repository.UserRepository;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.param.RsEventInputParam;
import com.thoughtworks.rslist.param.VoteInputParam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;

import static com.thoughtworks.rslist.common.method.DataInitMethod.initRsEvents;
import static com.thoughtworks.rslist.common.method.DataInitMethod.initUserTable;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RsListApplicationTests {

    public ObjectMapper objectMapper;

    RsEventDto rsEvent;

    UserDto user;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    RsEventRepository rsEventRepository;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void should_init_RsEvents() {
        initRsEvents();
        initUserTable();
        objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);

        userRepository.deleteAll();
        user = UserDto.builder()
                .userName("xyf").gender("male")
                .age(19).email("xiao@thought.com")
                .phone("19999999999").voteNum(10)
                .build();
        userRepository.save(user);

        rsEventRepository.deleteAll();
        rsEvent = RsEventDto.builder()
                .eventName("第一条事件").keyWord("无标签")
                .votedNum(0).userDto(user)
                .build();
        rsEventRepository.save(rsEvent);
    }

    @Test
    void should_get_rs_list_all() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无标签")))
                .andExpect(status().isOk());
    }

    @Test
    void should_get_re_event_index() throws Exception {
        List<RsEventDto> all = rsEventRepository.findAll();
        mockMvc.perform(get("/rs/" + all.get(0).getId()))
                .andExpect(jsonPath("$.eventName", is("第一条事件")))
                .andExpect(jsonPath("$.keyWord", is("无标签")))
                .andExpect(status().isOk());
    }


    @Test
    void should_add_rs_event() throws Exception {
        User user = new User("xyf1", "male", 19, "xiao@thoughtworks.com", "18888888888", 10);
        RsEvent rsEvent = new RsEvent("石油降价了", "经济", user);
        String event = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("石油降价了")))
                .andExpect(jsonPath("$[1].keyWord", is("经济")))
                .andExpect(status().isOk());
    }

    @Test
    void should_update_rs_event_by_id() throws Exception {

        RsEventDto rsEventDto = rsEventRepository.findAll().get(0);
        RsEventInputParam rsEventInputParam = new RsEventInputParam();
        rsEventInputParam.setEventName("修改第三条事件");
        rsEventInputParam.setKeyWord("娱乐");
        rsEventInputParam.setUserId(String.valueOf(rsEventDto.getUserDto().getId()));
        String event = objectMapper.writeValueAsString(rsEventInputParam);

        mockMvc.perform(patch("/rs/" + rsEventDto.getId()).content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].eventName", is("修改第三条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("娱乐")))
                .andExpect(status().isOk());
    }

    @Test
    void should_delete_rs_event_index() throws Exception {
        RsEventDto rsEventDto = rsEventRepository.findAll().get(0);
        mockMvc.perform(delete("/rs/" + rsEventDto.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(0)))
                .andExpect(status().isOk());
    }

    @Test
    void should_register_user_when_add_rs_event_if_user_not_exist() throws Exception {
        User user = new User("xyf1", "male", 19, "xiao@thoughtworks.com", "18888888888", 10);
        RsEvent rsEvent = new RsEvent("添加一条热搜", "娱乐", user);
        String event = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/get/users"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].userName", is("xyf1")))
                .andExpect(jsonPath("$[1].age", is(19)))
                .andExpect(status().isOk());
    }

    @Test
    void should_not_register_user_when_add_rs_event_if_user_exist() throws Exception {
        User user = new User("xyf1", "male", 19, "xiao@thoughtworks.com", "18888888888", 10);
        RsEvent rsEvent = new RsEvent("添加一条热搜", "娱乐", user);
        String event = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/get/users"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(status().isOk());
    }

    @Test
    void should_register_user_when_update_rs_event_if_user_not_exist() throws Exception {

        RsEventDto rsEventDto = rsEventRepository.findAll().get(0);
        RsEventInputParam rsEventInputParam = new RsEventInputParam();
        rsEventInputParam.setEventName("修改第三条事件");
        rsEventInputParam.setKeyWord("娱乐");
        rsEventInputParam.setUserId("1111");
        String event = objectMapper.writeValueAsString(rsEventInputParam);

        mockMvc.perform(patch("/rs/" + rsEventDto.getId()).content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/get/users"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());
    }

    @Test
    void userName_should_not_null() throws Exception {
        User user = new User("xyf", "male", 2, "xiao@thoughtworks.com", "18888888888", 10);
        RsEvent rsEvent = new RsEvent(null, "娱乐", user);
        String event = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/get/users"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());
    }

    @Test
    void keyWord_should_not_null() throws Exception {
        User user = new User("xyf", "male", 2, "xiao@thoughtworks.com", "18888888888", 10);
        RsEvent rsEvent = new RsEvent("添加一条事件", null, user);
        String event = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/get/users"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());
    }

    @Test
    void user_should_not_null() throws Exception {

        RsEvent rsEvent = new RsEvent("添加一条事件", "娱乐", null);
        String event = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/get/users"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());
    }

    @Test
    void user_userName_should_less_then_8() throws Exception {
        User user = new User("xyf4564654564", "male", 19, "xiao@thoughtworks.com", "18888888888", 10);
        RsEvent rsEvent = new RsEvent("添加一条事件", "娱乐", user);
        String event = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/get/users"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());
    }

    @Test
    void user_userName_should_not_null() throws Exception {
        User user = new User(null, "male", 19, "xiao@thoughtworks.com", "18888888888", 10);
        RsEvent rsEvent = new RsEvent("添加一条事件", "娱乐", user);
        String event = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/get/users"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());
    }

    @Test
    void user_age_should_between_18_and_100() throws Exception {
        User user = new User("xyf", "male", 2, "xiao@thoughtworks.com", "18888888888", 10);
        RsEvent rsEvent = new RsEvent("添加一条事件", "娱乐", user);
        String event = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/get/users"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());
    }

    @Test
    void user_age_should_not_null() throws Exception {
        User user = new User("xyf", "male", null, "xiao@thoughtworks.com", "18888888888", 10);
        RsEvent rsEvent = new RsEvent("添加一条事件", "娱乐", user);
        String event = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/get/users"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());
    }

    @Test
    void user_email_should_suit_format() throws Exception {
        User user = new User("xyf", "male", 19, "xiaothoughtworks.com", "18888888888", 10);
        RsEvent rsEvent = new RsEvent("添加一条事件", "娱乐", user);
        String event = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/get/users"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());
    }

    @Test
    void user_phone_should_suit_format() throws Exception {

        User user = new User("xyf", "male", 19, "xiao@thoughtworks.com", "1888888888811", 10);
        RsEvent rsEvent = new RsEvent("添加一条事件", "娱乐", user);
        String event = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/get/users"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());
    }

    @Test
    void user_phone_should_not_null() throws Exception {

        User user = new User("xyf", "male", 19, "xiao@thoughtworks.com", null, 10);
        RsEvent rsEvent = new RsEvent("添加一条事件", "娱乐", user);
        String event = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/get/users"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());
    }

    @Test
    void user_gender_should_suit_format() throws Exception {

        User user = new User("xyf", null, 19, "xiao@thoughtworks.com", "1888888888811", 10);
        RsEvent rsEvent = new RsEvent("添加一条事件", "娱乐", user);
        String event = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/get/users"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());
    }

    @Test
    void should_return_with_index_in_header_post() throws Exception {
        User user = new User("xyf", "male", 19, "xiao@thoughtworks.com", "18888888888", 10);
        RsEvent rsEvent = new RsEvent("石油降价了", "经济", user);
        String event = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(header().exists("id"))
                .andExpect(header().stringValues("id", "-1"));
    }

    @Test
    void should_get_rs_list_all_with_no_user() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无标签")))
                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
                .andExpect(status().isOk());
    }

    @Test
    void should_get_re_event_with_no_user() throws Exception {
        List<RsEventDto> all = rsEventRepository.findAll();
        mockMvc.perform(get("/rs/" + all.get(0).getId()))
                .andExpect(jsonPath("$.eventName", is("第一条事件")))
                .andExpect(jsonPath("$.keyWord", is("无标签")))
                .andExpect(jsonPath("$", not(hasKey("user"))))
                .andExpect(status().isOk());
    }

    @Test
    void should_throw_method_argument_not_valid_exception() throws Exception {
        User user = new User("xyf", "male", 19, "xiao@thoughtworks.com", "18888888888", 10);
        RsEvent rsEvent = new RsEvent("添加一条事件", null, user);
        String event = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));
    }

    @Test
    void should_throw_invalid_user() throws Exception {
        User user = new User("xyf21313123", "male", 19, "xiao@thoughtworks.com", "18888888888", 10);
        objectMapper = new ObjectMapper();
        String event = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user/register").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    void should_add_rs_event_to_database_if_user_exist() throws Exception {
        User user = new User("xyf1", "male", 19, "xiao@thoughtworks.com", "18888888888", 10);
        RsEvent rsEvent = new RsEvent("添加一条事件", "娱乐", user);
        String event = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<UserDto> all = userRepository.findAll();
        assertEquals(2, all.size());
    }

    @Test
    void should_add_rs_event_to_database_if_user_not_exist() throws Exception {
        User user = new User("xyf2", "male", 19, "xiao@thoughtworks.com", "18888888888", 10);
        RsEvent rsEvent = new RsEvent("添加一条事件", "娱乐", user);
        String event = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<UserDto> all = userRepository.findAll();
        assertEquals(2, all.size());
    }

    @Test
    void should_update_event_when_user_match() throws Exception {
        RsEventDto rsEventDto = rsEventRepository.findAll().get(0);

        RsEventInputParam rsEventInputParam = new RsEventInputParam();
        rsEventInputParam.setEventName("新的热搜事件");
        rsEventInputParam.setKeyWord("新的关键字");
        rsEventInputParam.setUserId(String.valueOf(rsEventDto.getUserDto().getId()));

        String jsonString = objectMapper.writeValueAsString(rsEventInputParam);

        mockMvc.perform(patch("/rs/" + rsEventDto.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        RsEventDto rsEventDtoUpdated = rsEventRepository.findById(rsEventDto.getId()).get();
        assertEquals("新的热搜事件", rsEventDtoUpdated.getEventName());
        assertEquals("新的关键字", rsEventDtoUpdated.getKeyWord());
    }

    @Test
    void should_update_event_when_user_not_match() throws Exception {
        RsEventDto rsEventDto = rsEventRepository.findAll().get(0);

        RsEventInputParam rsEventInputParam = new RsEventInputParam();
        rsEventInputParam.setEventName("新的热搜事件");
        rsEventInputParam.setKeyWord("新的关键字");
        rsEventInputParam.setUserId(String.valueOf(1111));

        String jsonString = objectMapper.writeValueAsString(rsEventInputParam);

        mockMvc.perform(patch("/rs/" + rsEventDto.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        RsEventDto rsEventDtoUpdated = rsEventRepository.findById(rsEventDto.getId()).get();
        assertEquals("第一条事件", rsEventDtoUpdated.getEventName());
        assertEquals("无标签", rsEventDtoUpdated.getKeyWord());
    }

    @Test
    void should_vote_when_voteNum_enough() throws Exception {
        RsEventDto rsEventDto = rsEventRepository.findAll().get(0);

        VoteInputParam voteInputParam = new VoteInputParam();
        voteInputParam.setUserId(rsEventDto.getUserDto().getId());
        voteInputParam.setVoteNum(5);
        voteInputParam.setVoteTime(new Date());
        String jsonString = objectMapper.writeValueAsString(voteInputParam);

        mockMvc.perform(post("/rs/vote/" + rsEventDto.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }

    @Test
    void should_vote_when_voteNum_not_enough() throws Exception {
        RsEventDto rsEventDto = rsEventRepository.findAll().get(0);

        VoteInputParam voteInputParam = new VoteInputParam();
        voteInputParam.setUserId(rsEventDto.getUserDto().getId());
        voteInputParam.setVoteNum(11);
        voteInputParam.setVoteTime(new Date());
        String jsonString = objectMapper.writeValueAsString(voteInputParam);

        mockMvc.perform(post("/rs/vote/" + rsEventDto.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }
}
