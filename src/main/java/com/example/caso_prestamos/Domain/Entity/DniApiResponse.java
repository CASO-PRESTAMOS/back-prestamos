package com.example.caso_prestamos.Domain.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DniApiResponse {
    private boolean success;
    private Data data;

    @Getter
    @Setter
    public static class Data {
        @JsonProperty("numero")
        private String dni;

        @JsonProperty("nombre_completo")
        private String fullName;

        @JsonProperty("nombres")
        private String firstNames;

        @JsonProperty("apellido_paterno")
        private String lastName;

        @JsonProperty("apellido_materno")
        private String lastMotherName;

    }

}
