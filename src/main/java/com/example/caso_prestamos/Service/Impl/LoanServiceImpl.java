package com.example.caso_prestamos.Service.Impl;

import com.example.caso_prestamos.Domain.Entity.Loan;
import com.example.caso_prestamos.Domain.Entity.Status;
import com.example.caso_prestamos.Repository.LoanRepository;
import com.example.caso_prestamos.Service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Loan> getAll() {
        return loanRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Loan findById(Long id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found"));
    }

    @Override
    @Transactional
    public Loan create(Loan loan) {
        // Lógica de negocio que asigna las tasas de interés basadas en la duración del préstamo
        if (loan.getDuration() == 2) {
            loan.setInterestRate(0.1);
        } else if (loan.getDuration() == 6) {
            loan.setInterestRate(0.2);
        }

        // Calculamos el monto final sumando el interés al monto inicial
        double finalAmount = loan.getAmount() + (loan.getAmount() * loan.getInterestRate());
        loan.setTotalAmount(finalAmount);

        // Establecemos otros atributos predeterminados
        loan.setStatus(Status.PENDING);
        loan.setStartDate(LocalDateTime.now());

        // Guardamos el préstamo en la base de datos
        return loanRepository.save(loan);
    }

    @Override
    @Transactional
    public Loan update(Long id, Loan loan) {
        Loan loanFromDb = findById(id);

        // Actualizamos solo los campos relevantes
        loanFromDb.setStatus(loan.getStatus());

        // Guardamos los cambios en la base de datos
        return loanRepository.save(loanFromDb);
    }
}