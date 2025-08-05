package br.com.usacar.vendas.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarcaRankingDTO {
    private String marca;
    private Long quantidadeVendida;
    private Double valorTotalVendido;
    private Double ticketMedio;


    public MarcaRankingDTO(String marca, Long quantidadeVendida, Double valorTotalVendido) {
        this.marca = marca;
        this.quantidadeVendida = quantidadeVendida;
        this.valorTotalVendido = valorTotalVendido;
        this.ticketMedio = (quantidadeVendida != null && quantidadeVendida > 0) ? (valorTotalVendido / quantidadeVendida) : 0.0;
    }
}
