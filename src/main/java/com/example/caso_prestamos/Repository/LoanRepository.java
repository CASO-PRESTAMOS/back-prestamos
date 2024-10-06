package com.example.caso_prestamos.Repository;

import com.example.caso_prestamos.Domain.Entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {
}
