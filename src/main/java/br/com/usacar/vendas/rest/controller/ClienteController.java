package br.com.usacar.vendas.rest.controller;


import br.com.usacar.vendas.model.ClienteModel;
import br.com.usacar.vendas.rest.dto.ClienteDTO;
import br.com.usacar.vendas.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cliente")
public class ClienteController {


    @Autowired
    private ClienteService clienteService;


    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> obter(@PathVariable int id) {
        ClienteDTO clienteDTO = clienteService.obterPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(clienteDTO);
    }

    @GetMapping()
    public ResponseEntity<List<ClienteDTO>> obterTodos() {
        List<ClienteDTO> clienteDTOList = clienteService.obterTodos();
        return ResponseEntity.ok(clienteDTOList);
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> salvar(@Valid @RequestBody ClienteModel novoCliente) {
        ClienteDTO novoClienteDTO = clienteService.salvar(novoCliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoClienteDTO);
    }

    @PutMapping
    public ResponseEntity<ClienteDTO> atualizar(@Valid @RequestBody ClienteModel clienteExistente) {
        ClienteDTO novoClienteDTO = clienteService.atualizar(clienteExistente);
        return ResponseEntity.status(HttpStatus.OK).body(novoClienteDTO);
    }

    @DeleteMapping
    public ResponseEntity<String> deletar(@Valid @RequestBody ClienteModel clienteExistente) {
        clienteService.deletar(clienteExistente);
        return ResponseEntity.ok().body("Deletado com sucesso!!");
    }
}
