package br.com.usacar.vendas.service;

import br.com.usacar.vendas.exception.BusinessRuleException;
import br.com.usacar.vendas.exception.ObjectNotFoundException;
import br.com.usacar.vendas.model.PerfilModel;
import br.com.usacar.vendas.model.UsuarioModel;
import br.com.usacar.vendas.repository.PerfilRepository;
import br.com.usacar.vendas.repository.UsuarioRepository;
import br.com.usacar.vendas.rest.dto.LoginRequestDTO;
import br.com.usacar.vendas.rest.dto.UsuarioRequestDTO;
import br.com.usacar.vendas.rest.dto.UsuarioResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public UsuarioResponseDTO salvar(UsuarioRequestDTO dto) {
        if (usuarioRepository.existsByCpf(dto.getCpf())) {
            throw new BusinessRuleException("Já existe um usuário cadastrado com este CPF.");
        }

        PerfilModel perfil = perfilRepository.findByNomeIgnoreCase(dto.getPerfil())
                .orElseThrow(() -> new ObjectNotFoundException("Perfil '" + dto.getPerfil() + "' não encontrado."));


        String senhaCriptografada = passwordEncoder.encode(dto.getSenha());

        // Cria a entidade UsuarioModel
        UsuarioModel novoUsuario = new UsuarioModel();
        novoUsuario.setNome(dto.getNome());
        novoUsuario.setCpf(dto.getCpf());
        novoUsuario.setEmail(dto.getEmail());
        novoUsuario.setSenha(senhaCriptografada);
        novoUsuario.setPerfil(perfil);

        // Salva no banco de dados
        UsuarioModel usuarioSalvo = usuarioRepository.save(novoUsuario);

        // Regra 5: Retorna o DTO de resposta, sem a senha
        UsuarioResponseDTO responseDTO = modelMapper.map(usuarioSalvo, UsuarioResponseDTO.class);
        responseDTO.setPerfil(usuarioSalvo.getPerfil().getNome());

        return responseDTO;
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO autenticar(LoginRequestDTO dto) {
        // 1. Encontrar o usuário pelo e-mail
        UsuarioModel usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BusinessRuleException("E-mail ou senha inválidos."));

        // 2. Verificar se a senha fornecida corresponde à senha criptografada no banco
        if (!passwordEncoder.matches(dto.getSenha(), usuario.getSenha())) {
            throw new BusinessRuleException("E-mail ou senha inválidos.");
        }

        // 3. Se a senha estiver correta, mapeia a entidade para o DTO de resposta
        UsuarioResponseDTO responseDTO = modelMapper.map(usuario, UsuarioResponseDTO.class);
        responseDTO.setPerfil(usuario.getPerfil().getNome()); // Define o nome do perfil

        return responseDTO;
    }
}
