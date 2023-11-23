package com.group.cs520.repository;

import com.group.cs520.model.Profile;
import com.group.cs520.model.User;
import com.group.cs520.repository.ProfileRepository;
import com.group.cs520.model.Preference;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends MongoRepository<Profile, ObjectId> {
}
