package br.com.usacar.vendas.model;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarroModel {
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
