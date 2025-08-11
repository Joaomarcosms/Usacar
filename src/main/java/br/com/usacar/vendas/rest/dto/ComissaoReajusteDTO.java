package br.com.usacar.vendas.rest.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class ComissaoReajusteDTO {
    @NotNull(message = "A data de início é obrigatória")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataInicio;

    @NotNull(message = "A data de fim é obrigatória")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataFim;

    @NotNull(message = "O valor mínimo total de vendas é obrigatório")
    @DecimalMin(value = "0.0", message = "O valor mínimo deve ser não negativo")
    private Double valorMinimoTotalVendas;

    @NotNull(message = "O percentual de reajuste é obrigatório")
    @DecimalMin(value = "0.0", message = "O percentual de reajuste deve ser não negativo")
    private Double percentualReajuste;

    private boolean simulacao;
}
