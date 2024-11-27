package com.example.caso_prestamos.Service.Impl;

import com.example.caso_prestamos.Domain.Entity.Loan;
import com.example.caso_prestamos.Domain.Entity.LoanStatus;
import com.example.caso_prestamos.Domain.Entity.PaymentSchedule;
import com.example.caso_prestamos.Domain.Entity.PaymentStatus;
import com.example.caso_prestamos.Repository.LoanRepository;
import com.example.caso_prestamos.Repository.PaymentScheduleRepository;
import com.example.caso_prestamos.Service.PaymentScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PaymentScheduleServiceImpl implements PaymentScheduleService {

    private final PaymentScheduleRepository paymentScheduleRepository;
    private final LoanRepository loanRepository;

    @Override
    public List<PaymentSchedule> generatePaymentSchedule(Loan loan) {
        List<PaymentSchedule> scheduleList = new ArrayList<>();
        Double monthlyPayment = loan.getAmount() / loan.getMonths();

        LocalDate paymentDate = loan.getStartDate().plusMonths(1);
        for (int i = 0; i < loan.getMonths(); i++) {
            PaymentSchedule payment = new PaymentSchedule();
            payment.setLoan(loan);
            payment.setPaymentDate(paymentDate);
            payment.setAmount(monthlyPayment);
            payment.setStatus(PaymentStatus.UNPAID);

            scheduleList.add(payment);
            paymentDate = paymentDate.plusMonths(1);
        }

        return paymentScheduleRepository.saveAll(scheduleList);
    }

    @Override
    public void markAllAsPaid(Long loanId) {
        List<PaymentSchedule> payments = paymentScheduleRepository.findByLoanId(loanId);

        // Marcar todos los pagos como PAID
        for (PaymentSchedule payment : payments) {
            payment.setStatus(PaymentStatus.PAID);
        }

        paymentScheduleRepository.saveAll(payments);

        // Obtener el préstamo relacionado
        Loan loan = payments.get(0).getLoan();

        // Verificar si todos los pagos están marcados como PAID
        boolean allPaid = payments.stream()
                .allMatch(payment -> payment.getStatus() == PaymentStatus.PAID);

        // Si todos los pagos están pagados, actualizar el estado del préstamo a PAID
        if (allPaid) {
            loan.setStatus(LoanStatus.PAID);
            loanRepository.save(loan);  // Guardar el préstamo actualizado
        }
    }
}
