package com.thoughtworks.rslist.api.controller;

import com.thoughtworks.rslist.api.service.RsService;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.param.RsEventInputParam;
import com.thoughtworks.rslist.param.VoteInputParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Date;
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
    Boolean isSuccess = rsService.voteToRsEvent(rsEventId, voteInputParam);
    if (isSuccess) {
      return ResponseEntity.created(null).build();
    }
    return ResponseEntity.badRequest().build();
  }

  @GetMapping("/vote/list")
  public ResponseEntity getVoteListBetweenStartAndEnd(@RequestParam Date startTime, @RequestParam Date endTime, @RequestParam int pageIndex) {
    List<Vote> voteList = rsService.getVoteListBetween(startTime, endTime, pageIndex);
    if (voteList == null || voteList.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.ok(voteList);
  }
}
