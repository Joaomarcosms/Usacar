package br.com.usacar.vendas.rest.controller;

import br.com.usacar.vendas.rest.dto.LoginRequestDTO;
import br.com.usacar.vendas.rest.dto.UsuarioRequestDTO;
import br.com.usacar.vendas.rest.dto.UsuarioResponseDTO;
import br.com.usacar.vendas.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> salvar(@Valid @RequestBody UsuarioRequestDTO dto) {
        UsuarioResponseDTO responseDTO = usuarioService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        UsuarioResponseDTO usuarioAutenticado = usuarioService.autenticar(dto);
        return ResponseEntity.ok(usuarioAutenticado);
    }
}
