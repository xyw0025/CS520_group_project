package com.group.cs520.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group.cs520.documentation.PreferenceApi;
import com.group.cs520.model.Preference;
import com.group.cs520.model.User;
import com.group.cs520.service.PreferenceService;


@RestController
@RequestMapping("/api/v1/preference")
public class PrefrenceController implements PreferenceApi {

    @Autowired
    PreferenceService preferenceService;

    @Override
    @GetMapping
    public ResponseEntity<List<Preference>> getAllPreferences() {
        List<Preference> prferences = preferenceService.allPreferences();
        return ResponseEntity.ok(prferences);
    }

    @Override
    @PostMapping()
    public ResponseEntity<?> createPreference(@RequestBody Map<String, String> payload) {
        try {
            Preference preference = preferenceService.create(payload);
            return ResponseEntity.ok(preference);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", e.getMessage()));
        }

    }
}
