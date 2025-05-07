package br.com.usacar.vendas.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModeloModel {
    private int id;
    private String nome;
    private int marcaId;
}
