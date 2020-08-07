package com.thoughtworks.rslist.api.controller;

import com.thoughtworks.rslist.api.service.RsService;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.param.RsEventInputParam;
import com.thoughtworks.rslist.param.VoteInputParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
public class RsController {

  @Autowired
  RsService rsService;

  @GetMapping("/rs/list")
  public ResponseEntity getRsListBetween(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer end) {
    List<RsEvent> rsList = rsService.getRsList();
    return ResponseEntity.ok(rsList);
  }

  @GetMapping("/rs/{id}")
  public ResponseEntity getRsListIndex(@PathVariable int id) {
    RsEvent rsEventById = rsService.getRsEventById(id);
    return ResponseEntity.ok(rsEventById);
  }

  @PostMapping("/rs/event")
  public ResponseEntity addRsEvent(@RequestBody @Valid RsEvent rsEvent) {
    Integer isUserExist = rsService.addRsEvent(rsEvent);
    if (isUserExist == null) {
      return ResponseEntity.badRequest().header("id", String.valueOf(-1)).build();
    }
    return ResponseEntity.created(null).header("id", String.valueOf(isUserExist)).build();
  }

  @PatchMapping("/rs/{rsEventId}")
  public ResponseEntity updateRsEventWhenUserMatch(@PathVariable int rsEventId, @RequestBody @Valid RsEventInputParam rsEventInputParam) {
    Boolean isSuccess = rsService.updateRsEventWhenUserMatch(rsEventId, rsEventInputParam);
    if (isSuccess) {
      return ResponseEntity.ok().build();
    }
    return ResponseEntity.badRequest().build();
  }

  @DeleteMapping("/rs/{id}")
  public ResponseEntity deleteRsEventIndex(@PathVariable int id) {
    rsService.deleteRsEventById(id);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/rs/vote/{rsEventId}")
  public ResponseEntity voteToRsEvent(@PathVariable int rsEventId, @RequestBody @Valid VoteInputParam voteInputParam) {
    ResponseEntity responseEntity = rsService.voteToRsEvent(rsEventId, voteInputParam);
    return responseEntity;
  }
}
