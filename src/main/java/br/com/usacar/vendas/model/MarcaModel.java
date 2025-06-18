package br.com.usacar.vendas.model;

import br.com.usacar.vendas.rest.dto.MarcaDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "Marca")
public class MarcaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nome", length = 128, nullable = false)
    @NotNull(message = "O campo é obrigatório")
    @NotBlank(message = "O campo não pode está vazio")
    private String nome;

    //Conversão de Model para DTO
    public MarcaDTO toDTO(){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, MarcaDTO.class);
    }
}
