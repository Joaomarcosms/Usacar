package br.com.usacar.vendas.rest.controller;

import br.com.usacar.vendas.exception.BusinessRuleException;
import br.com.usacar.vendas.exception.ObjectNotFoundException;
import br.com.usacar.vendas.rest.dto.VendedorCadastroDTO; // DTO de entrada
import br.com.usacar.vendas.rest.dto.VendedorResponseDTO; // DTO de saída
import br.com.usacar.vendas.service.VendedorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/vendedores")
public class VendedorController {

    @Autowired
    private VendedorService vendedorService;

    // obter vendedor por ID
    @GetMapping("/{id}")
    public ResponseEntity<VendedorResponseDTO> obter(@PathVariable int id) {
        VendedorResponseDTO vendedorDTO = vendedorService.obterPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(vendedorDTO);
    }

    // obter todos os vendedores
    @GetMapping
    public ResponseEntity<List<VendedorResponseDTO>> obterTodos() {
        List<VendedorResponseDTO> vendedorDTOList = vendedorService.obterTodos();
        return ResponseEntity.ok(vendedorDTOList);
    }

    //  cadastrar novo vendedor na base de dados
    @PostMapping
    public ResponseEntity<Object> salvar(@Valid @RequestBody VendedorCadastroDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HashMap<String, List<String>>() {{
                put("erros", errors);
            }});
        }
        try {
            Map<String, Object> response = vendedorService.salvar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (BusinessRuleException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HashMap<String, String>() {{
                put("mensagem", e.getMessage());
            }});
        }
    }

    // atualizar dados de vendedor existente na base de dados
    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizar(@PathVariable Integer id, @Valid @RequestBody VendedorCadastroDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HashMap<String, List<String>>() {{
                put("erros", errors);
            }});
        }
        try {
            VendedorResponseDTO vendedorAtualizado = vendedorService.atualizar(id, dto);
            return ResponseEntity.status(HttpStatus.OK).body(vendedorAtualizado);
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new HashMap<String, String>() {{
                put("mensagem", e.getMessage());
            }});
        } catch (BusinessRuleException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HashMap<String, String>() {{
                put("mensagem", e.getMessage());
            }});
        }
    }

    // deletar as informações do vendedor da base de dados
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable int id) {
        vendedorService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // Outro método
    @GetMapping("/outromedoto")
    public ResponseEntity<String>obterVendedor(@RequestParam String nome,
                                               @RequestParam(required = false )int id) {
        return ResponseEntity.ok().body("Nome: " + nome + " ID: " + id);
    }
}
