package com.group.cs520.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.query.Criteria;

import org.bson.types.ObjectId;
import java.util.Optional;
import java.util.List;

import com.group.cs520.model.Preference;
import com.group.cs520.model.User;
import java.util.Map;

import com.group.cs520.repository.PreferenceRepository;
import org.springframework.data.mongodb.core.MongoTemplate;

@Service
public class PreferenceService {
    @Autowired
    private PreferenceRepository preferenceRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private MongoTemplate mongoTemplate;


    public Preference create(Map<String, String> preferenceMap) {
        Preference preference = new Preference(preferenceMap);
        preferenceRepository.insert(preference);
        profileService.setPreference(preferenceMap.get("profile_id"), preference);
        return preference;
    }

    // find user's perferences using user's id
    // public Optional<List<Preference>> userPreferences(String user_id) {
        // Query query = new Query(Criteria.where("id").is(user_id));
        // User user = mongoTemplate.find(query, User.class);
        // Profile profile = profileService.getProfileByUser(user_id);
        
    // }
}
