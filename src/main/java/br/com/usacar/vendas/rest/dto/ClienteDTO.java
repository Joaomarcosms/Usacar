package br.com.usacar.vendas.rest.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ClienteDTO {

    private int id;

    private String nome;

    private String cpf;

    private String telefone;

    private String email;

    private LocalDate dataCadastro;
}

