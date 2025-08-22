package br.com.usacar.vendas.model;

import br.com.usacar.vendas.rest.dto.ModeloDTO;
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
@Table(name = "Modelo")
public class ModeloModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome", length = 128, nullable = false)
    @NotNull(message = "O campo é obrigatório")
    @NotBlank(message = "O campo não pode está vazio")
    private String nome;

    @ManyToOne
    @JoinColumn(name = "marca_id", insertable = false, updatable = false)
    private MarcaModel marca;


    /*
    @Column(name = "marcaId")
    @NotNull(message = "O campo é obrigatório")
    private int marcaId;

     */

    //Conversão de Model para DTO
    public ModeloDTO toDTO() {
        ModeloDTO dto = new ModeloDTO();
        dto.setId(this.id);
        dto.setNome(this.nome);
        if(this.marca != null){
            // supondo que MarcaModel tenha também toDTO()
            dto.setMarca(this.marca.toDTO());
        }
        return dto;
    }

}
