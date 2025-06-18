package br.com.usacar.vendas.service;

import br.com.usacar.vendas.exception.*;
import br.com.usacar.vendas.model.VendedorModel;
import br.com.usacar.vendas.repository.VendedorRepository;
import br.com.usacar.vendas.rest.dto.VendedorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendedorService {

    @Autowired
    private VendedorRepository vendedorRepository;

    //Obtem os dados de um vendedor por id
    @Transactional(readOnly = true)
    public VendedorDTO obterPorId(int id) {
        VendedorModel vendedor = vendedorRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Vendedor com " + id + " não encontrado"));
        return vendedor.toDTO();
    }

    //Obtem os dados de todos os vendedores em sistema.
    @Transactional(readOnly = true)
    public List<VendedorDTO> obterTodos() {
        List<VendedorModel> vendedor = vendedorRepository.findAll();
        return vendedor.stream().map(v -> v.toDTO()).collect(Collectors.toList());
    }

    /*
     *Salva os dados de um vendedor.
     * Com o tratamento das devidadas exceptions
     */

    @Transactional
    public VendedorDTO salvar(VendedorModel novoVendedor) {
        try {
            //Caso ocorra uma tentativa de salvar um novo vendedor com um cpf já existente
            if (vendedorRepository.existsByCpf(novoVendedor.getCpf())) {
                throw new ConstraintException("Já existe um vendedor com esse CPF " + novoVendedor.getCpf());
            }
            //Salvar o novo vendedor na base de dados
            return vendedorRepository.save(novoVendedor).toDTO();

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro!! Não foi possível salvar o vendedor " + novoVendedor.getCpf());
        } catch (ConstraintException e) {

            // Relança a mensagem original ou adiciona contexto
            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro de restrição de integridade ao salvar o vendedor " + novoVendedor.getNome() + ".");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Não foi possível salvar o vendedor " + novoVendedor.getNome() + ". Violação de regra de negócio!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Não foi possível salvar o vendedor " + novoVendedor.getNome() + ". Falha na conexão com o banco de dados!");
        }
    }

    /*
     *Atualiza os dados de um vendedor existente
     * Com o tratamento das devidadas exceptions
     */

    @Transactional
    public VendedorDTO atualizar(VendedorModel vendedorExistente) {
        try {
            //Caso ocorra uma tentativa de atualizar um vendedor que não existe utilizando um Cpf.
            if (!vendedorRepository.existsByCpf(vendedorExistente.getCpf())) {
                throw new ConstraintException("O vendedor com esse Cpf " + vendedorExistente.getCpf() + " não existe na base de dados!");
            }
            //Atualiza o vendedor na base de dados
            return vendedorRepository.save(vendedorExistente).toDTO();

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível atualizar o vendedor" + vendedorExistente.getNome() + " !");
        } catch (ConstraintException e) {
            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao atualizar o vendedor " + vendedorExistente.getNome() + ": Restrição de integridade de dados.");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Não foi possível atualizar o vendedor " + vendedorExistente.getNome() + ". Violação de regra de negócio!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Não foi possível atualizar o vendedor " + vendedorExistente.getNome() + ". Falha na conexão com o banco de dados!");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro! Não foi possível atualizar o vendedor" + vendedorExistente.getNome() + ". Não encontrado no banco de dados!");
        }
    }

    /*
     *Deleta um vendedor da base de dados.
     * Com o tratamento das devidadas exceptions
     */

    @Transactional
    public void deletar(VendedorModel vendedorExistente) {

        try {
            //Caso ocorra uma tentativa de deletar um vendedor que não existe utilizando o id.
            if (!vendedorRepository.existsById(vendedorExistente.getId())) {
                throw new ConstraintException("Vendedor inexistente na base de dados!");
            }
            //Deletar o vendedor na base de dados.
            vendedorRepository.delete(vendedorExistente);

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível deletar o vendedor " + vendedorExistente.getNome() + " !");
        } catch (ConstraintException e) {

            // Relança a mensagem original ou adiciona contexto
            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao deletar o vendedor " + vendedorExistente.getNome() + ": Restrição de integridade de dados.");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Não foi possível deletar o vendedor " + vendedorExistente.getNome() + ". Violação de regra de negócio!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Não foi possível deletar o vendedor " + vendedorExistente.getNome() + ". Falha na conexão com o banco de dados!");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro! Não foi possível deletar o vendedor" + vendedorExistente.getNome() + ". Não encontrado no banco de dados!");
        }
    }
}
