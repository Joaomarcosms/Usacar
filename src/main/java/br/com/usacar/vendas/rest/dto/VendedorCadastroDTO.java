package br.com.usacar.vendas.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendedorCadastroDTO {
    private Integer id;

    @NotNull(message = "O campo nome é obrigatório")
    @NotBlank(message = "O campo nome não pode estar vazio")
    private String nome;

    @NotNull(message = "O campo CPF é obrigatório")
    @NotBlank(message = "O campo CPF não pode estar vazio")
    @CPF(message = "CPF inválido")
    private String cpf;

    @NotNull(message = "O campo telefone é obrigatório")
    @NotBlank(message = "O campo telefone não pode estar vazio")
    private String telefone;

    @NotNull(message = "O campo email é obrigatório")
    @NotBlank(message = "O campo email não pode estar vazio")
    @Email(message = "Email inválido")
    private String email;

    @NotNull(message = "O campo data de admissão é obrigatório")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataAdmissao;

    private LocalDate dataDemissao;

    @NotNull(message = "O campo senha é obrigatório")
    @NotBlank(message = "O campo senha não pode estar vazio")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}$",
            message = "A senha deve ter no mínimo 8 caracteres, pelo menos 1 número e 1 caractere especial.")
    private String senha;
}
