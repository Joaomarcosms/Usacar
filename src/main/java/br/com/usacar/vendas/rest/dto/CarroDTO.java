package br.com.usacar.vendas.rest.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CarroDTO {
    private int id;

    private int anoFabricacao;

    private int anoModelo;

    private String placa;

    private  String quilometragem;

    private double precoCompra;

    private LocalDate dataCadastro;

    private int modeloId;

    private int corId;

    private int statusId;

    // DTOs aninhados
    private ModeloDTO modelo;
    private CorDTO cor;
    private StatusCarroDTO status;
}
