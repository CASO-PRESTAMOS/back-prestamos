package com.example.caso_prestamos.Repository;

import com.example.caso_prestamos.Domain.Entity.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VencimientoRepository extends JpaRepository <Prestamo, Integer>{
}
