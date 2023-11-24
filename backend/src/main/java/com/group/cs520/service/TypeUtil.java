package com.group.cs520.service;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.group.cs520.constants.ProfileConstants;


public class TypeUtil {
    public static ObjectId objectIdConverter(String id) {
        return new ObjectId(id);
    }

    public static List<String> jsonStringArray(String jsonArray) {
        Gson converter = new Gson();                  
        Type type = new TypeToken<List<String>>(){}.getType();
        return converter.fromJson(jsonArray, type);
    }

    public static List<ObjectId> objectIdArray(String jsonArray) {
        List<String> stringList = jsonStringArray(jsonArray);
        List<ObjectId> objIds = new ArrayList<>();

        for (String element: stringList) {
            objIds.add(objectIdConverter(element));
        }

        return objIds;
    }


    public static void setField(Object object, Field field, String value) {
        try {
            field.setAccessible(true);
            Class<?> fieldType = field.getType();

            if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
                field.set(object, Integer.parseInt(value));
            } else if (fieldType.equals(List.class)) {
                field.set(object, TypeUtil.jsonStringArray(value));
            } else if (fieldType.equals(ObjectId.class)) {
                field.set(object, objectIdArray(value));
            } else {
                field.set(object, value);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error setting field: " + field.getName(), e);
        }
    }


    public static Integer getGender(String gender) {
        return ProfileConstants.Gender.valueOf(gender.toUpperCase()).ordinal();
    }
}
