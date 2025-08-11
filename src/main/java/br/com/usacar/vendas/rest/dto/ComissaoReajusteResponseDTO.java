package br.com.usacar.vendas.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComissaoReajusteResponseDTO {
    private String vendedor;
    private Double valorTotalVendas;
    private Double comissaoAnterior;
    private Double comissaoReajustada;
}
