package br.com.usacar.vendas.rest.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendaCancelamentoResponseDTO {
    private String mensagem;
    private Integer carroId;
    private String statusRestaurado;
}
