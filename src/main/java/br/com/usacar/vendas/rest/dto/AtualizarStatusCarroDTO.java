package br.com.usacar.vendas.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class AtualizarStatusCarroDTO {
    @NotBlank(message = "O novo status é obrigatório")
    private String novoStatus;
}
