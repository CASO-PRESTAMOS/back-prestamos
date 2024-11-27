package com.example.caso_prestamos.Service;


import com.example.caso_prestamos.Domain.Entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(String dni);
    Optional<User> getUserByDni(String dni);
    List<User> getAllUsers();
    User updateUser(User user); // METODO NO USADO YA QUE NO LE VEO SENTIDO, PERO LO DEJO POR SI LO OCUPAMOS LUEGO
    void deleteUser(String dni);
}
