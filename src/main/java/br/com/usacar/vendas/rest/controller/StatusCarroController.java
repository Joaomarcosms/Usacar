package br.com.usacar.vendas.rest.controller;

import br.com.usacar.vendas.model.StatusCarroModel;
import br.com.usacar.vendas.rest.dto.StatusCarroDTO;
import br.com.usacar.vendas.service.StatusCarroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/status")
public class StatusCarroController {

    //Injeção automatica de dependencia
    @Autowired
    private StatusCarroService statusCarroService;

    //Consultar dados por ID
    @GetMapping("/{id}")
    public ResponseEntity<StatusCarroDTO> obter(@PathVariable int id) {
        StatusCarroDTO statusCarroDTO = statusCarroService.obterPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(statusCarroDTO);
    }

    //Obtem todos os dados cadastrados
    @GetMapping
    public ResponseEntity<List<StatusCarroDTO>> obterTodos() {
        List<StatusCarroDTO> statusCarroDTOList = statusCarroService.obterTodos();
        return ResponseEntity.ok(statusCarroDTOList);
    }

    //Inserção de dados desejados
    @PostMapping
    public ResponseEntity<StatusCarroDTO> salvar(@RequestBody StatusCarroModel novoStatusCarro) {
        StatusCarroDTO novoStatusCarroDTO = statusCarroService.salvar(novoStatusCarro);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoStatusCarroDTO);
    }

    //Atualização de dados já existentes em banco
    @PutMapping
    public ResponseEntity<StatusCarroDTO> atualizar(@Valid @RequestBody StatusCarroModel StatusCarroExistente) {
        StatusCarroDTO novoStatusCarro = statusCarroService.atualizar(StatusCarroExistente);
        return ResponseEntity.status(HttpStatus.OK).body(novoStatusCarro);
    }

    //Deleta dados desejados do banco
    @DeleteMapping
    public ResponseEntity<String>deletar (@Valid @RequestBody StatusCarroModel StatusCarroExistente) {
        statusCarroService.deletar(StatusCarroExistente );
        return ResponseEntity.ok().body("Deletado com sucesso!!");


    }
}
