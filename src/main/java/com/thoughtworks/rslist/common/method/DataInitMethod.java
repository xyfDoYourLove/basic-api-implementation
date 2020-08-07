package com.thoughtworks.rslist.common.method;

import com.thoughtworks.rslist.api.repository.UserRepository;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class DataInitMethod {

    public static List<RsEvent> rsEvents = new ArrayList<>();
    public static List<User> userList = new ArrayList<>();

    public static void initRsEvents() {
        rsEvents.clear();
        User user = new User("xiaowang", "female", 18, "a@thoughtworks.com", "18888888888", 10);
        rsEvents.add(new RsEvent("第一条事件", "无标签", 0, user));
        rsEvents.add(new RsEvent("第二条事件", "无标签", 0, user));
        rsEvents.add(new RsEvent("第三条事件", "无标签", 0, user));
    }

    public static void initUserTable() {
        userList.clear();
        User user = new User("xiaowang", "male", 19, "xiao@thought.com", "19999999999", 10);
        userList.add(user);
    }

}
