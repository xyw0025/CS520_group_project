package com.group.cs520.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.group.cs520.constants.MatchConstants;
import com.group.cs520.model.Match;
import com.group.cs520.model.MatchHistory;
import com.group.cs520.repository.MatchRepository;
import com.group.cs520.repository.MatchHistoryRepository;
import java.util.ArrayList;


@Service
public class MatchService {
    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private MatchHistoryRepository matchHistoryRepository;

    @Autowired
    private UserService userService;

    public List<Match> allMatches() {
        return matchRepository.findAll();
    }

    public Optional<List<Match>> allSuccessMatches() {
        return matchRepository.findByStatus(1);
    }

    public Match create(Map<String, Object> matchMap) {
        Match match = new Match(matchMap);
        matchRepository.insert(match);

        List<String> userIds = TypeUtil.jsonStringArray(matchMap.get("userIds").toString());
        for (String userId: userIds) {
            userService.addMatch(userId, match);    
        }
        return match;
    }


    public Match matchByUserIds(Map<String, Object> matchMap) {
        List<ObjectId> userIds = TypeUtil.objectIdArray(matchMap.get("userIds").toString());
        return matchByUserIds(userIds).orElseThrow();
    }

    private Optional<Match> matchByUserIds(List<ObjectId> userIds) {
        Optional<Match> match = matchRepository.findByUserIds(userIds);
        if (!match.isPresent()) {
            Collections.reverse(userIds);
            match = matchRepository.findByUserIds(userIds);
        }
        return match;
    }


    public Match updateMatchHistory(Map<String, Object> matchMap) {
        // 1. find match 

        // pass in two string 
        // sort them 
        // convert to object list
        String senderId = matchMap.get("senderId").toString();
        String receiverId = matchMap.get("receiverId").toString();
        String behavior = matchMap.get("behavior").toString();

        String[] ids = {senderId, receiverId};
        Arrays.sort(ids);

        Match match = matchRepository.findByUserIds(TypeUtil.listStringToListObjectID(ids))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Match not found"));

        // 2. update match history
        MatchHistory matchHistory = new MatchHistory(senderId, receiverId, behavior);
        updateMatchHistory(match, matchHistory);
        updateMatchStatus(match);
        matchRepository.save(match);
        return match;
    }

    private void updateMatchHistory(Match match, MatchHistory matchHistory) {
        matchHistoryRepository.insert(matchHistory);
        List<MatchHistory> histories = new ArrayList<>(match.getMatchHistories());
        histories.add(matchHistory);
        match.setMatchHistories(histories);
    }


    private void updateMatchStatus(Match match) {
        long acceptCount = match.getMatchHistories().stream()
            .filter(matchHistory -> matchHistory.getBehavior() == MatchConstants.BEHAVIOR.ACCEPT.ordinal())
            .count();

        long rejectCount = match.getMatchHistories().stream()
            .filter(matchHistory -> matchHistory.getBehavior() == MatchConstants.BEHAVIOR.REJECT.ordinal())
            .count();

        if (acceptCount == 2) {
            match.setStatus(MatchConstants.STATUS.MATCHED.ordinal());
        } else if (rejectCount == 2) {
            match.setStatus(MatchConstants.STATUS.FAILED.ordinal());
        } else {
            match.setStatus(MatchConstants.STATUS.AWAIT.ordinal());
        }
    }
}
