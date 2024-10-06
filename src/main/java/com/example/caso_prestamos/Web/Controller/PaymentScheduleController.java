package com.example.caso_prestamos.Web.Controller;

import com.example.caso_prestamos.Domain.Entity.PaymentSchedule;
import com.example.caso_prestamos.Service.PaymentScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment-schedule")
public class PaymentScheduleController {

    @Autowired
    private PaymentScheduleService paymentScheduleService;

    @GetMapping("/generate/")
    public List<PaymentSchedule> getAllPaymentSchedules() {
        // Generar cronogramas de pago antes de obtener todos
        paymentScheduleService.generatePaymentSchedulesForAllLoans();

        // Obtener todos los cronogramas
        return paymentScheduleService.getAllPaymentSchedules();
    }

    @PostMapping("/mark-completed/{paymentId}")
    public void markPaymentAsCompleted(@PathVariable Long paymentId) {
        paymentScheduleService.markPaymentAsCompleted(paymentId);
    }
}