package br.com.usacar.vendas.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModeloDTO {
    private int id;
    private String nome;
    private int marcaid;



}
