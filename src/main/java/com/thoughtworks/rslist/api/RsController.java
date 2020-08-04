package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class RsController {

  private static List<RsEvent> rsEvents;

  public static void initRsEvents() {
    rsEvents = new ArrayList<>();
    rsEvents.add(new RsEvent("第一条事件", "无标签"));
    rsEvents.add(new RsEvent("第二条事件", "无标签"));
    rsEvents.add(new RsEvent("第三条事件", "无标签"));
  }

  @GetMapping("/rs/list")
  public List<RsEvent> getRsListBetween(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer end) {
    if (start == null || end == null) {
      return rsEvents;
    }
    return rsEvents.subList(start - 1, end);
  }

  @GetMapping("/rs/{index}")
  public RsEvent getRsListIndex(@PathVariable int index) {
    return rsEvents.get(index - 1);
  }

  @PostMapping("/rs/event")
  public void addRsEvent(@RequestBody RsEvent rsEvent) {
    rsEvents.add(rsEvent);
  }

  @PatchMapping("/rs/{index}")
  public void updateRsEventIndex(@PathVariable int index, @RequestBody RsEvent rsEvent) {
    RsEvent rsEventToUpdate = rsEvents.get(index - 1);
    if (rsEvent.getEventName() != null && rsEvent.getEventName() != "") {
      rsEventToUpdate.setEventName(rsEvent.getEventName());
    }
    if (rsEvent.getKeyWord() != null && rsEvent.getKeyWord() != "") {
      rsEventToUpdate.setKeyWord(rsEvent.getKeyWord());
    }
    rsEvents.set(index - 1, rsEventToUpdate);
  }

  @DeleteMapping("/rs/{index}")
  public void deleteRsEventIndex(@PathVariable int index) {
    rsEvents.remove(index - 1);
  }
}
