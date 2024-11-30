package com.example.caso_prestamos.Service.Impl;

import com.example.caso_prestamos.Domain.Entity.Loan;
import com.example.caso_prestamos.Domain.Entity.LoanStatus;
import com.example.caso_prestamos.Domain.Entity.PaymentSchedule;
import com.example.caso_prestamos.Domain.Entity.PaymentStatus;
import com.example.caso_prestamos.Repository.LoanRepository;
import com.example.caso_prestamos.Repository.PaymentScheduleRepository;
import com.example.caso_prestamos.Service.PaymentScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        Double principal = loan.getAmount(); // Capital inicial
        Double rate = loan.getInterestRate(); // Tasa de interés
        Integer months = loan.getMonths(); // Periodos en meses
        Integer n = 1; // Número de veces que se aplica el interés por periodo (mensual en este caso)
        Double totalAmount = principal * Math.pow(1 + rate / n, n * months); // Fórmula de interés compuesto
        Double monthlyPayment = totalAmount / months; // Dividimos en pagos mensuales

        LocalDate paymentDate = loan.getStartDate().plusDays(30);
        for (int i = 0; i < months; i++) {
            PaymentSchedule payment = new PaymentSchedule();
            payment.setLoan(loan);
            payment.setPaymentDate(paymentDate);
            payment.setAmount(monthlyPayment); // Monto de la cuota
            payment.setStatus(PaymentStatus.UNPAID);

            scheduleList.add(payment);
            paymentDate = paymentDate.plusDays(30);
        }

        return paymentScheduleRepository.saveAll(scheduleList);
    }

    // Este metodo se ejecutara automaticamente cada dia a la medianoche
    @Transactional
    @Scheduled(cron = "*/10 * * * * ?")  // Ejecuta a las 00:00 todos los días
    public void updatePaymentStatusAutomatically() {
        // Obtener todos los pagos no pagados cuyo plazo haya pasado
        List<PaymentSchedule> overduePayments = paymentScheduleRepository.findAllByStatusAndPaymentDateBefore(PaymentStatus.UNPAID, LocalDate.now());

        for (PaymentSchedule payment : overduePayments) {
            Loan loan = payment.getLoan();

            // Si ya pasó un año desde la fecha de inicio del préstamo
            if (loan.getStartDate().plusYears(1).isBefore(LocalDate.now())) {
                // Detener la acumulación de interés y cambiar el estado del préstamo a "judicial-debt"
                loan.setStatus(LoanStatus.JUDICIAL_DEBT);
                loanRepository.save(loan);
                continue;
            }

            // Si la fecha de pago ya pasó y el pago no está realizado, actualizar el estado a LATE
            if (payment.getPaymentDate().isBefore(LocalDate.now())) {
                payment.setStatus(PaymentStatus.LATE);

                // Calcular el interés adicional
                double additionalInterest = loan.getAmount() * 0.01; // 1% del monto original
                payment.setAmount(payment.getAmount() + additionalInterest);

                paymentScheduleRepository.save(payment);

                // Actualizar el estado del préstamo a LATE si tiene pagos atrasados
                if (loan.getPaymentScheduleList().stream().anyMatch(ps -> ps.getStatus() == PaymentStatus.LATE)) {
                    loan.setStatus(LoanStatus.LATE);
                    loanRepository.save(loan);
                }
            }
        }
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
