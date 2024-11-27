package com.example.caso_prestamos.Web.Controller;

import com.example.caso_prestamos.Domain.Entity.PaymentSchedule;
import com.example.caso_prestamos.Service.PaymentScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/payments")
@RequiredArgsConstructor
public class PaymentScheduleController {

    private final PaymentScheduleService paymentScheduleService;

    // Marcar todas las cuotas como pagadas de un prestamo
    @PatchMapping("/loan/{loanId}/markAsPaid")
    public ResponseEntity<Void> markAllAsPaid(@PathVariable Long loanId) {
        paymentScheduleService.markAllAsPaid(loanId);
        return ResponseEntity.noContent().build();
    }
}
