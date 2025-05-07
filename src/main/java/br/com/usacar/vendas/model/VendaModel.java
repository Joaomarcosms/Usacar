package br.com.usacar.vendas.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendaModel {
    private int id;
    private LocalDate dataVenda;
    private double valorVenda;
    private double valorComissao;
    private int carroId;
    private int clienteId;
    private int vendedorId;

}
