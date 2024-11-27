package com.example.caso_prestamos.Web.Controller;

import com.example.caso_prestamos.Domain.Entity.Loan;
import com.example.caso_prestamos.Domain.Entity.PaymentSchedule;
import com.example.caso_prestamos.Service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/loans")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class LoanController {

    private final LoanService loanService;

    // Crear un préstamo para un usuario
    @PostMapping("/create")
    public ResponseEntity<Loan> createLoan(
            @RequestParam String dni,
            @RequestParam Double amount,
            @RequestParam Integer months) {
        Loan loan = loanService.createLoan(dni, amount, months);
        return ResponseEntity.ok(loan);
    }

    // Obtener todos los préstamos de un usuario
    @GetMapping("/user/{dni}")
    public ResponseEntity<List<Loan>> getLoansByUser(@PathVariable String dni) {
        List<Loan> loans = loanService.getLoansByUser(dni);
        return ResponseEntity.ok(loans);
    }

    // Actualizar el estado de un pago en el cronograma de pagos
    @PatchMapping("/payment/{paymentId}")
    public ResponseEntity<Void> updatePaymentStatus(
            @PathVariable Long paymentId,
            @RequestParam String status) {
        loanService.updatePaymentStatus(paymentId, status);
        return ResponseEntity.noContent().build();
    }

    // Obtener el cronograma de pagos de un préstamo
    @GetMapping("/{loanId}/schedule")
    public ResponseEntity<List<PaymentSchedule>> getPaymentScheduleByLoan(@PathVariable Long loanId) {
        List<PaymentSchedule> schedule = loanService.getPaymentScheduleByLoan(loanId);
        return ResponseEntity.ok(schedule);
    }
}
