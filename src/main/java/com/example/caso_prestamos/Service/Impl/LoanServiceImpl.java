package com.example.caso_prestamos.Service.Impl;

import com.example.caso_prestamos.Domain.Entity.*;
import com.example.caso_prestamos.Repository.LoanRepository;
import com.example.caso_prestamos.Repository.PaymentScheduleRepository;
import com.example.caso_prestamos.Repository.UserRepository;
import com.example.caso_prestamos.Service.LoanService;
import com.example.caso_prestamos.Service.PaymentScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final PaymentScheduleService paymentScheduleService;
    private final PaymentScheduleRepository paymentScheduleRepository;

    @Override
    public Loan createLoan(String dni, Double amount, Integer months) {
        if (months != 2 && months != 6) {
            throw new IllegalArgumentException("La duración del préstamo solo puede ser de 2 o 6 meses.");
        }

        User user = userRepository.findById(dni)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con DNI: " + dni));

        Loan loan = new Loan();
        loan.setUser(user);
        loan.setAmount(amount);
        loan.setMonths(months);
        loan.setInterestRate(calculateInterestRate(months));
        loan.setStartDate(LocalDate.now());
        loan.setEndDate(LocalDate.now().plusMonths(months));
        loan.setStatus(LoanStatus.UNPAID);

        Loan savedLoan = loanRepository.save(loan);

        // Crear cronograma de pagos
        List<PaymentSchedule> paymentScheduleList = paymentScheduleService.generatePaymentSchedule(savedLoan);
        savedLoan.setPaymentScheduleList(paymentScheduleList);

        return loanRepository.save(savedLoan);
    }

    @Override
    public List<Loan> getLoansByUser(String dni) {
        User user = userRepository.findById(dni)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con DNI: " + dni));
        return loanRepository.findByUser(user);
    }

    @Override
    public void updatePaymentStatus(Long paymentId, String status) {
        // Validar estado
        PaymentStatus paymentStatus = PaymentStatus.valueOf(status.toUpperCase());


        PaymentSchedule payment = paymentScheduleRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + paymentId));

        payment.setStatus(paymentStatus);
        paymentScheduleRepository.save(payment);

        // Validar si todos los pagos están pagados
        Loan loan = payment.getLoan();
        boolean allPaid = loan.getPaymentScheduleList().stream()
                .allMatch(ps -> ps.getStatus() == PaymentStatus.PAID);

        if (allPaid) {
            loan.setStatus(LoanStatus.PAID);
            loanRepository.save(loan);
        }
    }

    @Override
    public List<PaymentSchedule> getPaymentScheduleByLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado con ID: " + loanId));
        return loan.getPaymentScheduleList();
    }

    private Double calculateInterestRate(Integer months) {
        // Ejemplo simple: 2% por 2 meses y 4% por 6 meses
        return months == 2 ? 0.02 : 0.04;
    }
}
