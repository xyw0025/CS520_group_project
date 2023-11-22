package com.group.cs520.service;
import org.bson.types.ObjectId;

public class TypeUtil {
    public static ObjectId ObjectIdConverter(String id) {
        return new ObjectId(id);
    }
}
