package br.com.usacar.vendas.model;

import br.com.usacar.vendas.rest.dto.VendaDTO;
import jakarta.persistence.*;
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
@Table(name = "Venda")
public class VendaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "dataVenda", nullable = false)
    @NotNull(message = "O campo é obrigatório")
    private LocalDate dataVenda;

    @Column(name = "valorVenda", nullable = false)
    @NotNull(message = "O campo é obrigatório")
    private double valorVenda;

    @Column(name = "valorComissao", nullable = false)
    @NotNull(message = "O campo é obrigatório")
    private double valorComissao;

    @Column(name = "carroId", nullable = false)
    @NotNull(message = "O campo é obrigatório")
    private int carroId;

    @Column(name = "clienteId", nullable = false)
    @NotNull(message = "O campo é obrigatório")
    private int clienteId;

    @Column(name = "vendedorId", nullable = false)
    @NotNull(message = "O campo é obrigatório")
    private int vendedorId;

    public VendaDTO toDTO(){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, VendaDTO.class);
    }

}
