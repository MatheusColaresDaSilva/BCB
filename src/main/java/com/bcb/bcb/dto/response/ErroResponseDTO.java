package com.bcb.bcb.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErroResponseDTO {

    private String campo;
    private String mensagem;

    public ErroResponseDTO (String mensagem) {
        this.mensagem = mensagem;
    }

}