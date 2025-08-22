package br.com.usacar.vendas.rest.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class RelatorioVendasDTO {
    private VendedorRelatorioDTO vendedor;
    private Integer quantidadeVendas;
    private BigDecimal valorTotalVendido;
    private List<VendaDetalheDTO> vendas;
}
