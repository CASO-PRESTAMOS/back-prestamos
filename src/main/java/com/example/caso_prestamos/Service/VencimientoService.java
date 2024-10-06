package com.example.caso_prestamos.Service;

import com.example.caso_prestamos.Domain.DTO.VencimientoDTO;

public interface VencimientoService {
    String obtenerEstadoVencimiento(VencimientoDTO vencimientoDTO);
    void notificarAdministrador(VencimientoDTO vencimientoDTO);
}
