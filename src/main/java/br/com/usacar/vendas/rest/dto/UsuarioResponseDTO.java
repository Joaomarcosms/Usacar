package br.com.usacar.vendas.rest.dto;

import lombok.Data;

@Data
public class UsuarioResponseDTO {
    private Integer id;
    private String nome;
    private String cpf;
    private String email;
    private String perfil;
}
