package com.example.caso_prestamos.Service;

import com.example.caso_prestamos.Domain.Entity.Loan;
import com.example.caso_prestamos.Domain.Entity.PaymentSchedule;

import java.util.List;

public interface PaymentScheduleService {

    List<PaymentSchedule> generatePaymentSchedule(Loan loan);
    void markAllAsPaid(Long loanId);
}