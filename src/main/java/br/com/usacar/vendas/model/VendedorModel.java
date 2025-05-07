package br.com.usacar.vendas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "Vendedor")
public class VendedorModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nome", nullable = false)
    @NotNull(message = "O campo é obrigatório")
    @NotBlank(message = "O campo não pode está vazio")
    private String nome;

    @Column(name = "cpf", nullable = false, unique = true)
    @CPF
    @Length(min = 11, max = 11, message = "O CPF deve conter 11 caracteres")
    @NotNull(message = "O campo é obrigatório")
    @NotBlank(message = "O campo não pode está vazio")
    private String cpf;

    @Column(name = "telefone", nullable = false)
    @Length(min = 10, max = 11)
    @NotNull(message = "O campo deve ser obrigatório")
    @NotBlank(message = "O campo não pode ser vazio")
    private String telefone;

    @Column(name = "email", nullable = false)
    @Email
    @Length(message = "O E-mail informado deve ser válido")
    @NotNull(message = "O campo deve ser obrigatório")
    @NotBlank(message = "O campo não pode ser vazio")
    private String email;

    @Column(name = "dataAdmissao", nullable = false)
    @NotNull(message = "O campo é obrigatório")
    private LocalDate dataAdmissao;

    @Column(name = "dataDemissao", nullable = false)
    private LocalDate dataDemissao;

    @Column(name = "senha", nullable = false)
    @NotNull(message = "O campo é obrigatório")
    @NotBlank(message = "O campo não pode ser vazio")
    private String senha;
}
