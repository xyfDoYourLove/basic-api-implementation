package com.thoughtworks.rslist.api.controller;

import com.thoughtworks.rslist.api.service.RsService;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.param.RsEventInputParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.thoughtworks.rslist.common.method.DataInitMethod.rsEvents;

@RestController
public class RsController {

  @Autowired
  RsService rsService;

  @GetMapping("/rs/list")
  public ResponseEntity getRsListBetween(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer end) {
    List<RsEvent> rsListBetween = null;
    try {
      rsListBetween = rsService.getRsListBetween(start, end);
    } catch (RsEventNotValidException e) {
      throw e;
    }
    return ResponseEntity.ok(rsListBetween);
  }

  @GetMapping("/rs/{index}")
  public ResponseEntity getRsListIndex(@PathVariable int index) {
    RsEvent rsEventIndex = null;
    try {
      rsEventIndex = rsService.getRsListIndex(index);
    } catch (RsEventNotValidException e) {
      throw e;
    }
    return ResponseEntity.ok(rsEventIndex);
  }

  @PostMapping("/rs/event")
  public ResponseEntity addRsEvent(@RequestBody @Valid RsEvent rsEvent) {
    ResponseEntity responseEntity = rsService.addRsEvent(rsEvent);
    return responseEntity;
  }

  @PatchMapping("/rs/list/{index}")
  public ResponseEntity updateRsEventIndex(@PathVariable int index, @RequestBody RsEvent rsEvent) {
    rsService.updateRsEventIndex(index, rsEvent);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/rs/{id}")
  public ResponseEntity deleteRsEventIndex(@PathVariable int id) {
    rsService.deleteRsEventIndex(id);
    return ResponseEntity.ok().build();
  }

  @PatchMapping("/rs/{rsEventId}")
  public ResponseEntity updateRsEventWhenUserMatch(@PathVariable int rsEventId, @RequestBody @Valid RsEventInputParam rsEventInputParam) {
    ResponseEntity responseEntity = rsService.updateRsEventWhenUserMatch(rsEventId, rsEventInputParam);
    return responseEntity;
  }
}
