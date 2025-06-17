package br.com.usacar.vendas.rest.controller;

import br.com.usacar.vendas.model.CarroModel;
import br.com.usacar.vendas.rest.dto.CarroDTO;
import br.com.usacar.vendas.service.CarroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carro")
public class CarroController {
    @Autowired
    private CarroService carroService;

    @GetMapping("/{id}")
    public ResponseEntity<CarroDTO> obter(@PathVariable int id) {
        CarroDTO carroDTO = carroService.obterPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(carroDTO);
    }

    @GetMapping
    public ResponseEntity<List<CarroDTO>> obterTodos() {
    List<CarroDTO> carroDTOList = carroService.obterTodos();
    return ResponseEntity.ok(carroDTOList);
    }


    @PostMapping
    public ResponseEntity<CarroDTO> salvar(@Valid  @RequestBody CarroModel novoCarro) {
        CarroDTO novoCarroDTO = carroService.salvar(novoCarro);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCarroDTO);
    }

    @PutMapping
    public ResponseEntity<CarroDTO> atualizar (@Valid @RequestBody CarroModel carroExistente) {
        CarroDTO novoCarroDTO = carroService.atualizar(carroExistente);
        return ResponseEntity.status(HttpStatus.OK).body(novoCarroDTO);
    }

    @DeleteMapping
    public ResponseEntity<String> deletar(@Valid @RequestBody CarroModel carroExistente) {
        carroService.deletar(carroExistente);
        return ResponseEntity.ok().body("Deletado com sucesso!!");
    }
}
