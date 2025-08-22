package br.com.usacar.vendas.rest.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class VendaDetalheDTO {
    private Integer id;
    private LocalDate data;
    private String cliente;
    private String veiculo;
    private BigDecimal valor;


}
