package com.example.caso_prestamos.Service.Impl;

import com.example.caso_prestamos.Domain.Entity.DniApiResponse;
import com.example.caso_prestamos.Domain.Entity.User;
import com.example.caso_prestamos.Repository.UserRepository;
import com.example.caso_prestamos.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Override
    public User createUser(String dni) {
        // Llamar a la API para obtener los datos del DNI
        String url = "https://apiperu.dev/api/dni";
        String requestJson = "{\"dni\":\"" + dni + "\"}";

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("dni", dni);

        // Configurar headers y token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("Content-Type", "application/json");
        String apiToken = "08e86b5bf8e05483a6b4d9bf13f0844936f5cf68b4f0dde76e80449f59a6c324";
        headers.setBearerAuth(apiToken);

        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        // Hacer la solicitud a la API
        DniApiResponse response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, entity, DniApiResponse.class).getBody();

        if (response != null && response.isSuccess()) {
            DniApiResponse.Data data = response.getData();
            User user = new User();
            user.setDni(dni);
            user.setFirstNames(data.getFirstNames());  // Usamos 'firstNames' en lugar de 'fullName'
            user.setLastName(data.getLastName());
            user.setLastMotherName(data.getLastMotherName());
            return userRepository.save(user);  // Guardar usuario en la base de datos
        } else {
            throw new RuntimeException("No se pudo obtener la información del DNI");
        }
    }

    @Override
    public Optional<User> getUserByDni(String dni) {
        return userRepository.findById(dni);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user); // METODO NO USADO YA QUE NO LE VEO SENTIDO, PERO LO DEJO POR SI LO OCUPAMOS LUEGO
    }

    @Override
    public void deleteUser(String dni) {
        userRepository.deleteById(dni);
    }
}
