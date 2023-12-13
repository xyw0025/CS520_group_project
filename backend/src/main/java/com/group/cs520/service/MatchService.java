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
import com.group.cs520.model.User;
import com.group.cs520.model.Match;
import com.group.cs520.model.MatchHistory;
import com.group.cs520.model.Conversation;
import com.group.cs520.repository.UserRepository;
import com.group.cs520.repository.MatchRepository;
import com.group.cs520.repository.MatchHistoryRepository;
import com.group.cs520.repository.ConversationRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;



@Service
public class MatchService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private MatchHistoryRepository matchHistoryRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private UserService userService;

    public List<Match> allMatches() {
        return matchRepository.findAll();
    }

    public Optional<List<Match>> allSuccessMatches() {
        return matchRepository.findByStatus(MatchConstants.STATUS.MATCHED.ordinal());
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

    public Match matchByUserIds(String id1, String id2) {
        List<ObjectId> userIds = Arrays.asList(TypeUtil.objectIdConverter(id1), TypeUtil.objectIdConverter(id2));
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
        // convert to object list
        String senderId = matchMap.get("senderId").toString();
        String receiverId = matchMap.get("receiverId").toString();
        String behavior = matchMap.get("behavior").toString();

        List<String> ids = Arrays.asList(senderId, receiverId);

        List<ObjectId> userIds = ids.stream()
            .map(ObjectId::new) // Fix the method reference here
            .collect(Collectors.toList());

        Match match = matchByUserIds(userIds)
            .orElseGet(() -> {
                Match newMatch = new Match(userIds);
                matchRepository.insert(newMatch);
                return newMatch;
            });

        // 2. update match history
        MatchHistory matchHistory = new MatchHistory(senderId, receiverId, behavior);
        updateMatchHistory(match, matchHistory);
        updateMatchStatus(match);
        matchRepository.save(match);
        return match;
    }

    private void updateMatchHistory(Match match, MatchHistory matchHistory) {
        matchHistoryRepository.insert(matchHistory);
        List<MatchHistory> histories = match.getMatchHistories() == null ? new ArrayList<>() : new ArrayList<>(match.getMatchHistories());
        histories.add(matchHistory);
        match.setMatchHistories(histories);
        matchRepository.save(match);
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
            createConversation(match.getUserIds());
        } else if (rejectCount == 2) {
            match.setStatus(MatchConstants.STATUS.FAILED.ordinal());
        } else {
            match.setStatus(MatchConstants.STATUS.AWAIT.ordinal());
        }
    }

    private void createConversation(List<ObjectId> userIds) {
        if (userIds.size() >= 2) {
            Conversation newConversation = new Conversation(userIds.get(0), userIds.get(1));
            conversationRepository.save(newConversation);
        }
    }

    public List<User> getMatchedUsers(ObjectId userId) {
        // Find matchings where status is 1 and userIds array contains the provided userId
        List<Match> matches = matchRepository.findByStatusAndUserIdsContaining(1, userId);

        // Extract the userIds and remove the provided userId
        List<ObjectId> matchedUserIds = matches.stream()
                .flatMap(match -> match.getUserIds().stream())
                .distinct()
                .filter(ids -> !ids.equals(userId))
                .collect(Collectors.toList());

        // Find all users that match the userIds
        return userRepository.findAllById(matchedUserIds);
    }
}
