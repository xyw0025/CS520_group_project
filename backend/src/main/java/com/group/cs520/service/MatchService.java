package com.group.cs520.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group.cs520.model.Match;
import com.group.cs520.repository.MatchRepository;


@Service
public class MatchService {
    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private UserService userService;

    public List<Match> allMatches() {
        return matchRepository.findAll();
    }

    public Match create(Map<String, String> matchMap) {
        Match match = new Match(matchMap);
        matchRepository.insert(match);

        List<String> userIds = TypeUtil.jsonStringArray(matchMap.get("userIds"));
        for (String userId: userIds) {
            userService.addMatch(userId, match);    
        }
        
        return match;
    }
}
