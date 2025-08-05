package br.com.usacar.vendas.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class VendaHistoricoDTO {
    private LocalDate dataVenda;
    private Double valorVenda;
    private Double valorComissao;
    private CarroDTO carro;
    private String vendedor;

    @Data
    @AllArgsConstructor
    public static class CarroDTO {
        private String marca;
        private String modelo;
        private Integer anoFabricacao;
        private Integer anoModelo;
        private String placa;
        private String cor;
    }
}
