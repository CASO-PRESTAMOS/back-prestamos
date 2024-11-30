package com.example.caso_prestamos.Domain.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor

public class PaymentSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan; // Relación con Loan

    private LocalDate paymentDate; // Fecha del pago
    private Double amount; // Monto a pagar en esa fecha

    @Enumerated(EnumType.STRING)
    private PaymentStatus status; // Estado del pago

    private LocalDate lateSince;

}
