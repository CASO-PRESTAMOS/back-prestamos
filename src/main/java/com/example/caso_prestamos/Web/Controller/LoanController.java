package com.example.caso_prestamos.Web.Controller;


import com.example.caso_prestamos.Domain.Entity.Loan;
import com.example.caso_prestamos.Service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("admin/loan")
public class LoanController {
    private final LoanService loanService;

    @GetMapping
    public ResponseEntity<List<Loan>> getAll(){
        List<Loan> loans = loanService.getAll();
        return new ResponseEntity<List<Loan>>(loans, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Loan> getById(@PathVariable("id") Long id){
        Loan loan = loanService.findById(id);
        return new ResponseEntity<Loan>(loan, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Loan> create(@RequestBody Loan loan){
        Loan newLoan = loanService.create(loan);
        return new ResponseEntity<Loan>(newLoan,HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Loan> update(@PathVariable("id") Long id,
                                       @RequestBody Loan loan){
        Loan updatedLoan = loanService.update(id, loan);
        return new ResponseEntity<Loan>(updatedLoan,HttpStatus.CREATED);
    }
}
