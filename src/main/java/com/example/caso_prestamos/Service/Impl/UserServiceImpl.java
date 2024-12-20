package com.example.caso_prestamos.Service.Impl;

import com.example.caso_prestamos.Domain.Entity.DniApiResponse;
import com.example.caso_prestamos.Domain.Entity.RucApiResponse;
import com.example.caso_prestamos.Domain.Entity.User;
import com.example.caso_prestamos.Repository.UserRepository;
import com.example.caso_prestamos.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Override
    public User createUser(String identifier) {
        if (identifier.length() == 8) {
            return createUserFromDni(identifier);
        } else if (identifier.length() == 11) {
            return createUserFromRuc(identifier);
        } else {
            throw new IllegalArgumentException("El identificador debe ser un DNI (8 dígitos) o RUC (11 dígitos).");
        }
    }

    private User createUserFromDni(String dni) {
        String url = "https://apiperu.dev/api/dni";
        HttpHeaders headers = createHeaders();
        String requestJson = "{\"dni\":\"" + dni + "\"}";

        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);
        DniApiResponse response = restTemplate.exchange(url, HttpMethod.POST, entity, DniApiResponse.class).getBody();

        if (response != null && response.isSuccess()) {
            DniApiResponse.Data data = response.getData();
            User user = new User();
            user.setIdentifier(dni);
            user.setFullName(data.getLastName() + " " + data.getLastMotherName() + " " + data.getFirstNames());
            return userRepository.save(user);
        } else {
            throw new RuntimeException("No se pudo obtener la información del DNI");
        }
    }

    private User createUserFromRuc(String ruc) {
        String url = "https://apiperu.dev/api/ruc";
        HttpHeaders headers = createHeaders();
        String requestJson = "{\"ruc\":\"" + ruc + "\"}";

        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);
        RucApiResponse response = restTemplate.exchange(url, HttpMethod.POST, entity, RucApiResponse.class).getBody();

        if (response != null && response.isSuccess()) {
            RucApiResponse.Data data = response.getData();
            User user = new User();
            user.setIdentifier(ruc);
            user.setFullName(data.getBusinessName());
            return userRepository.save(user);
        } else {
            throw new RuntimeException("No se pudo obtener la información del RUC");
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("Content-Type", "application/json");
        headers.setBearerAuth("1cca345836605bcf5c7e59e83f725680cd491912e1a8dcdbd2a3cdb08f3d4cfc");
        return headers;
    }

    @Override
    public Optional<User> getUserByIdentifier(String identifier) {
        return userRepository.findById(identifier);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
