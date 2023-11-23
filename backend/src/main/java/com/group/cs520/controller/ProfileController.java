package com.group.cs520.controller;

import java.util.Collections;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.group.cs520.model.Profile;
import com.group.cs520.model.User;
import com.group.cs520.service.ProfileService;
import com.group.cs520.service.TypeUtil;
import com.group.cs520.service.UserService;


import java.util.List;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserService userService;


    @GetMapping()
    public ResponseEntity<List<Profile>> getAllProfile() {
        List<Profile> profiles = profileService.allProfiles();
        return ResponseEntity.ok(profiles);
    }


    @GetMapping("user")
    public ResponseEntity<Profile> getProfileByUser(@RequestParam String user_id) {
        ObjectId userObjId = TypeUtil.objectIdConverter(user_id);
        return ResponseEntity.ok(profileService.getProfileByUser(userObjId));
    }


    @GetMapping("/{id}")
    public ResponseEntity<Profile> getSingleProfile(@PathVariable ObjectId id) {
        Profile profile = profileService.singleProfile(id);
        return ResponseEntity.ok(profile);
    }

    @PostMapping()
    public ResponseEntity<?> createProfile(@RequestBody Map<String, String> payload) {
        try {
            // should do some to payload
            System.out.print(payload);
            Profile profile = profileService.create(payload);
            return ResponseEntity.ok(profile);
        } catch(Exception e) { // TODO should be specific
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", e.getMessage()));
        }
    }


    // TODO: update
    // @PutMapping("/{id}")
    // public ResponseEntity<?> updateProfile(@PathVariable ObjectId id, @RequestBody Map<String, String> payload) {
    //     // should do some to payload
    //     try {
    //         profileService.update(id, payload);

    //     } catch (Exception error) {
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", e.getMessage()));;
    //     }
    // }


    
    
}
