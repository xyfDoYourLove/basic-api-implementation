package com.thoughtworks.rslist;

import com.thoughtworks.rslist.api.RsController;
import com.thoughtworks.rslist.api.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
                .andExpect(jsonPath("$[0]", not(hasValue("user"))))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无标签")))
                .andExpect(jsonPath("$[1]", not(hasValue("user"))))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无标签")))
                .andExpect(jsonPath("$[2]", not(hasValue("user"))))
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
        String event = "{\"eventName\":\"石油降价了\",\"keyWord\":\"经济\",\"user\": {\"userName\":\"xyf\",\"age\": 19,\"gender\": \"male\",\"email\": \"a@b.com\",\"phone\": \"18888888888\",\"voteNum\":\"10\"}}";

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

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
        String event = "{\"eventName\":\"修改第三条事件\",\"keyWord\":\"\",\"user\": {\"userName\":\"xyf\",\"age\": 19,\"gender\": \"male\",\"email\": \"a@b.com\",\"phone\": \"18888888888\",\"voteNum\":\"10\"}}";

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
        String event = "{\"eventName\":\"添加一条热搜\",\"keyWord\":\"娱乐\",\"user\": {\"userName\":\"xyf\",\"age\": 19,\"gender\": \"male\",\"email\": \"a@b.com\",\"phone\": \"18888888888\",\"voteNum\":\"10\"}}";

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/user/list"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].userName", is("xyf")))
                .andExpect(jsonPath("$[1].age", is(19)))
                .andExpect(status().isOk());
    }

    @Test
    void should_not_register_user_when_add_rs_event_if_user_exist() throws Exception {
        String event = "{\"eventName\":\"添加一条热搜\",\"keyWord\":\"娱乐\",\"user\": {\"userName\":\"xiaowang\",\"age\": 19,\"gender\": \"male\",\"email\": \"a@b.com\",\"phone\": \"18888888888\",\"voteNum\":\"10\"}}";

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/user/list"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());
    }

    @Test
    void should_register_user_when_update_rs_event_if_user_not_exist() throws Exception {
        String event = "{\"eventName\":\"修改第三条事件\",\"keyWord\":\"\",\"user\": {\"userName\":\"xyf\",\"age\": 19,\"gender\": \"male\",\"email\": \"a@b.com\",\"phone\": \"18888888888\",\"voteNum\":\"10\"}}";

        mockMvc.perform(patch("/rs/3").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/user/list"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(status().isOk());
    }

    @Test
    void userName_should_not_null() throws Exception {
        String event = "{\"eventName\":,\"keyWord\":\"娱乐\",\"user\": {\"userName\":\"xyf\",\"age\": 19,\"gender\": \"male\",\"email\": \"a@b.com\",\"phone\": \"18888888888\",\"voteNum\":\"10\"}}";

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/user/list"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());
    }

    @Test
    void keyWord_should_not_null() throws Exception {
        String event = "{\"eventName\":\"添加一条事件\",\"keyWord\":,\"user\": {\"userName\":\"xyf\",\"age\": 19,\"gender\": \"male\",\"email\": \"a@b.com\",\"phone\": \"18888888888\",\"voteNum\":\"10\"}}";

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/user/list"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());
    }

    @Test
    void user_should_not_null() throws Exception {
        String event = "{\"eventName\":\"添加一条事件\",\"keyWord\":,\"user\":}";

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/user/list"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());
    }

    @Test
    void user_userName_should_less_then_8() throws Exception {
        String event = "{\"eventName\":\"添加一条事件\",\"keyWord\":,\"user\": {\"userName\":\"xyf4564654564\",\"age\": 19,\"gender\": \"male\",\"email\": \"a@b.com\",\"phone\": \"18888888888\",\"voteNum\":\"10\"}}";

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/user/list"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());
    }

    @Test
    void user_age_should_between_18_and_100() throws Exception {
        String event = "{\"eventName\":\"添加一条事件\",\"keyWord\":,\"user\": {\"userName\":\"xyf\",\"age\": 3,\"gender\": \"male\",\"email\": \"a@b.com\",\"phone\": \"18888888888\",\"voteNum\":\"10\"}}";

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/user/list"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());
    }

    @Test
    void user_email_should_suit_format() throws Exception {
        String event = "{\"eventName\":\"添加一条事件\",\"keyWord\":,\"user\": {\"userName\":\"xyf\",\"age\": 19,\"gender\": \"male\",\"email\": \"ab.com\",\"phone\": \"18888888888\",\"voteNum\":\"10\"}}";

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/user/list"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());
    }

    @Test
    void user_phone_should_suit_format() throws Exception {
        String event = "{\"eventName\":\"添加一条事件\",\"keyWord\":,\"user\": {\"userName\":\"xyf\",\"age\": 19,\"gender\": \"male\",\"email\": \"a@b.com\",\"phone\": \"1888888888811\",\"voteNum\":\"10\"}}";

        mockMvc.perform(post("/rs/event").content(event).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/user/list"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());
    }

}
