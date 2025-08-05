package br.com.usacar.vendas.service;

import br.com.usacar.vendas.exception.BusinessRuleException;
import br.com.usacar.vendas.exception.ObjectNotFoundException;
import br.com.usacar.vendas.model.VendedorModel;
import br.com.usacar.vendas.repository.VendedorRepository;
import br.com.usacar.vendas.rest.dto.VendedorCadastroDTO;
import br.com.usacar.vendas.rest.dto.VendedorResponseDTO;
import org.modelmapper.ModelMapper;
import java.time.LocalDate;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

@Service
public class VendedorService {

    @Autowired
    private VendedorRepository vendedorRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Obtém os dados de um vendedor por ID.
     *
     * @param id O ID do vendedor a ser buscado.
     * @return O DTO de resposta do vendedor.
     * @throws ObjectNotFoundException Se o vendedor não for encontrado.
     */
    @Transactional(readOnly = true)
    public VendedorResponseDTO obterPorId(int id) {
        VendedorModel vendedor = vendedorRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Vendedor com ID " + id + " não encontrado."));
        return modelMapper.map(vendedor, VendedorResponseDTO.class);
    }

    /**
     * Obtém os dados de todos os vendedores no sistema.
     *
     * @return Uma lista de DTOs de resposta de todos os vendedores.
     */
    @Transactional(readOnly = true)
    public List<VendedorResponseDTO> obterTodos() {
        List<VendedorModel> vendedores = vendedorRepository.findAll();
        return vendedores.stream()
                .map(vendedor -> modelMapper.map(vendedor, VendedorResponseDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Salva os dados de um novo vendedor, aplicando validações e criptografando a senha.
     *
     * @param dto O DTO de cadastro do vendedor contendo os dados.
     * @return Um mapa contendo a mensagem de sucesso e o ID do vendedor cadastrado.
     * @throws BusinessRuleException Se houver violação de regra de negócio (CPF/email duplicado, data futura).
     */
    @Transactional
    public Map<String, Object> salvar(VendedorCadastroDTO dto) {
        // Validação de CPF único
        if (vendedorRepository.existsByCpf(dto.getCpf())) {
            throw new BusinessRuleException("CPF já cadastrado.");
        }

        // Validação de E-mail único
        if (vendedorRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessRuleException("E-mail já está em uso.");
        }

        // Validação de dataAdmissao (não pode ser futura)
        if (dto.getDataAdmissao().isAfter(LocalDate.now())) {
            throw new BusinessRuleException("A data de admissão não pode ser futura.");
        }

        // Criptografar a senha
        String senhaCriptografada = passwordEncoder.encode(dto.getSenha());


        VendedorModel novoVendedor = modelMapper.map(dto, VendedorModel.class);
        novoVendedor.setSenha(senhaCriptografada); // Define a senha criptografada no modelo

        VendedorModel vendedorSalvo = vendedorRepository.save(novoVendedor);

        // Retorna o Map de resposta customizado
        Map<String, Object> response = new HashMap<>();
        response.put("mensagem", "Vendedor cadastrado com sucesso.");
        response.put("vendedorId", vendedorSalvo.getId());
        return response;
    }

    /**
     * Atualiza os dados de um vendedor existente, aplicando validações e criptografando a senha se fornecida.
     *
     * @param id O ID do vendedor a ser atualizado.
     * @param dto O DTO de cadastro do vendedor contendo os dados atualizados.
     * @return O DTO de resposta do vendedor atualizado.
     * @throws ObjectNotFoundException Se o vendedor não for encontrado.
     * @throws BusinessRuleException Se houver violação de regra de negócio (CPF/email duplicado, data futura).
     */
    @Transactional
    public VendedorResponseDTO atualizar(Integer id, VendedorCadastroDTO dto) {
        VendedorModel vendedorExistente = vendedorRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Vendedor com ID " + id + " não encontrado para atualização."));

        // Validação de CPF único (se o CPF for alterado e já existir para outro vendedor)
        if (!vendedorExistente.getCpf().equals(dto.getCpf()) && vendedorRepository.existsByCpf(dto.getCpf())) {
            throw new BusinessRuleException("CPF já cadastrado para outro vendedor.");
        }

        // Validação de E-mail único (se o E-mail for alterado e já existir para outro vendedor)
        if (!vendedorExistente.getEmail().equals(dto.getEmail()) && vendedorRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessRuleException("E-mail já está em uso por outro vendedor.");
        }

        // Validação de dataAdmissao (não pode ser futura)
        if (dto.getDataAdmissao().isAfter(LocalDate.now())) {
            throw new BusinessRuleException("A data de admissão não pode ser futura.");
        }


        modelMapper.map(dto, vendedorExistente);

        // Se a senha for fornecida no DTO e não estiver vazia, criptografe e atualize
        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            vendedorExistente.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        VendedorModel vendedorAtualizado = vendedorRepository.save(vendedorExistente);
        return modelMapper.map(vendedorAtualizado, VendedorResponseDTO.class); // Retorna VendedorResponseDTO
    }

    /**
     * Deleta um vendedor da base de dados.
     *
     * @param id O ID do vendedor a ser deletado.
     * @throws ObjectNotFoundException Se o vendedor não for encontrado.
     * @throws BusinessRuleException Se houver alguma regra de negócio que impeça a exclusão.
     */
    @Transactional
    public void deletar(int id) {
        VendedorModel vendedor = vendedorRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Vendedor com ID " + id + " não encontrado para exclusão."));


        vendedorRepository.delete(vendedor);
    }
}
