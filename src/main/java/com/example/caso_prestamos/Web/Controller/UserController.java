package com.example.caso_prestamos.Web.Controller;

import com.example.caso_prestamos.Domain.Entity.User;
import com.example.caso_prestamos.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/admin/users")
public class UserController {

    private final UserService userService;

    // Endpoint para crear un usuario a partir de su DNI
    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestParam String identifier) {
        try {
            if (identifier.length() != 8 && identifier.length() != 11) {
                throw new IllegalArgumentException("El identificador debe ser un DNI (8 dígitos) o RUC (11 dígitos).");
            }
            User user = userService.createUser(identifier);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            throw e; // Esto será manejado por CustomExceptionHandler
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el usuario: " + e.getMessage(), e);
        }
    }

    // Endpoint para obtener un usuario por DNI
    @GetMapping("/{identifier}")
    public ResponseEntity<User> getUserByIdentifier(@PathVariable String identifier) {
        try {
            return userService.getUserByIdentifier(identifier)
                    .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                    .orElseThrow(() -> new IllegalArgumentException("Usuario con identificador " + identifier + " no encontrado."));
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener el usuario: " + e.getMessage(), e);
        }
    }

    // Endpoint para obtener todos los usuarios
    @GetMapping("/")
    public ResponseEntity<Iterable<User>> getAllUsers() {
        try {
            Iterable<User> users = userService.getAllUsers();
            if (!users.iterator().hasNext()) {
                throw new IllegalArgumentException("No hay usuarios registrados.");
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            throw e; // Será manejado por CustomExceptionHandler
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener la lista de usuarios: " + e.getMessage(), e);
        }
    }

}

