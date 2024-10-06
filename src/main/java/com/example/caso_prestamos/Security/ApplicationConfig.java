package com.example.caso_prestamos.Security;

import com.example.caso_prestamos.Service.Auth.AdminDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final AdminDetailsService adminDetailsService;


}
