package br.com.usacar.vendas.rest.controller;

import br.com.usacar.vendas.rest.dto.RelatorioVendasDTO;
import br.com.usacar.vendas.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/vendas")
    public ResponseEntity<List<RelatorioVendasDTO>> getRelatorioVendas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {

        List<RelatorioVendasDTO> relatorio = relatorioService.gerarRelatorioVendasPorPeriodo(dataInicio, dataFim);
        return ResponseEntity.ok(relatorio);
    }
}
