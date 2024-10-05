package com.example.caso_prestamos.Service.Impl;

import com.example.caso_prestamos.Domain.Entity.Prestamo;
import com.example.caso_prestamos.Domain.Enum.Duracion;
import com.example.caso_prestamos.Repository.VencimientoRepository;
import com.example.caso_prestamos.Service.VencimientoService;
import com.example.caso_prestamos.Domain.DTO.VencimientoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VencimientoServiceImpl implements VencimientoService {

    private final VencimientoRepository vencimientoRepository;

    @Override
    public String obtenerEstadoVencimiento(VencimientoDTO vencimientoDTO) {
        LocalDateTime fechaActual = LocalDateTime.now();

        // Obtener el préstamo por su ID
        Optional<Prestamo> prestamoOptional = vencimientoRepository.findById(vencimientoDTO.getIdPrestamo());

        if (prestamoOptional.isPresent()) {
            Prestamo prestamo = prestamoOptional.get();

            // Obtener la fecha de vencimiento directamente
            LocalDateTime fechaVencimiento = prestamo.getFechaVencimiento();

            // Verificar si ya ha vencido comparando la fecha actual con la fecha de vencimiento
            if (fechaVencimiento.isBefore(fechaActual)) {
                return "El préstamo con ID: " + vencimientoDTO.getIdPrestamo() + " ha vencido. Fecha de vencimiento: " + fechaVencimiento;
            } else {
                return "El préstamo con ID: " + vencimientoDTO.getIdPrestamo() + " aún no ha vencido. Fecha de vencimiento: " + fechaVencimiento;
            }
        } else {
            return "No se encontró ningún préstamo con la ID: " + vencimientoDTO.getIdPrestamo();
        }
    }

    private LocalDateTime calcularFechaVencimiento(LocalDateTime fechaInicio, Duracion duracion) {
        if (duracion == Duracion.UN_MES) {
            return fechaInicio.plusMonths(1);
        } else if (duracion == Duracion.SEIS_MESES) {
            return fechaInicio.plusMonths(6);
        }
        return fechaInicio; // Caso por defecto
    }


    @Override
    public void notificarAdministrador(VencimientoDTO vencimientoDTO) {
        // Implementación de la notificación (correo electrónico, alerta en el panel, etc.)
        System.out.println("Notificación enviada para el préstamo ID: " + vencimientoDTO.getIdPrestamo());
    }
}


