package com.group.cs520.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.group.cs520.model.Message;

public interface MessageRepository extends MongoRepository<Message, ObjectId> {

}