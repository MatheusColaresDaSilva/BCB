package com.bcb.bcb.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDTO<T> {

    private T content;
    private List<ErroResponseDTO> erros;

    public ResponseDTO(T data) {
        this.content = data;
    }

    public static ResponseDTO<Object> withError(List<ErroResponseDTO> erros){
        ResponseDTO<Object> responseDTO = new ResponseDTO<>();

        responseDTO.setErros(erros);

        return responseDTO;
    }

    public static ResponseDTO<Object> withErro(ErroResponseDTO erro){
        return withError(Arrays.asList(erro));
    }

}
