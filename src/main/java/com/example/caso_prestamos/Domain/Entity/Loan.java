package com.example.caso_prestamos.Domain.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_dni", nullable = false)
    private User user; // Relación con User

    private Double amount; // Monto del préstamo
    private Integer months; // Duración en meses
    private Double interestRate; // Tasa de interés
    private LocalDate startDate; // Fecha de inicio
    private LocalDate endDate; // Fecha de fin

    @Enumerated(EnumType.STRING)
    private LoanStatus status; // Estado del préstamo


    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentSchedule> paymentScheduleList; // Lista de pagos asociados

}