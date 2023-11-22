package com.group.cs520.service;
import com.group.cs520.model.Profile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.cs520.model.Preference;
import com.group.cs520.model.User;
import com.group.cs520.repository.ProfileRepository;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

import java.lang.reflect.Field;

@Service
public class ProfileService {
    public ProfileService() {

    }

    @Autowired
    private ProfileRepository profileRepository;


    public Optional<Profile> singleProfile(ObjectId id) {
        return profileRepository.findById(id);
    }


    public Profile createProfile(Map<String, String> profileMap) {
        // String[] keys = {"displayName", "gender", "age", "bio"};
        // Map<String, String> map = profileMap.entrySet().stream().filter(x -> x.getKey())
        Profile profile = new Profile(profileMap);
        return profileRepository.insert(profile);
    }

    // TODO: edit

}
