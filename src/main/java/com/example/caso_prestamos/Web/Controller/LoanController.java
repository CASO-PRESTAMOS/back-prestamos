package com.example.caso_prestamos.Web.Controller;

import com.example.caso_prestamos.Domain.Entity.Loan;
import com.example.caso_prestamos.Service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/loan")
@PreAuthorize("hasRole('ADMIN')")
public class LoanController {

    private final LoanService loanService;

    @GetMapping("/all")
    public ResponseEntity<List<Loan>> getAll() {
        List<Loan> loans = loanService.getAll();
        return new ResponseEntity<>(loans, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Loan> createLoan(@RequestBody Loan loan) {
        Loan newLoan = loanService.create(loan);
        return new ResponseEntity<>(newLoan, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Loan> updateLoan(@PathVariable("id") Long id, @RequestBody Loan loan) {
        Loan updatedLoan = loanService.update(id, loan);
        return new ResponseEntity<>(updatedLoan, HttpStatus.OK);
    }

    @GetMapping("/select/{id}")
    public ResponseEntity<Loan> getLoanById(@PathVariable("id") Long id) {
        try {
            Loan loan = loanService.findById(id);
            return new ResponseEntity<>(loan, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
