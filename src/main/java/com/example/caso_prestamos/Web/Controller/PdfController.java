package com.example.caso_prestamos.Web.Controller;

import com.example.caso_prestamos.Domain.Entity.Loan;
import com.example.caso_prestamos.Domain.Entity.PaymentSchedule;
import com.example.caso_prestamos.Repository.LoanRepository;
import com.example.caso_prestamos.Repository.PaymentScheduleRepository;
import com.example.caso_prestamos.Service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    // Inyección de dependencias
    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private PdfService pdfService;
    @Autowired
    private PaymentScheduleRepository paymentScheduleRepository;

    @GetMapping("/loan/{id}")
    public ResponseEntity<byte[]> generateLoanReport(@PathVariable Long id) {
        // Intentamos obtener el Loan desde el repositorio
        Loan loan = loanRepository.findById(id).orElse(null);  // Usamos orElse para evitar NullPointerException

        if (loan == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // Si el préstamo no se encuentra
        }

        // Generamos el reporte PDF
        byte[] pdfBytes = pdfService.generateLoanReportPdf(loan);

        if (pdfBytes == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);  // Si hay un error generando el PDF
        }

        // Definir los encabezados para la respuesta
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=LoanReport.pdf");
        headers.add("Content-Type", "application/pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/loanBoleta/{loanId}/schedule/{paymentScheduleId}")
    public ResponseEntity<byte[]> generateBolet(@PathVariable Long loanId, @PathVariable Long paymentScheduleId) {
        PaymentSchedule paymentSchedule = paymentScheduleRepository.findById(paymentScheduleId).orElse(null);
        Loan loan = loanRepository.findById(loanId).orElse(null);

        if (loan == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // Si el préstamo no se encuentra
        }

        // Generamos el reporte PDF
        byte[] pdfBytes = pdfService.generateBoletagenerate(loan,paymentSchedule);

        if (pdfBytes == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);  // Si hay un error generando el PDF
        }

        // Definir los encabezados para la respuesta
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=LoanReport.pdf");
        headers.add("Content-Type", "application/pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    private Optional<Loan> findLoanById(Long loanId) {
        return Optional.empty();
    }
}


