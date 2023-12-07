package com.group.cs520.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group.cs520.documentation.MatchApi;
import com.group.cs520.model.Match;
import com.group.cs520.model.User;
import com.group.cs520.service.MatchService;



@RestController
@RequestMapping("/api/v1/match")
public class MatchController implements MatchApi {
    @Autowired
    MatchService matchService;

    @Override
    @GetMapping
    public ResponseEntity<List<Match>> getAllMatches() {
        List<Match> matches = matchService.allMatches();
        return ResponseEntity.ok(matches);
    }

    @Override
    @GetMapping("matched")
    public ResponseEntity<List<Match>> getAllSuccessMatches() {
        List <Match> matches = matchService.allSuccessMatches().orElseThrow();
        return ResponseEntity.ok(matches);
    }

    @Override
    @GetMapping("userIds")
    public ResponseEntity<?> getMatchByUserIds(@RequestParam(name = "userId1") String userId1, @RequestParam(name = "userId2") String userId2) {
        try {
            Match match = matchService.matchByUserIds(userId1, userId2);

            // or Arrays.asList(userIds.split(",")) if the pass in param is a string "[id1],[id2]"
            return ResponseEntity.ok(match);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @Override
    @PostMapping("add-match-history")
    public ResponseEntity<?> updateMatchHistory(@RequestBody Map<String, Object> payload) {
        // senderId, receiverId, behavior
        try {
            Match match = matchService.updateMatchHistory(payload);
            return ResponseEntity.ok(match);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", e.getMessage()));
        }
    }
}
