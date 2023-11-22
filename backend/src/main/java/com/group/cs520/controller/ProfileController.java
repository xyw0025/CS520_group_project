package com.group.cs520.controller;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.group.cs520.model.Profile;
import com.group.cs520.service.ProfileService;



@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;


    @GetMapping("/{id}")
    public ResponseEntity<Profile> getSingleProfile(@PathVariable ObjectId id) {
        Profile profile = profileService.singleProfile(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"));
        return ResponseEntity.ok(profile);
    }


    @PostMapping()
    public ResponseEntity<?> createProfile(@RequestBody Map<String, String> payload) {
        try {
            // should do some to payload
            Profile profile = profileService.createProfile();

        } catch(Exception e) { //TODO should be specific
            // TODO
        }
    }



    @PutMapping("/{id}")
    public ResponseEntity<?> editProfile(@PathVariable ObjectId id, @RequestBody Map<String, String> payload) {
        try {

            // should do some to payload
            Profile profile = profileService.singleProfile(id);

        } catch(Exception e) {
            // TODO
        }
    }


    
    
}
