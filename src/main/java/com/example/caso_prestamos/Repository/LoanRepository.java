package com.example.caso_prestamos.Repository;

import com.example.caso_prestamos.Domain.Entity.Loan;
import com.example.caso_prestamos.Domain.Entity.LoanStatus;
import com.example.caso_prestamos.Domain.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByUser(User user);
    Loan findFirstByUserAndStatusNot(User user, LoanStatus status);
}
