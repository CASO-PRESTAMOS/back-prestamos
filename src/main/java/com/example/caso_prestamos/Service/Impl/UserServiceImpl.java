package com.example.caso_prestamos.Service.Impl;

import com.example.caso_prestamos.Service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{
        private final RestTemplate restTemplate;
        private final ObjectMapper objectMapper;

        private static final String API_URL = "https://apiperu.dev/api/dni/";
        private static final String API_TOKEN = "7cc72efcd2bdc94384a2fb75acf40c4122b39625c5ef10fb3b4293956aaa02c6";

    @Override
    public boolean validateDni(String dni) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + API_TOKEN);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                API_URL + dni,
                HttpMethod.GET,
                entity,
                String.class
        );

        JsonNode root = objectMapper.readTree(response.getBody());
        return root.path("success").asBoolean();
    }
}
