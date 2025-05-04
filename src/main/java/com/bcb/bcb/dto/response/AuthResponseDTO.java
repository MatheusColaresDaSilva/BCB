package com.bcb.bcb.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {

    @Schema(description = "Token de autenticação (documento invertido)", example = "10987654321")
    private String token;

}
