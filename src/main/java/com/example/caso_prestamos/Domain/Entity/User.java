package com.example.caso_prestamos.Domain.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    private String identifier; // Puede ser DNI (8 dígitos) o RUC (11 dígitos)

    private String fullName; // Se usará tanto para nombres completos como razón social
}
