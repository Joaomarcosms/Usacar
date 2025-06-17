package br.com.usacar.vendas.rest.controller;

import br.com.usacar.vendas.model.VendedorModel;
import br.com.usacar.vendas.rest.dto.VendedorDTO;
import br.com.usacar.vendas.service.VendedorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendedor")
public class VendedorController {

    @Autowired
    private VendedorService vendedorService;


    //Aqui esta obtendo o vendedor por id
    @GetMapping("/{id}")
    public ResponseEntity<VendedorDTO> obter(@PathVariable int id) {
        VendedorDTO vendedorDTO = vendedorService.obterPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(vendedorDTO);
    }

    //Trás todos os dados de vendedores cadastrados em sistema
    @GetMapping
    public ResponseEntity<List<VendedorDTO>> obterTodos() {
        List<VendedorDTO> vendedorDTOList = vendedorService.obterTodos();
        return ResponseEntity.ok(vendedorDTOList);
    }

    //Cadastrar novo vendedor na base de dados
    @PostMapping
    public ResponseEntity<VendedorDTO> salvar(@Valid @RequestBody VendedorModel novoVendedor) {
        VendedorDTO novoVendedorDTO = vendedorService.salvar(novoVendedor);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoVendedorDTO);
    }

    //Atualizar dados de vendedor existente na base de dados
    @PutMapping
    public ResponseEntity<VendedorDTO> atualizar(@Valid @RequestBody VendedorModel vendedorExistente) {
        VendedorDTO novoVendedorDTO = vendedorService.atualizar(vendedorExistente);
        return ResponseEntity.status(HttpStatus.OK).body(novoVendedorDTO);
    }

    //Deleta as informações do vendedor da base de dados
    @DeleteMapping()
    public ResponseEntity<String> deletar(@Valid @RequestBody VendedorModel vendedorExistente) {
        vendedorService.deletar(vendedorExistente);
        return ResponseEntity.ok().body("Deletado com sucesso!!");
    }
}
