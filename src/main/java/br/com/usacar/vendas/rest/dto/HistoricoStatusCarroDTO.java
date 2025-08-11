package br.com.usacar.vendas.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoricoStatusCarroDTO {
    private LocalDateTime dataHora;
    private String statusAnterior;
    private String novoStatus;
    private String usuarioResponsavel;
}
