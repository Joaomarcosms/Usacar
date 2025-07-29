package br.com.usacar.vendas.rest.controller;

import br.com.usacar.vendas.model.CarroModel;
import br.com.usacar.vendas.rest.dto.CarroDTO;
import br.com.usacar.vendas.rest.dto.CarroEstoqueDTO;
import br.com.usacar.vendas.rest.dto.CarroFiltroDTO;
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

    //Injeção automatica de dependencia
    @Autowired
    private CarroService carroService;

    //Consultar dados por ID
    @GetMapping("/{id}")
    public ResponseEntity<CarroDTO> obter(@PathVariable int id) {
        CarroDTO carroDTO = carroService.obterPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(carroDTO);
    }

    //Obtem todos os dados cadastrados
    @GetMapping
    public ResponseEntity<List<CarroDTO>> obterTodos() {
    List<CarroDTO> carroDTOList = carroService.obterTodos();
    return ResponseEntity.ok(carroDTOList);
    }

    @GetMapping("/estoque")
    public List<CarroEstoqueDTO> consultarEstoque(
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) String modelo,
            @RequestParam(required = false) Integer anoFabricacao,
            @RequestParam(required = false) String cor,
            @RequestParam(required = false) Integer kmMaxima,
            @RequestParam(required = false) String status
    ) {
        CarroFiltroDTO filtro = new CarroFiltroDTO();
        filtro.setMarca(marca);
        filtro.setModelo(modelo);
        filtro.setAnoFabricacao(anoFabricacao);
        filtro.setCor(cor);
        filtro.setKmMaxima(kmMaxima);
        filtro.setStatus(status);

        return carroService.consultarEstoque(filtro);
    }

    //Inserção de dados desejados
    @PostMapping
    public ResponseEntity<CarroDTO> salvar(@Valid  @RequestBody CarroModel novoCarro) {
        CarroDTO novoCarroDTO = carroService.salvar(novoCarro);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCarroDTO);
    }

    //Atualização de dados já existentes em banco
    @PutMapping
    public ResponseEntity<CarroDTO> atualizar (@Valid @RequestBody CarroModel carroExistente) {
        CarroDTO novoCarroDTO = carroService.atualizar(carroExistente);
        return ResponseEntity.status(HttpStatus.OK).body(novoCarroDTO);
    }

    //Deleta dados desejados do banco
    @DeleteMapping
    public ResponseEntity<String> deletar(@Valid @RequestBody CarroModel carroExistente) {
        carroService.deletar(carroExistente);
        return ResponseEntity.ok().body("Deletado com sucesso!!");
    }
}
