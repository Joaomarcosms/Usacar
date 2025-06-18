package br.com.usacar.vendas.rest.controller;

import br.com.usacar.vendas.model.CorModel;
import br.com.usacar.vendas.rest.dto.CorDTO;
import br.com.usacar.vendas.service.CorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cor")
public class CorController {

    //Injeção automatica de dependencia
    @Autowired
    private CorService corService;

    //Consultar dados por ID
    @GetMapping("/{id}")
    public ResponseEntity<CorDTO> obter(@PathVariable int id) {
        CorDTO corDTO = corService.obterPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(corDTO);
    }

    //Obtem todos os dados cadastrados
    @GetMapping
    public ResponseEntity<List<CorDTO>> obterTodas() {
        List<CorDTO> corDTOList = corService.obterTodas();
        return ResponseEntity.ok().body(corDTOList);
    }

    //Inserção de dados desejados
    @PostMapping
    public ResponseEntity<CorDTO> salvar(@RequestBody CorModel novaCor) {
        CorDTO novaCorDTO = corService.salvar(novaCor);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaCorDTO);
    }

    //Atualização de dados já existentes em banco
    @PutMapping
    public ResponseEntity<CorDTO> atualizar(@Valid @RequestBody CorModel corExistente) {
        CorDTO novaCorDTO = corService.atualizar(corExistente);
        return ResponseEntity.status(HttpStatus.OK).body(novaCorDTO);
    }

    //Deleta dados desejados do banco
    @DeleteMapping
    public ResponseEntity<String> deletar(@Valid @RequestBody CorModel corExistente) {
        corService.deletar(corExistente);
        return ResponseEntity.ok().body("Deletado com sucesso!!");
    }
}
