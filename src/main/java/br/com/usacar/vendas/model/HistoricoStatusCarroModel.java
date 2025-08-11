package br.com.usacar.vendas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "historico_status_carro")
public class HistoricoStatusCarroModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "carro_id", nullable = false)
    private CarroModel carro;

    @Column(name = "status_anterior", nullable = false)
    private String statusAnterior;

    @Column(name = "novo_status", nullable = false)
    private String novoStatus;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Column(name = "usuario_responsavel", nullable = false)
    private String usuarioResponsavel;
}
