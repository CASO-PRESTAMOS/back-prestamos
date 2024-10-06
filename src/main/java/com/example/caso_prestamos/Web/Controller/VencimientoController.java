package com.example.caso_prestamos.Web.Controller;

import com.example.caso_prestamos.Service.VencimientoService;
import com.example.caso_prestamos.Domain.DTO.VencimientoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vencimientos")
@RequiredArgsConstructor
public class VencimientoController {

    private final VencimientoService vencimientoService;

    @PostMapping("/estado")
    public ResponseEntity<String> obtenerEstadoVencimiento(@RequestBody VencimientoDTO vencimientoDTO) {
        String estado = vencimientoService.obtenerEstadoVencimiento(vencimientoDTO);
        return ResponseEntity.ok(estado);
    }

    @PostMapping("/notificar")
    public ResponseEntity<Void> notificarAdministrador(@RequestBody VencimientoDTO vencimientoDTO) {
        vencimientoService.notificarAdministrador(vencimientoDTO);
        return ResponseEntity.ok().build();
    }
}