package com.example.caso_prestamos.Domain.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor

public class Admin  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idAdmin;

    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;


}