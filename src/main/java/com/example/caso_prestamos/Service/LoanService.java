package com.example.caso_prestamos.Service;

import com.example.caso_prestamos.Domain.Entity.Loan;

import java.util.List;

public interface LoanService {
    List<Loan> getAll();
    Loan findById(Long id);
    Loan create(Loan loan);
    Loan update(Long id, Loan loan);
}
