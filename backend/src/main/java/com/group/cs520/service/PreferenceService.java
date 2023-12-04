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
import com.group.cs520.model.Profile;
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
    private MongoTemplate mongoTemplate;


    public List<Preference> allPreferences() {
        return preferenceRepository.findAll();
    }

    public Preference singlePreference(String id) {
        ObjectId preferenceId = TypeUtil.objectIdConverter(id);
        return preferenceRepository.findById(preferenceId).orElseThrow(() -> new IllegalArgumentException("Preference not found"));
    }


    public Preference PreferenceByName(String name) {
        return preferenceRepository.findPreferenceByName(name).orElseThrow(() -> new IllegalArgumentException("Preference not found"));
    }

    public Preference create(Map<String, String> preferenceMap) {
        Preference preference = new Preference(preferenceMap);
        preferenceRepository.insert(preference);
        return preference;
    }

    // find user's perferences using user's id
    // public Optional<List<Preference>> userPreferences(String user_id) {
        // Profile profile = profileService.getProfileByUser(user_id);
        // profileService.
        // return profile.getPreferences();
        // Profile profile = profileService.getProfileByUser(user_id);
        
    // }
}
