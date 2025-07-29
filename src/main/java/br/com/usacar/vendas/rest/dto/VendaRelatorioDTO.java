package br.com.usacar.vendas.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VendaRelatorioDTO {
        private String vendedor;
        private long quantidadeVendas;
        private double valorTotalVendido;
        private double valorTotalComissao;
}
