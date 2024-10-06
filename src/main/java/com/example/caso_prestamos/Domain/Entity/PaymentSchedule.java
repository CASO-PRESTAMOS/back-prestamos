package com.example.caso_prestamos.Domain.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor

public class PaymentSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // Relación con Loan
    @JoinColumn(name = "loan_id", nullable = false) // loan_id será la clave foránea
    private Loan loan;

    private LocalDateTime paymentDueDate;

    private boolean paid;

    private Double installmentAmount;
}
