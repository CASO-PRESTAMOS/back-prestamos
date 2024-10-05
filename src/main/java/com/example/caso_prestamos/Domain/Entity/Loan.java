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
    private Long loanID;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false, foreignKey = @ForeignKey(name = "FK_user_loan"))
    private User userID;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "expired_date", nullable = false)
    private LocalDateTime expireDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Column(name = "interest_rate", nullable = false)
    private Double interestRate;
}
