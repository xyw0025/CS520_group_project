package com.group.cs520.controller;

import java.util.Collections;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;

import com.group.cs520.model.Profile;
import com.group.cs520.model.User;
import com.group.cs520.service.ProfileService;
import com.group.cs520.service.TypeUtil;
import com.group.cs520.service.UserService;
import com.group.cs520.service.GCPStorageService;




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
        return ResponseEntity.ok(profileService.getProfileByUser(user_id));
    }


    @GetMapping("/{id}")
    public ResponseEntity<Profile> getSingleProfile(@PathVariable ObjectId id) {
        Profile profile = profileService.singleProfile(id);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/{profile_id}/update_preferences")
    public ResponseEntity<?> updateProfilePreferences(@PathVariable String profile_id, @RequestBody Map<String, String> payload) {
        Profile profile = profileService.updatePreferences(profile_id, payload);
        return ResponseEntity.ok(profile);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable ObjectId id, @RequestBody Map<String, String> payload) {
        try {
            Profile profile = profileService.update(id, payload);
            return ResponseEntity.ok(profile);

        } catch (Exception error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", error.getMessage()));
        }
    }

    @Autowired
    private GCPStorageService gcpStorageService;

    @PostMapping("/{id}/upload")
    public ResponseEntity<String> uploadImage(@PathVariable String id, @RequestParam("file") MultipartFile file) throws IOException {
        String photoURL = gcpStorageService.uploadImage(file);
        profileService.setProfilePhoto(id, photoURL);
        return ResponseEntity.ok(photoURL);
    }
}
