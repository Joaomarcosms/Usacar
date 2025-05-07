package br.com.usacar.vendas.model;



import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private LocalDate anoFabricacao;

    @Column(name = "anoModelo", nullable = false)
    @NotNull(message = "O campo é obrigatório")
    private LocalDate anoModelo;

    @Column(name = "placa", nullable = false)
    @NotNull(message = "O campo é obrigatório")
    @NotBlank(message = "O campo não pode está vazio")
    private String placa;

    @Column(name = "quilometragem",length = 128, nullable = false)
    @NotNull(message = "O campo é obrigatório")
    @NotBlank(message = "O campo não pode está vazio")
    private  String quilometragem;

    @Column(name = "precoCompra", nullable = false)
    @NotNull(message = "O campo é obrigatório")
    private double precoCompra;

    @Column(name = "dataCadastro", nullable = false)
    @NotNull(message = "O campo é obrigatório")
    private LocalDate dataCadastro;

    @Column(name = "modeloId", nullable = false)
    @NotNull(message = "O campo é obrigatório")
    private int modeloId;

    @Column(name = "corId", nullable = false)
    @NotNull(message = "O campo é obrigatório")
    private int corId;

    @Column(name = "statusId", nullable = false)
    @NotNull(message = "O campo é obrigatório")
    private int statusId;
}
