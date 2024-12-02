package com.example.caso_prestamos.Service;

import com.example.caso_prestamos.Domain.Entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(String identifier);
    Optional<User> getUserByIdentifier(String identifier);
    List<User> getAllUsers();
}
