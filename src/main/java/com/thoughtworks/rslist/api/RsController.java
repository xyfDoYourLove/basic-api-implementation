package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {

  private static List<RsEvent> rsEvents;

  public static void initRsEvents() {
    rsEvents = new ArrayList<>();
    User user = new User("xiaowang", "female", 18, "a@thoughtworks.com", "18888888888", 10);
    rsEvents.add(new RsEvent("第一条事件", "无标签", user));
    rsEvents.add(new RsEvent("第二条事件", "无标签", user));
    rsEvents.add(new RsEvent("第三条事件", "无标签", user));
  }

  @GetMapping("/rs/list")
  public ResponseEntity getRsListBetween(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer end) {
    if (start == null || end == null) {
      return ResponseEntity.ok(rsEvents);
    }
    return ResponseEntity.ok(rsEvents.subList(start - 1, end));
  }

  @GetMapping("/rs/{index}")
  public ResponseEntity getRsListIndex(@PathVariable int index) {
    return ResponseEntity.ok(rsEvents.get(index - 1));
  }

  @PostMapping("/rs/event")
  public ResponseEntity addRsEvent(@RequestBody @Valid RsEvent rsEvent) {
    rsEvents.add(rsEvent);
    if (!isAlreadyRegistered(rsEvent.getUser())) {
      UserController.userList.add(rsEvent.getUser());
    }
    return ResponseEntity.created(null).header("index", String.valueOf(rsEvents.size() - 1)).build();
  }

  public Boolean isAlreadyRegistered(User user) {

    for (int i = 0; i < UserController.userList.size(); i++) {
      if (user.getUserName().equals(UserController.userList.get(i).getUserName())) {
        return true;
      }
    }
    return false;
  }

  @PatchMapping("/rs/{index}")
  public ResponseEntity updateRsEventIndex(@PathVariable int index, @RequestBody RsEvent rsEvent) {
    RsEvent rsEventToUpdate = rsEvents.get(index - 1);
    if (rsEvent.getEventName() != null && rsEvent.getEventName() != "") {
      rsEventToUpdate.setEventName(rsEvent.getEventName());
    }
    if (rsEvent.getKeyWord() != null && rsEvent.getKeyWord() != "") {
      rsEventToUpdate.setKeyWord(rsEvent.getKeyWord());
    }
    rsEvents.set(index - 1, rsEventToUpdate);

    if (!isAlreadyRegistered(rsEvent.getUser())) {
      UserController.userList.add(rsEvent.getUser());
    }

    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/rs/{index}")
  public ResponseEntity deleteRsEventIndex(@PathVariable int index) {
    rsEvents.remove(index - 1);
    return ResponseEntity.ok().build();
  }
}
