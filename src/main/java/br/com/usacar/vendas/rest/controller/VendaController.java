package br.com.usacar.vendas.rest.controller;

import br.com.usacar.vendas.model.VendaModel;
import br.com.usacar.vendas.repository.VendaRepository;
import br.com.usacar.vendas.rest.dto.*;
import br.com.usacar.vendas.service.CarroService;
import br.com.usacar.vendas.service.VendaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/venda")
public class VendaController {

    //Injeção automatica de dependencia
    @Autowired
    private VendaService vendaService;

    //Consultar dados por ID
    @GetMapping("/{id}")
    public ResponseEntity<VendaDTO> obter(@PathVariable int id) {
        VendaDTO vendaDTO = vendaService.obterPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(vendaDTO);
    }

    //Obtem todos os dados cadastrados
    @GetMapping
    public ResponseEntity<List<VendaDTO>> obterTodas() {
        List<VendaDTO> vendaDTOList = vendaService.obterTodas();
        return ResponseEntity.ok().body(vendaDTOList);
    }

    @GetMapping("/comissoes")
    public ResponseEntity<List<VendaRelatorioDTO>> gerarRelatorioComissoes(
            @RequestParam("dataInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(value = "dataFim", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {

        List<VendaRelatorioDTO> relatorio = vendaService.gerarRelatorioComissoes(dataInicio, dataFim);
        return ResponseEntity.ok(relatorio);
    }

    @GetMapping("/relatorios/marcas-mais-vendidas")
    public ResponseEntity<List<MarcaRankingDTO>> obterRankingMarcasMaisVendidas(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim
    ) {
        List<MarcaRankingDTO> ranking = vendaService.obterRankingMarcasMaisVendidas(dataInicio, dataFim);
        return ResponseEntity.ok(ranking);
    }


    //Inserção de dados desejados
    @PostMapping
    public ResponseEntity<VendaDTO> salvar(@Valid @RequestBody VendaDTO vendaDTO) {
        VendaModel venda = vendaService.converterParaEntidade(vendaDTO);
        VendaDTO vendaSalva = vendaService.salvar(venda);
        return ResponseEntity.status(HttpStatus.CREATED).body(vendaSalva);
    }

    @PostMapping("/comissoes/reajustar")
    public ResponseEntity<List<ComissaoReajusteResponseDTO>> reajustarComissao(@Valid @RequestBody ComissaoReajusteDTO dto) {
        List<ComissaoReajusteResponseDTO> resultados = vendaService.reajustarComissao(dto);
        return ResponseEntity.ok(resultados);
    }


    //Atualização de dados já existentes em banco
    @PutMapping
    public ResponseEntity<VendaDTO> atualizar(@Valid @RequestBody VendaModel vendaExistente) {
        VendaDTO novaVendaDTO = vendaService.atualizar(vendaExistente);
        return ResponseEntity.status(HttpStatus.OK).body(novaVendaDTO);
    }

    //Deleta dados desejados do banco
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable Integer id) {
        vendaService.deletar(id); // O service agora vai receber o ID diretamente
        return ResponseEntity.ok().body("Deletado com sucesso!!");
    }

}
