package com.example.caso_prestamos.Domain.Entity;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanId;

    private String clientName;
    private String clientDNI;
    private String clientAddress;
    private Double amount;
    private Integer duration; // Duration in months
    private Double interestRate;
    private Double totalAmount;

    private LocalDateTime startDate;
    private LocalDateTime expireDate;

    @Enumerated(EnumType.STRING)
    private Status status;
}