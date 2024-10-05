package com.example.caso_prestamos;

import com.example.caso_prestamos.Domain.Entity.Admin;
import com.example.caso_prestamos.Domain.Enum.Role;
import com.example.caso_prestamos.Repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class CasoPrestamosApplication implements CommandLineRunner {
	private final AdminRepository adminRepository;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public CasoPrestamosApplication(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
		this.adminRepository = adminRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public static void main(String[] args) {
		SpringApplication.run(CasoPrestamosApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (adminRepository.count() == 0) {
			Admin admin = new Admin();
			admin.setUsername("admin");
			admin.setPassword(passwordEncoder.encode("admin123"));
			admin.setRole(Role.ADMIN);
			adminRepository.save(admin);
			System.out.println("Administrador pre-registrado con éxito con rol ADMIN.");
		}
	}
}
