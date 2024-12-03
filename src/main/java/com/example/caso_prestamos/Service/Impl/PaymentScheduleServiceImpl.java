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
import java.time.temporal.ChronoUnit;
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
    @Scheduled(cron = "0/30 * * * * *")  // Ejecuta a las 00:00 todos los días
    public void updatePaymentStatusAutomatically() {
        // Obtener todos los pagos no realizados cuyo plazo haya pasado
        List<PaymentSchedule> overduePayments = paymentScheduleRepository.findAllByStatusAndPaymentDateBefore(
                PaymentStatus.UNPAID, LocalDate.now());

        for (PaymentSchedule payment : overduePayments) {
            Loan loan = payment.getLoan();

            // Verificar si ya pasó un año desde la fecha de inicio del préstamo
            if (loan.getStartDate().plusYears(1).isBefore(LocalDate.now())) {
                // Detener la acumulación de interés y cambiar el estado del préstamo a "judicial-debt"
                loan.setStatus(LoanStatus.JUDICIAL_DEBT);
                loanRepository.save(loan);
                continue;
            }

            // Si el pago está vencido, actualizar su estado y calcular intereses
            if (payment.getPaymentDate().isBefore(LocalDate.now())) {
                payment.setStatus(PaymentStatus.LATE);

                // Calcular los días de retraso
                long daysLate = ChronoUnit.DAYS.between(payment.getPaymentDate(), LocalDate.now());

                // Calcular el nuevo monto con interés del 1% acumulado diariamente
                double originalAmount = payment.getAmount();
                double newAmount = originalAmount * Math.pow(1.01, daysLate); // Fórmula para interés compuesto diario
                payment.setAmount(newAmount);

                // Registrar la fecha desde cuando el pago está atrasado
                if (payment.getLateSince() == null) {
                    payment.setLateSince(payment.getPaymentDate());
                }

                paymentScheduleRepository.save(payment);

                // Verificar si algún pago del préstamo está atrasado y actualizar el estado del préstamo
                if (loan.getPaymentScheduleList().stream().anyMatch(ps -> ps.getStatus() == PaymentStatus.LATE)) {
                    loan.setStatus(LoanStatus.LATE);
                    loanRepository.save(loan);
                }
            }
        }
    }

}
