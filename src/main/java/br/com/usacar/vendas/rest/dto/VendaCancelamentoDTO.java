package br.com.usacar.vendas.rest.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendaCancelamentoDTO {
    private Integer vendaId;
    private String motivo;
}
