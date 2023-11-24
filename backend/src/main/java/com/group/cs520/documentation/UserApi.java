package com.group.cs520.documentation;
import com.group.cs520.model.User;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;


@Tag(name = "User API")
public interface UserApi {

    @Operation(summary = "Retrieves all users")
    ResponseEntity<List<User>> getAllUsers();

    @Operation(summary = "Retrieves all active users")
    ResponseEntity<List<User>> getActiveUsers();

    @Operation(summary = "Retrieves a single user by email")
    ResponseEntity<User> getSingleUserByParam(String email);

    @Operation(summary = "Creates a new user")
    ResponseEntity<?> createUser(Map<String, String> payload);

    @Operation(summary = "Logs in a user")
    ResponseEntity<?> loginUser(@RequestBody Map<String, String> credentials, HttpServletResponse response);

    @Operation(summary = "Logs out a user")
    ResponseEntity<?> logoutUser(HttpServletResponse response);

    @Operation(summary = "Retrieves the current user")
    ResponseEntity<User> getCurrentUser(HttpServletRequest request);

    @Operation(summary = "Retrieves a single user by ID")
    ResponseEntity<User> getSingleUser(ObjectId id);
}
