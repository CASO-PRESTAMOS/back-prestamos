package com.example.caso_prestamos.Service.Impl;

import com.example.caso_prestamos.Domain.Entity.Loan;
import com.example.caso_prestamos.Domain.Entity.Status;
import com.example.caso_prestamos.Repository.LoanRepository;
import com.example.caso_prestamos.Service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private LoanRepository loanRepository;
    @Transactional(readOnly = true)
    @Override
    public List<Loan> getAll(){
        return loanRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Loan findById(Long id){
        return loanRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Loan not found"));
    }

    @Transactional
    @Override
    public Loan create(Loan loan){

        if(loan.getDuration()==2){
            loan.setInterestRate(0.1);
        }
        if(loan.getDuration()==6){
            loan.setInterestRate(0.2);
        }
        double finalMount = loan.getAmount()+(loan.getAmount()*loan.getInterestRate());
        loan.setAmount(finalMount);
        loan.setStatus(Status.PENDING);
        loan.setStartDate(LocalDateTime.now());
        return loanRepository.save(loan);
    }

    @Transactional
    @Override
    public Loan update(Long id,Loan loan){
        Loan loanFromDb = findById(id);
        loanFromDb.setStatus(loan.getStatus());
        return loanRepository.save(loanFromDb);
    }
}
