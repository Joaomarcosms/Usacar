package br.com.usacar.vendas.rest.controller;

import br.com.usacar.vendas.model.ModeloModel;
import br.com.usacar.vendas.rest.dto.ModeloDTO;
import br.com.usacar.vendas.service.ModeloService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/modelo")
public class ModeloController {

    //Injeção automatica de dependencia
    @Autowired
    private ModeloService modeloService;

    //Consultar dados por ID
    @GetMapping("/{id}")
    public ResponseEntity<ModeloDTO> obter(@PathVariable int id) {
        ModeloDTO modeloDTO = modeloService.obterPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(modeloDTO);
    }

    //Obtem todos os dados cadastrados
    @GetMapping
    public ResponseEntity<List<ModeloDTO>> obterTodos() {
        List<ModeloDTO> modeloDTOList = modeloService.obterTodos();
        return ResponseEntity.ok(modeloDTOList);
    }

    //Inserção de dados desejados
    @PostMapping
    public ResponseEntity<ModeloDTO> salvar(@RequestBody ModeloModel novoModelo) {
        ModeloDTO novoModeloDTO = modeloService.salvar(novoModelo);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoModeloDTO);
    }

    //Atualização de dados já existentes em banco
    @PutMapping
    public ResponseEntity<ModeloDTO> atualizar(@Valid @RequestBody ModeloModel modeloExistente) {
        ModeloDTO novoModeloDTO = modeloService.atualizar(modeloExistente);
        return ResponseEntity.status(HttpStatus.OK).body(novoModeloDTO);
    }

    //Deleta dados desejados do banco
    @DeleteMapping
    public ResponseEntity<String> deletar(@Valid @RequestBody ModeloModel modeloExistente) {
        modeloService.deletar(modeloExistente);
        return ResponseEntity.ok().body("Deletado com sucesso!!");
    }
}
