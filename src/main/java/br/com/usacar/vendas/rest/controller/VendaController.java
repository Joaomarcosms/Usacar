package br.com.usacar.vendas.rest.controller;

import br.com.usacar.vendas.model.VendaModel;
import br.com.usacar.vendas.repository.VendaRepository;
import br.com.usacar.vendas.rest.dto.VendaDTO;
import br.com.usacar.vendas.service.CarroService;
import br.com.usacar.vendas.service.VendaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/venda")
public class VendaController {

    @Autowired
    private VendaService vendaService;

    @GetMapping("/{id}")
    public ResponseEntity<VendaDTO> obter(@PathVariable int id) {
        VendaDTO vendaDTO = vendaService.obterPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(vendaDTO);
    }

    @GetMapping
    public ResponseEntity<List<VendaDTO>> obterTodas() {
        List<VendaDTO> vendaDTOList = vendaService.obterTodas();
        return ResponseEntity.ok().body(vendaDTOList);
    }

    @PostMapping
    public ResponseEntity<VendaDTO> salvar(@Valid  @RequestBody VendaModel novaVenda) {
        VendaDTO novaVendaDTO = vendaService.salvar(novaVenda);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaVendaDTO);
    }

    @PutMapping
    public ResponseEntity<VendaDTO> atualizar(@Valid @RequestBody VendaModel vendaExistente) {
        VendaDTO novaVendaDTO = vendaService.atualizar(vendaExistente);
        return ResponseEntity.status(HttpStatus.OK).body(novaVendaDTO);
    }

    @DeleteMapping
    public ResponseEntity<String> deletar(@Valid @RequestBody VendaModel vendaExistente) {
        vendaService.deletar(vendaExistente);
        return ResponseEntity.ok().body("Deletado com sucesso!!");
    }

}
