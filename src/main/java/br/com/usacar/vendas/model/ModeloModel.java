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
    @JoinColumn(name = "marcaId", insertable = false, updatable = false)
    private MarcaModel marca;

    /*
    @Column(name = "marcaId")
    @NotNull(message = "O campo é obrigatório")
    private int marcaId;

     */

    //Conversão de Model para DTO
    public ModeloDTO toDTO() {
        ModelMapper modelMapper = new ModelMapper();
        // Mapeamento explícito para a marca aninhada, se necessário
        modelMapper.typeMap(ModeloModel.class, ModeloDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getMarca() != null ? src.getMarca().toDTO() : null, ModeloDTO::setMarca);
        });
        return modelMapper.map(this, ModeloDTO.class);
    }
}
