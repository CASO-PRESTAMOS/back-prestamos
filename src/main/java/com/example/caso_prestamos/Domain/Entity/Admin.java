package com.example.caso_prestamos.Domain.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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


}