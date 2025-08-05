package br.com.usacar.vendas.model;

import br.com.usacar.vendas.rest.dto.StatusCarroDTO;
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
@Table(name = "StatusCarro")
public class StatusCarroModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "descricao", length = 128, nullable = false)
    @NotNull(message = "O campo é obrigatório")
    @NotBlank(message = "O campo não pode está vazio")
    private String descricao;

    /*
    @ManyToOne
    @JoinColumn(name = "status_id")
    private StatusCarroModel status;

     */


    //Conversão de Model para DTO
    public StatusCarroDTO toDTO(){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this,StatusCarroDTO.class);
    }
}
