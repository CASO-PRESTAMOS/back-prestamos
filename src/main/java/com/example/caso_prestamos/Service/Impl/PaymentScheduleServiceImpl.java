package com.example.caso_prestamos.Service.Impl;

import com.example.caso_prestamos.Domain.Entity.Loan;
import com.example.caso_prestamos.Domain.Entity.PaymentSchedule;
import com.example.caso_prestamos.Domain.Entity.Status;
import com.example.caso_prestamos.Repository.PaymentScheduleRepository;
import com.example.caso_prestamos.Repository.LoanRepository;
import com.example.caso_prestamos.Service.PaymentScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentScheduleServiceImpl implements PaymentScheduleService {

    @Autowired
    private PaymentScheduleRepository paymentScheduleRepository;

    @Autowired
    private LoanRepository loanRepository;

    // Metodo para generar el calendario de pagos para todos los préstamos
    @Override
    public void generatePaymentSchedulesForAllLoans() {
        List<Loan> loans = loanRepository.findAll();

        for (Loan loan : loans) {
            LocalDateTime startDate = loan.getStartDate();

            // Calcula el monto de la cuota mensual dividiendo el totalAmount por la duración
            Double installmentAmount = loan.getTotalAmount() / loan.getDuration();

            for (int month = 1; month <= loan.getDuration(); month++) {
                LocalDateTime paymentDueDate = startDate.plusMonths(month);

                // Verificar si ya existe un cronograma para la fecha y préstamo actual
                boolean scheduleExists = paymentScheduleRepository.existsByLoanAndPaymentDueDate(loan, paymentDueDate);

                // Solo crear el cronograma si no existe
                if (!scheduleExists) {
                    PaymentSchedule paymentSchedule = new PaymentSchedule();
                    paymentSchedule.setLoan(loan);
                    paymentSchedule.setPaymentDueDate(paymentDueDate);
                    paymentSchedule.setPaid(false);

                    // Asignar el monto de la cuota mensual
                    paymentSchedule.setInstallmentAmount(installmentAmount);

                    paymentScheduleRepository.save(paymentSchedule);
                }
            }
        }
    }

    // Metodo para obtener todos los calendarios de pago
    @Override
    public List<PaymentSchedule> getAllPaymentSchedules() {
        return paymentScheduleRepository.findAll();
    }

    // Metodo para marcar un pago como completado
    @Override
    public void markPaymentAsCompleted(Long paymentId) {
        // Busca el cronograma de pago por ID
        PaymentSchedule paymentSchedule = paymentScheduleRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment Schedule not found"));

        Loan loan = paymentSchedule.getLoan();

        // Obtén todos los pagos asociados al préstamo, ordenados por la fecha de vencimiento
        List<PaymentSchedule> allPayments = paymentScheduleRepository.findByLoan_LoanIdOrderByPaymentDueDateAsc(loan.getLoanId());

        // Encuentra el índice del cronograma actual
        int index = allPayments.indexOf(paymentSchedule);

        // Marca como completados todos los cronogramas hasta el seleccionado, incluidos los anteriores
        for (int i = 0; i <= index; i++) {
            PaymentSchedule currentSchedule = allPayments.get(i);
            if (!currentSchedule.isPaid()) {  // Solo marcar si aún no está pagado
                currentSchedule.setPaid(true);
                paymentScheduleRepository.save(currentSchedule);
            }
        }

        // Verifica si todos los pagos del préstamo están completados
        boolean allPaid = allPayments.stream().allMatch(PaymentSchedule::isPaid);

        // Si todos los pagos están completos, cambia el estado del préstamo
        if (allPaid) {
            loan.setStatus(Status.COMPLETE); // Cambia el estado del préstamo a COMPLETO
            loanRepository.save(loan);
        }
    }

    // Metodo para enviar alertas de pago
    @Scheduled(fixedRate = 86400000) // Ejecutar diariamente
    @Override
    public void sendPaymentAlerts() {
        LocalDateTime now = LocalDateTime.now();
        // Busca los pagos vencidos que no han sido pagados
        List<PaymentSchedule> duePayments = paymentScheduleRepository.findByPaymentDueDateBeforeAndPaidFalse(now.plusDays(1));

        for (PaymentSchedule payment : duePayments) {
            // Supongamos que el objeto Loan tiene un metodo getDebtorName() para obtener el nombre del deudor
            String debtorName = payment.getLoan().getClientName(); // Cambia esto según tu modelo de datos
            Long paymentScheduleId = payment.getId(); // Obtén el ID del cronograma de pagos

            System.out.println("Alerta: El pago con ID " + paymentScheduleId +
                    " con vencimiento el " + payment.getPaymentDueDate() +
                    " está próximo. Deudor: " + debtorName + ".");
        }
    }
}