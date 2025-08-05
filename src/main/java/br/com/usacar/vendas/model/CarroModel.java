package br.com.usacar.vendas.model;



import br.com.usacar.vendas.rest.dto.CarroDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "Carro")
public class CarroModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "anoFabricacao", nullable = false)
    @NotNull(message = "O campo é obrigatório")
    private int anoFabricacao;


    @Column(name = "anoModelo", nullable = false)
    @NotNull(message = "O campo é obrigatório")
    private int anoModelo;

    @Column(name = "placa", nullable = false)
    @NotNull(message = "O campo é obrigatório")
    @NotBlank(message = "O campo não pode está vazio")
    private String placa;

    @Column(name = "quilometragem",length = 128, nullable = false)
    @NotNull(message = "O campo é obrigatório")
    private int quilometragem;

    @Column(name = "precoCompra", nullable = false)
    @NotNull(message = "O campo é obrigatório")
    private double precoCompra;

    @JsonFormat(pattern = "yyy-MM-dd")
    @Column(name = "dataCadastro", nullable = false)
    @NotNull(message = "O campo é obrigatório")
    private LocalDate dataCadastro;

    /*
    @Column(name = "modeloId", nullable = false)
    @NotNull(message = "O campo é obrigatório")
    private int modeloId;

    @Column(name = "corId", nullable = false)
    @NotNull(message = "O campo é obrigatório")
    private int corId;

    @Column(name = "statusId", nullable = false)
    @NotNull(message = "O campo é obrigatório")
    private int statusId;

     */


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modeloId", insertable = false, updatable = false)
    private ModeloModel  modelo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "corId", insertable = false, updatable = false)
    private CorModel cor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statusId", insertable = false, updatable = false)
    private StatusCarroModel status;




    //Conversão do Model para DTO
    public CarroDTO toDTO() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, CarroDTO.class);
    }


    public void setStatus(StatusCarroModel status) {
        this.status = status;
    }
}