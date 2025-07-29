package br.com.usacar.vendas.model;

import br.com.usacar.vendas.rest.dto.VendaDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jdk.jshell.Snippet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Builder
@Table(name = "Venda")
public class VendaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "dataVenda", nullable = false)
    @NotNull(message = "O campo é obrigatório")
    private LocalDate dataVenda;

    @Column(name = "valorVenda", nullable = false)
    @NotNull(message = "O campo é obrigatório")
    private double valorVenda;

    @Column(name = "valorComissao", nullable = false)
    @NotNull(message = "O campo é obrigatório")
    private double valorComissao;
    /*
    @Column(name = "carroId", nullable = false)
    @NotNull(message = "O campo é obrigatório")
    private int carroId;

    @Column(name = "clienteId", nullable = false)
    @NotNull(message = "O campo é obrigatório")
    private int clienteId;

    @Column(name = "vendedorId", nullable = false)
    @NotNull(message = "O campo é obrigatório")
    private int vendedorId;

     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carroId", nullable = false)
    private CarroModel carro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clienteId", nullable = false)
    private ClienteModel cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendedorId", nullable = false)
    private VendedorModel vendedor;



    //Conversão de Model para DTO
    public VendaDTO toDTO(){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, VendaDTO.class);
    }

}
