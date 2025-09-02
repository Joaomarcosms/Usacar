package br.com.usacar.vendas.rest.controller;

import br.com.usacar.vendas.model.ClienteModel;
import br.com.usacar.vendas.rest.dto.ClienteDTO;
import br.com.usacar.vendas.rest.dto.VendaHistoricoDTO;
import br.com.usacar.vendas.service.ClienteService;
import br.com.usacar.vendas.service.VendaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    //Injeção automatica de dependencia
    @Autowired
    private ClienteService clienteService;

    @Autowired
    private VendaService vendaService;

    //Consultar dados por ID
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> obter(@PathVariable int id) {
        ClienteDTO clienteDTO = clienteService.obterPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(clienteDTO);
    }

    //Obtem todos os dados cadastrados
    @GetMapping()
    public ResponseEntity<List<ClienteDTO>> obterTodos() {
        List<ClienteDTO> clienteDTOList = clienteService.obterTodos();
        return ResponseEntity.ok(clienteDTOList);
    }

    //Para poder consultar o cliente por cpf, integração com o frontEnd
    //Precisa melhorar, foi para teste
    @GetMapping("/obterporcpf/{cpf}")
    public ResponseEntity<ClienteDTO> obterPorCpf(@PathVariable String cpf) {
        ClienteDTO clienteDTO = clienteService.obterPorCpf(cpf);
        return ResponseEntity.ok(clienteDTO);
    }

    @GetMapping("/cpf/{cpf}/historico-vendas")
    public ResponseEntity<List<VendaHistoricoDTO>> obterHistoricoVendasPorCpf(@PathVariable String cpf) {
        List<VendaHistoricoDTO> historico = vendaService.obterHistoricoVendasPorCpf(cpf);
        return ResponseEntity.ok(historico);
    }


    @GetMapping("/{id}/historico-vendas")
    public ResponseEntity<List<VendaHistoricoDTO>> obterHistoricoVendas(@PathVariable Integer id) {
        List<VendaHistoricoDTO> historico = vendaService.obterHistoricoVendasPorCliente(id);
        return ResponseEntity.ok(historico);
    }


    //Inserção de dados desejados
    @PostMapping
    public ResponseEntity<ClienteDTO> salvar(@Valid @RequestBody ClienteModel novoCliente) {
        ClienteDTO novoClienteDTO = clienteService.salvar(novoCliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoClienteDTO);
    }

    //Atualização de dados já existentes em banco
    @PutMapping
    public ResponseEntity<ClienteDTO> atualizar(@Valid @RequestBody ClienteModel clienteExistente) {
        ClienteDTO novoClienteDTO = clienteService.atualizar(clienteExistente);
        return ResponseEntity.status(HttpStatus.OK).body(novoClienteDTO);
    }

    //Deleta dados desejados do banco
    @DeleteMapping
    public ResponseEntity<String> deletar(@Valid @RequestBody ClienteModel clienteExistente) {
        clienteService.deletar(clienteExistente);
        return ResponseEntity.ok().body("Deletado com sucesso!!");
    }

    //Para poder deletar o cliente por cpf, integração com o frontEnd
    //Precisa melhorar, foi para teste
    @DeleteMapping("/deletar/{cpf}")
    public ResponseEntity<String> deletarPorCpf(@PathVariable String cpf) {
        clienteService.deletarPorCpf(cpf);
        return ResponseEntity.ok("Cliente deletado com sucesso!");
    }
}
