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
            @RequestParam String identifier,
            @RequestParam Double amount,
            @RequestParam Integer months) {
        try {
            if (identifier == null || identifier.isBlank()) {
                throw new IllegalArgumentException("El identificador no puede estar vacío.");
            }
            if (amount <= 0) {
                throw new IllegalArgumentException("El monto debe ser mayor a cero.");
            }
            if (amount > 5000) {
                throw new IllegalArgumentException("El monto debe ser menor a cinco mil.");
            }
            if (months <= 0) {
                throw new IllegalArgumentException("La duración en meses debe ser mayor a cero.");
            }
            Loan loan = loanService.createLoan(identifier, amount, months);
            return ResponseEntity.ok(loan);
        } catch (IllegalArgumentException e) {
            throw e; // Será manejado por CustomExceptionHandler
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el préstamo: " + e.getMessage(), e);
        }
    }

    // Obtener todos los préstamos de un usuario
    @GetMapping("/user/{identifier}")
    public ResponseEntity<List<Loan>> getLoansByUser(@PathVariable String identifier) {
        try {
            if (identifier == null || identifier.isBlank()) {
                throw new IllegalArgumentException("El identificador no puede estar vacío.");
            }
            List<Loan> loans = loanService.getLoansByUser(identifier);
            if (loans.isEmpty()) {
                throw new IllegalArgumentException("No se encontraron préstamos para el usuario con identificador " + identifier);
            }
            return ResponseEntity.ok(loans);
        } catch (IllegalArgumentException e) {
            throw e; // Será manejado por CustomExceptionHandler
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener los préstamos del usuario: " + e.getMessage(), e);
        }
    }

    @GetMapping("/loan/{id}")
    public ResponseEntity<Loan> getLoanById(@PathVariable Long id) {
        Loan loan = loanService.getLoan(id);
        return ResponseEntity.ok(loan);
    }

    // Actualizar el estado de un pago en el cronograma de pagos, solo a PAID
    @PatchMapping("/payment/{paymentId}/markPaid")
    public ResponseEntity<Void> updatePaymentStatus(@PathVariable Long paymentId) {
        try {
            if (paymentId == null || paymentId <= 0) {
                throw new IllegalArgumentException("El ID del pago no puede ser nulo o menor o igual a cero.");
            }

            // Validar si el pago existe
            boolean paymentExists = loanService.doesPaymentExist(paymentId);
            if (!paymentExists) {
                throw new IllegalArgumentException("No se encontró un pago con el ID: " + paymentId);
            }

            loanService.updatePaymentStatus(paymentId);
            return ResponseEntity.noContent().build(); // Respuesta sin contenido, indicando éxito
        } catch (IllegalArgumentException e) {
            throw e; // Será manejado por CustomExceptionHandler
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el estado del pago: " + e.getMessage(), e);
        }
    }

    // Obtener el cronograma de pagos de un préstamo
    @GetMapping("/{loanId}/schedule")
    public ResponseEntity<List<PaymentSchedule>> getPaymentScheduleByLoan(@PathVariable Long loanId) {
        try {
            if (loanId == null) {
                throw new IllegalArgumentException("El ID del préstamo no puede ser nulo.");
            }
            List<PaymentSchedule> schedule = loanService.getPaymentScheduleByLoan(loanId);
            if (schedule.isEmpty()) {
                throw new IllegalArgumentException("No se encontraron pagos para el préstamo con ID " + loanId);
            }
            return ResponseEntity.ok(schedule);
        } catch (IllegalArgumentException e) {
            throw e; // Será manejado por CustomExceptionHandler
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener el cronograma de pagos del préstamo: " + e.getMessage(), e);
        }
    }


}
