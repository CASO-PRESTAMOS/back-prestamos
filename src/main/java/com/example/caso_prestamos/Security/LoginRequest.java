package com.example.caso_prestamos.Security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class  LoginRequest {
    private String username;
    private String password;
}