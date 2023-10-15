package com.group.cs520.service;

import com.group.cs520.model.User;
import com.group.cs520.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public List<User> allUsers() {
        return userRepository.findAll();
    }

    public Optional<User> singleUser(ObjectId id) {
        return userRepository.findById(id);
    }



    public User createUser(String email, String password) {
        return userRepository.insert(new User(email, password));
    }

//    public List<Optional<User>> activeUsers() {
//        return userRepository.findActiveUsers();
//    }

    public Optional<User> singleUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}
