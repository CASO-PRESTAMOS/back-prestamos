package com.example.caso_prestamos.Web.Controller;

import com.example.caso_prestamos.Service.PaymentScheduleService;
import com.example.caso_prestamos.Service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/payments")
@RequiredArgsConstructor
public class PaymentScheduleController {

    private final PaymentScheduleService paymentScheduleService;
    private final LoanService loanService; // Aseguramos que el servicio de Loan está disponible para verificar el préstamo

    // Marcar todas las cuotas como pagadas de un préstamo
    @PatchMapping("/loan/{loanId}/markAsPaid")
    public ResponseEntity<Void> markAllAsPaid(@PathVariable Long loanId) {
        try {
            if (loanId == null || loanId <= 0) {
                throw new IllegalArgumentException("El ID del préstamo no puede ser nulo o menor o igual a cero.");
            }

            // Validar si el préstamo existe
            boolean loanExists = loanService.doesLoanExist(loanId);  // Verifica si el préstamo existe
            if (!loanExists) {
                throw new IllegalArgumentException("No se encontró un préstamo con el ID: " + loanId);
            }

            paymentScheduleService.markAllAsPaid(loanId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            throw e; // Será manejado por CustomExceptionHandler
        } catch (Exception e) {
            throw new RuntimeException("Error al marcar todas las cuotas como pagadas: " + e.getMessage(), e);
        }
    }
}
