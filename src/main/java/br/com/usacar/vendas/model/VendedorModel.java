package br.com.usacar.vendas.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendedorModel {
    private int id;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private LocalDate dataAdmissao;
    private LocalDate dataDemissao;
    private String senha;
}
