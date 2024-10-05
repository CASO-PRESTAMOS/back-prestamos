package com.example.caso_prestamos.Domain.Entity;

import com.example.caso_prestamos.Domain.Enum.Duracion;
import com.example.caso_prestamos.Domain.Enum.Estado;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "Prestamo")
public class Prestamo{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idPrest;

    @Column(name = "fechainicio", nullable = false)
    private LocalDateTime fechaInicio;
    @Column(name = "fechavencimiento", nullable = false)
    private LocalDateTime fechaVencimiento;
    @Column(name = "monto")
    private Float monto;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private Estado estado;

    @Enumerated(EnumType.STRING)
    @Column(name = "duracion")
    private Duracion duracion;

    @ManyToOne
    @JoinColumn(name = "id_cliente", referencedColumnName = "idCliente")
    private Cliente cliente;

}