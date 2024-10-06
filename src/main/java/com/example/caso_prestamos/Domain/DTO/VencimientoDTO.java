package com.example.caso_prestamos.Domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor

public class VencimientoDTO {
    private int idPrestamo;
    private String DNI;
}
