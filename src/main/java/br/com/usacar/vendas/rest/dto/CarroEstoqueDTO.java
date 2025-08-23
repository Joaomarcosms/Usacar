package br.com.usacar.vendas.rest.dto;

import br.com.usacar.vendas.model.CarroModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarroEstoqueDTO {
    private Integer id;
    private String marca;
    private String modelo;
    private Integer anoFabricacao;
    private Integer anoModelo;
    private String placa;
    private Integer quilometragem;
    private Double precoCompra;
    private String cor;
    private LocalDate dataCadastro;
    private String status;

    // Construtor usado na sua query JPQL
    public CarroEstoqueDTO(
            Integer id,
            String marca,
            String modelo,
            Integer anoFabricacao,
            Integer anoModelo,
            String placa,
            Integer quilometragem,
            Double precoCompra,
            String cor,
            String status
    ) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.anoFabricacao = anoFabricacao;
        this.anoModelo = anoModelo;
        this.placa = placa;
        this.quilometragem = quilometragem;
        this.precoCompra = precoCompra;
        this.cor = cor;
        this.status = status;
        this.dataCadastro = null;
        this.id = null;
    }
}
