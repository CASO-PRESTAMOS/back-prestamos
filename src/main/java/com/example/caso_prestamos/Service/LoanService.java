package com.example.caso_prestamos.Service;

import com.example.caso_prestamos.Domain.Entity.Loan;
import com.example.caso_prestamos.Domain.Entity.PaymentSchedule;

import java.util.List;

public interface LoanService {

    Loan createLoan(String dni, Double amount, Integer months);
    List<Loan> getLoansByUser(String dni);
    void updatePaymentStatus(Long paymentId, String status);
    List<PaymentSchedule> getPaymentScheduleByLoan(Long loanId);
}