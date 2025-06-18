package br.com.usacar.vendas.rest.controller;


import br.com.usacar.vendas.model.MarcaModel;
import br.com.usacar.vendas.rest.dto.MarcaDTO;
import br.com.usacar.vendas.service.MarcaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/marca")
public class MarcaController {

    //Injeção automatica de dependencia
    @Autowired
    private MarcaService marcaService;

    //Consultar dados por ID
    @GetMapping("/{id}")
    public ResponseEntity<MarcaDTO> obter(@PathVariable int id) {
        MarcaDTO marcaDTO = marcaService.obterPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(marcaDTO);
    }

    //Obtem todos os dados cadastrados
    @GetMapping
    public ResponseEntity<List<MarcaDTO>> obterTodas() {
        List<MarcaDTO> marcaDTOList = marcaService.obterTodas();
        return ResponseEntity.ok(marcaDTOList);
    }

    //Inserção de dados desejados
    @PostMapping
    public ResponseEntity<MarcaDTO> salvar(@RequestBody MarcaModel novaMarca) {
        MarcaDTO novaMarcaDTO = marcaService.salvar(novaMarca);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaMarcaDTO);
    }

    //Atualização de dados já existentes em banco
    @PutMapping
    public ResponseEntity<MarcaDTO> atualizar(@Valid @RequestBody MarcaModel marcaExistente) {
        MarcaDTO novaMarcaDTO = marcaService.atualizar(marcaExistente);
        return ResponseEntity.status(HttpStatus.OK).body(novaMarcaDTO);
    }

    //Deleta dados desejados do banco
    @DeleteMapping
    public ResponseEntity<String> deletar(@Valid @RequestBody MarcaModel marcaExistente) {
        marcaService.deletar(marcaExistente);
        return ResponseEntity.ok().body("Deletado com sucesso!!");
    }
}
