package com.group.cs520.service;
import com.group.cs520.model.Profile;
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

@Service
public class ProfileService {
    public ProfileService() {

    }

    @Autowired
    private ProfileRepository profileRepository;


    public Optional<Profile> singleProfile(ObjectId id) {
        return profileRepository.findById(id);
    }


    public Profile createProfile(Map<string, string> profileMap) {
        // TODO: payload parsing 
        // Profile profile = new Profile();
        // profile.setCreatedTime(Instant.now());
        // profile.setUpdatedTime(Instant.now());
        // return profileRepository.insert(profile);
    }

    // TODO: edit

}
