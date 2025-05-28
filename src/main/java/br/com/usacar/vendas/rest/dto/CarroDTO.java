package br.com.usacar.vendas.rest.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CarroDTO {
    private int id;

    private LocalDate anoFabricacao;

    private LocalDate anoModelo;

    private String placa;

    private  String quilometragem;

    private double precoCompra;

    private LocalDate dataCadastro;

    private int modeloId;

    private int corId;

    private int statusId;
}
