package com.group.cs520.service;
import java.util.List;

import org.bson.types.ObjectId;
import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class TypeUtil {
    public static ObjectId objectIdConverter(String id) {
        return new ObjectId(id);
    }

    public static List<String> jsonStringArray(String jsonArray) {
        Gson converter = new Gson();                  
        Type type = new TypeToken<List<String>>(){}.getType();
        return converter.fromJson(jsonArray, type);
    }
}
