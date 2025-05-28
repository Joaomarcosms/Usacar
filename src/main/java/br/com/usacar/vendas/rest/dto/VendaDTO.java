package br.com.usacar.vendas.rest.dto;

import java.time.LocalDate;

public class VendaDTO {

    private int id;

    private LocalDate dataVenda;

    private double valorVenda;

    private double valorComissao;

    private int carroId;

    private int clienteId;

    private int vendedorId;
}
