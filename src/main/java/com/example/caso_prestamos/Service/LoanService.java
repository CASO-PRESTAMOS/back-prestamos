package com.example.caso_prestamos.Service;

import com.example.caso_prestamos.Domain.Entity.Loan;
import com.example.caso_prestamos.Domain.Entity.PaymentSchedule;

import java.util.List;

public interface LoanService {

    Loan createLoan(String identifier, Double amount, Integer months);
    List<Loan> getLoansByUser(String identifier);
    void updatePaymentStatus(Long paymentId);
    List<PaymentSchedule> getPaymentScheduleByLoan(Long loanId);
    boolean doesPaymentExist(Long paymentId);
    boolean doesLoanExist(Long loanId);
}