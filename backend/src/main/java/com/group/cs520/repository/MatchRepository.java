package com.group.cs520.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.bson.types.ObjectId;

import com.group.cs520.model.Match;



@Repository
public interface MatchRepository extends MongoRepository<Match, ObjectId> {
    List<Match> findByStatusAndUserIdsContaining(Integer status, ObjectId userId);
}