package br.com.usacar.vendas.model;

import br.com.usacar.vendas.rest.dto.ClienteDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "Cliente")
public class ClienteModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome",length = 128, nullable = false)
    @NotNull(message = "O nome tem que ser obrigatório")
    @NotBlank(message = "Não poderá está vazio")
    private String nome;

    @Column(name = "cpf", length = 11, nullable = false, unique = true)
    @CPF
    @Length(min = 11, max = 11, message = "O cpf deve conter 11 caracteres")
    @NotNull(message = "O campo tem que ser preenchido obrigatório.")
    @NotBlank(message = "O campo não poderá ficar vazio")
    private String cpf;


    @Length(min = 11, max = 11, message = "O telefone deve conter 11 digitos")
    @Column(name = "telefone", nullable = false)
    @NotNull(message = "O campo tem que ser preenchido obrigatório.")
    @NotBlank(message = "O campo não poderá ficar vazio")
    private String telefone;

    @Column(name = "email",length = 255, nullable = false, unique = true)
    @Email(message = "O E-mail informado precisa ser válido")
    @NotNull(message = "O campo tem que ser preenchido obrigatóriamente.")
    @NotBlank(message = "O campo não poderá ficar vazio")
    private String email;

    @Column(name = "dataCadastro", nullable = false)
    private LocalDate dataCadastro;

    //Conversão de Model para DTO
    public ClienteDTO toDTO() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, ClienteDTO.class);
    }
}
