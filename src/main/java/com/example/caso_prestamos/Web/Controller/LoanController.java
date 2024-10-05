package com.example.caso_prestamos.Web.Controller;


import com.example.caso_prestamos.Domain.Entity.Loan;
import com.example.caso_prestamos.Service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/loan")
@PreAuthorize("hasRole('ADMIN')")
public class LoanController {
    private final LoanService loanService;

    @GetMapping("/all")
    public ResponseEntity<List<Loan>> getAll(){
        List<Loan> loans = loanService.getAll();
        return new ResponseEntity<List<Loan>>(loans, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Loan> getById(@PathVariable("id") Long id){
        Loan loan = loanService.findById(id);
        return new ResponseEntity<Loan>(loan, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Loan> create(@RequestBody @Valid Loan loan){
        Loan newLoan = loanService.create(loan);
        return new ResponseEntity<>(newLoan,HttpStatus.CREATED);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<Loan> update(@PathVariable("id") Long id,
                                       @RequestBody Loan loan){
        Loan updatedLoan = loanService.update(id, loan);
        return new ResponseEntity<Loan>(updatedLoan,HttpStatus.CREATED);
    }
}
