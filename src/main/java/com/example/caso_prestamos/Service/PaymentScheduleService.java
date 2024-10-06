package com.example.caso_prestamos.Service;

import com.example.caso_prestamos.Domain.Entity.PaymentSchedule;
import com.example.caso_prestamos.Domain.Entity.Loan;

import java.util.List;

public interface PaymentScheduleService {

    void generatePaymentSchedulesForAllLoans();

    List<PaymentSchedule> getAllPaymentSchedules();

    void markPaymentAsCompleted(Long paymentId);

    void sendPaymentAlerts();
}