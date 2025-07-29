package br.com.usacar.vendas.rest.dto;

import lombok.Data;

@Data
public class CarroFiltroDTO {
    private String marca;
    private String modelo;
    private Integer anoFabricacao;
    private String cor;
    private Integer kmMaxima;
    private String status;
}
