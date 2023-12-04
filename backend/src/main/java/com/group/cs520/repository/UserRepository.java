package com.group.cs520.repository;

import com.group.cs520.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.Aggregation;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
    List<User> findByIsActiveTrue();
    Optional<User> findUserByEmail(String email);

    @Aggregation(pipeline = { "{ $sample: { size: ?0 } }" })
    List<User> findRandomUsers(int limit);

}
