package com.example.caso_prestamos.Domain.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RucApiResponse {
    private boolean success;
    private Data data;

    @Getter
    @Setter
    public static class Data {
        @JsonProperty("ruc")
        private String ruc;

        @JsonProperty("nombre_o_razon_social")
        private String businessName;
    }
}
