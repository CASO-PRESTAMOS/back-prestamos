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
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Endpoint para obtener un usuario por DNI
    @GetMapping("/{identifier}")
    public ResponseEntity<User> getUserByIdentifier(@PathVariable String identifier) {
        return userService.getUserByIdentifier(identifier)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Endpoint para obtener todos los usuarios
    @GetMapping("/")
    public ResponseEntity<Iterable<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    // Endpoint para actualizar un usuario
    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        return new ResponseEntity<>(userService.updateUser(user), HttpStatus.OK); // METODO NO USADO YA QUE NO LE VEO SENTIDO, PERO LO DEJO POR SI LO OCUPAMOS LUEGO
    }

    // Endpoint para eliminar un usuario por DNI
    @DeleteMapping("/delete/{identifier}")
    public ResponseEntity<Void> deleteUser(@PathVariable String identifier) {
        try {
            userService.deleteUser(identifier);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
