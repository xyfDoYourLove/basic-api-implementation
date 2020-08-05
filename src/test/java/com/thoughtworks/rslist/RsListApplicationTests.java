package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.api.RsController;
import com.thoughtworks.rslist.api.UserController;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RsListApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void should_init_RsEvents() {
        RsController.initRsEvents();
        UserController.initUserList();
    }

    @Test
    void should_get_rs_list_all() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无标签")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无标签")))
                .andExpect(status().isOk());
    }

    @Test
    void should_get_re_event_index() throws Exception {
        mockMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.eventName", is("第一条事件")))
                .andExpect(jsonPath("$.keyWord", is("无标签")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.eventName", is("第二条事件")))
                .andExpect(jsonPath("$.keyWord", is("无标签")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/3"))
                .andExpect(jsonPath("$.eventName", is("第三条事件")))
                .andExpect(jsonPath("$.keyWord", is("无标签")))
                .andExpect(status().isOk());
    }

    @Test
    void should_get_re_event_between() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无标签")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=2&end=3"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无标签")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=1&end=3"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无标签")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无标签")))
                .andExpect(status().isOk());
    }

    @Test
    void should_add_rs_event() throws Exception {
        User user = new User("xyf", "male", 19, "xiao@thoughtworks.com", "18888888888", 10);
        RsEvent rsEvent = new RsEvent("石油降价了", "经济", user);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        String event = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无标签")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无标签")))
                .andExpect(jsonPath("$[3].eventName", is("石油降价了")))
                .andExpect(jsonPath("$[3].keyWord", is("经济")))
                .andExpect(status().isOk());
    }

    @Test
    void should_update_rs_event_index() throws Exception {
        User user = new User("xyf", "male", 2, "xiao@thoughtworks.com", "18888888888", 10);
        RsEvent rsEvent = new RsEvent("修改第三条事件", "", user);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        String event = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(patch("/rs/3").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无标签")))
                .andExpect(jsonPath("$[2].eventName", is("修改第三条事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无标签")))
                .andExpect(status().isOk());
    }

    @Test
    void should_delete_rs_event_index() throws Exception {
        mockMvc.perform(delete("/rs/3"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无标签")))
                .andExpect(status().isOk());
    }

    @Test
    void should_register_user_when_add_rs_event_if_user_not_exist() throws Exception {
        User user = new User("xyf", "male", 19, "xiao@thoughtworks.com", "18888888888", 10);
        RsEvent rsEvent = new RsEvent("添加一条热搜", "娱乐", user);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        String event = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/user/list"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].userName", is("xyf")))
                .andExpect(jsonPath("$[1].age", is(19)))
                .andExpect(status().isOk());
    }

    @Test
    void should_not_register_user_when_add_rs_event_if_user_exist() throws Exception {
        User user = new User("xiaowang", "male", 19, "xiao@thoughtworks.com", "18888888888", 10);
        RsEvent rsEvent = new RsEvent("添加一条热搜", "娱乐", user);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        String event = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/user/list"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());
    }

    @Test
    void should_register_user_when_update_rs_event_if_user_not_exist() throws Exception {
        User user = new User("xyf", "male", 2, "xiao@thoughtworks.com", "18888888888", 10);
        RsEvent rsEvent = new RsEvent("修改第三条事件", "娱乐", user);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        String event = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(patch("/rs/3").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/user/list"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(status().isOk());
    }

    @Test
    void userName_should_not_null() throws Exception {
        User user = new User("xyf", "male", 2, "xiao@thoughtworks.com", "18888888888", 10);
        RsEvent rsEvent = new RsEvent(null, "娱乐", user);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        String event = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/user/list"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());
    }

    @Test
    void keyWord_should_not_null() throws Exception {
        User user = new User("xyf", "male", 2, "xiao@thoughtworks.com", "18888888888", 10);
        RsEvent rsEvent = new RsEvent("添加一条事件", null, user);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        String event = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/user/list"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());
    }

    @Test
    void user_should_not_null() throws Exception {

        RsEvent rsEvent = new RsEvent("添加一条事件", "娱乐", null);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        String event = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/user/list"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());
    }

    @Test
    void user_userName_should_less_then_8() throws Exception {
        User user = new User("xyf4564654564", "male", 19, "xiao@thoughtworks.com", "18888888888", 10);
        RsEvent rsEvent = new RsEvent("添加一条事件", "娱乐", user);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        String event = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/user/list"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());
    }

    @Test
    void user_userName_should_not_null() throws Exception {
        User user = new User(null, "male", 19, "xiao@thoughtworks.com", "18888888888", 10);
        RsEvent rsEvent = new RsEvent("添加一条事件", "娱乐", user);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        String event = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/user/list"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());
    }

    @Test
    void user_age_should_between_18_and_100() throws Exception {
        User user = new User("xyf", "male", 2, "xiao@thoughtworks.com", "18888888888", 10);
        RsEvent rsEvent = new RsEvent("添加一条事件", "娱乐", user);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        String event = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/user/list"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());
    }

    @Test
    void user_age_should_not_null() throws Exception {
        User user = new User("xyf", "male", null, "xiao@thoughtworks.com", "18888888888", 10);
        RsEvent rsEvent = new RsEvent("添加一条事件", "娱乐", user);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        String event = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/user/list"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());
    }

    @Test
    void user_email_should_suit_format() throws Exception {
        User user = new User("xyf", "male", 19, "xiaothoughtworks.com", "18888888888", 10);
        RsEvent rsEvent = new RsEvent("添加一条事件", "娱乐", user);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        String event = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/user/list"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());
    }

    @Test
    void user_phone_should_suit_format() throws Exception {

        User user = new User("xyf", "male", 19, "xiao@thoughtworks.com", "1888888888811", 10);
        RsEvent rsEvent = new RsEvent("添加一条事件", "娱乐", user);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        String event = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/user/list"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());
    }

    @Test
    void user_phone_should_not_null() throws Exception {

        User user = new User("xyf", "male", 19, "xiao@thoughtworks.com", null, 10);
        RsEvent rsEvent = new RsEvent("添加一条事件", "娱乐", user);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        String event = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/user/list"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());
    }

    @Test
    void user_gender_should_suit_format() throws Exception {

        User user = new User("xyf", null, 19, "xiao@thoughtworks.com", "1888888888811", 10);
        RsEvent rsEvent = new RsEvent("添加一条事件", "娱乐", user);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        String event = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/user/list"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());
    }

    @Test
    void should_return_with_index_in_header_post() throws Exception {
        User user = new User("xyf", "male", 19, "xiao@thoughtworks.com", "18888888888", 10);
        RsEvent rsEvent = new RsEvent("石油降价了", "经济", user);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        String event = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().exists("index"))
                .andExpect(header().string("index", "3"));
    }

    @Test
    void should_get_rs_list_all_with_no_user() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无标签")))
                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无标签")))
                .andExpect(jsonPath("$[1]", not(hasKey("user"))))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无标签")))
                .andExpect(jsonPath("$[2]", not(hasKey("user"))))
                .andExpect(status().isOk());
    }

    @Test
    void should_get_re_event_with_no_user() throws Exception {
        mockMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.eventName", is("第一条事件")))
                .andExpect(jsonPath("$.keyWord", is("无标签")))
                .andExpect(jsonPath("$", not(hasKey("user"))))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.eventName", is("第二条事件")))
                .andExpect(jsonPath("$.keyWord", is("无标签")))
                .andExpect(jsonPath("$", not(hasKey("user"))))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/3"))
                .andExpect(jsonPath("$.eventName", is("第三条事件")))
                .andExpect(jsonPath("$.keyWord", is("无标签")))
                .andExpect(jsonPath("$", not(hasKey("user"))))
                .andExpect(status().isOk());
    }
}
