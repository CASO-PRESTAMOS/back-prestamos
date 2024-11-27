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
    private String dni; // El DNI será la clave primaria

    private String firstNames;
    private String lastName;
    private String lastMotherName;
}
