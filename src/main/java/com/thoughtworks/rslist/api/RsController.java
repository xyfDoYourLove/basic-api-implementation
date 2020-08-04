package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class RsController {
  private List<RsEvent> rsEvents = initRsList();

  private List<RsEvent> initRsList() {
    List<RsEvent> rsEvents = new ArrayList<>();
    rsEvents.add(new RsEvent("第一条事件", "无标签"));
    rsEvents.add(new RsEvent("第二条事件", "无标签"));
    rsEvents.add(new RsEvent("第三条事件", "无标签"));
    return rsEvents;
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
}
