package com.example.caso_prestamos.Repository;

import com.example.caso_prestamos.Domain.Entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository <Admin, Long> {
    Optional<Admin> findByUsername(String username);

}