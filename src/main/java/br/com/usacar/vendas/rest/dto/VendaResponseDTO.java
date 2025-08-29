package br.com.usacar.vendas.rest.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VendaResponseDTO {
    private Integer id;
    private LocalDate dataVenda;
    private double valorVenda;
    private double valorComissao;

    private CarroDTO carro;
    private ClienteDTO cliente;
    private VendedorResponseDTO vendedor;
}
