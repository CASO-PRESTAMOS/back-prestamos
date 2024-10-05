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
@Table(name = "Cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCliente;

    @Column(name = "nombre", nullable = false)
    private String nombre;
    @Column(name = "DNI", nullable = false)
    private String DNI;
    @Column(name = "direccion", nullable = false)
    private String direccion;
    @Column(name = "rol", nullable = false)
    private String rol;

}
