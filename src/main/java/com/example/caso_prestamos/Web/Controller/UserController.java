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

    // Endpoint para actualizar un usuario
    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
    // METODO NO USADO YA QUE NO LE VEO SENTIDO, PERO LO DEJO POR SI LO OCUPAMOS LUEGO
        try {
            if (user.getIdentifier() == null) {
                throw new IllegalArgumentException("El usuario debe tener un ID válido para ser actualizado.");
            }
            User updatedUser = userService.updateUser(user);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            throw e; // Será manejado por CustomExceptionHandler
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el usuario: " + e.getMessage(), e);
        }
    }

    // Endpoint para eliminar un usuario por DNI
    @DeleteMapping("/delete/{identifier}")
    public ResponseEntity<Void> deleteUser(@PathVariable String identifier) {
        try {
            if (identifier.isBlank()) {
                throw new IllegalArgumentException("El identificador no puede estar vacío.");
            }
            userService.deleteUser(identifier);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            throw e; // Será manejado por CustomExceptionHandler
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el usuario con identificador: " + identifier, e);
        }
    }

}

