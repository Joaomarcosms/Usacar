package br.com.usacar.vendas.service;

import br.com.usacar.vendas.exception.*;
import br.com.usacar.vendas.model.ClienteModel;
import br.com.usacar.vendas.repository.ClienteRepository;
import br.com.usacar.vendas.rest.dto.ClienteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional(readOnly = true)
    public ClienteDTO obterPorId(int id) {
        ClienteModel cliente = clienteRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Cliente com " + id + " não encontrado"));
        return cliente.toDTO();
    }

    /**
     * Aqui obtem a lista de todos os clientes cadastrados
     * @return a lista dos clientes cadastrados
     */

    @Transactional(readOnly = true)
    public List<ClienteDTO> obterTodos() {
        List<ClienteModel> clientes = clienteRepository.findAll();
        return clientes.stream().map(cliente -> cliente.toDTO()).collect(Collectors.toList());
    }

    /**
     * Salva um novo cliente na base de dados.
     *
     * @param novoCliente ClienteModel contendo os dados do novo cliente.
     * @return ClienteDTO representando o cliente salvo.
     * @throws ConstraintException       Se o telefone ou e-mail já existirem.
     * @throws DataIntegrityException    Se ocorrer violação de integridade.
     * @throws BusinessRuleException     Se houver violação de regra de negócio.
     * @throws SQLException              Se ocorrer falha de conexão com o banco de dados.
     */
    @Transactional
    public ClienteDTO salvar(ClienteModel novoCliente) {
        try {
            //Caso ocorra uma tentativa de salvar um novo Cliente com um cpf já existente.
            if (clienteRepository.existsByCpf(novoCliente.getCpf())) {
                throw new ConstraintException("Já existe um Cliente com esse Cpf " + novoCliente.getCpf() + " na base de dados!");
            }

            //Salva o novo Cliente na base de dados.
            return clienteRepository.save(novoCliente).toDTO();

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível salvar o Cliente " + novoCliente.getNome() + " !");
        } catch (ConstraintException e) {
            // Relança a mensagem original ou adiciona contexto
            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro de restrição de integridade ao salvar o Cliente " + novoCliente.getNome() + ".");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Não foi possível salvar o Cliente " + novoCliente.getNome() + ". Violação de regra de negócio!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Não foi possível salvar o Cliente " + novoCliente.getNome() + ". Falha na conexão com o banco de dados!");
        }
    }

    /**
     * Atualiza os dados de um cliente existente.
     *
     * @param clienteExistente ClienteModel contendo os dados atualizados do cliete.
     * @return ClienteDTO representando o cliente atualizado.
     * @throws ConstraintException       Se o telefone ou e-mail não existir.
     * @throws DataIntegrityException    Se ocorrer violação de integridade.
     * @throws BusinessRuleException     Se houver violação de regra de negócio.
     * @throws SQLException              Se ocorrer falha de conexão com o banco de dados.
     */
    @Transactional
    public ClienteDTO atualizar(ClienteModel clienteExistente) {

        try {
            //Caso ocorra uma tentativa de salvar um cliente que não existe utilizando um Cpf.
            if (!clienteRepository.existsByCpf(clienteExistente.getCpf())) {
                throw new ConstraintException("O Cliente com esse Cpf " + clienteExistente.getCpf() + " não existe na base de dados!");
            }

            //Atualiza o cliente na base de dados.
            return clienteRepository.save(clienteExistente).toDTO();

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível atualizar o Cliente " + clienteExistente.getNome() + " !");
        } catch (ConstraintException e) {
            // Relança a mensagem original ou adiciona contexto
            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao atualizar o Cliente " + clienteExistente.getNome() + ": Restrição de integridade de dados.");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Não foi possível atualizar o Cliente " + clienteExistente.getNome() + ". Violação de regra de negócio!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Não foi possível atualizar o Cliente " + clienteExistente.getNome() + ". Falha na conexão com o banco de dados!");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro! Não foi possível atualizar o Cliente" + clienteExistente.getNome() + ". Não encontrado no banco de dados!");
        }
    }

    /**
     * Deleta um cliente da base de dados.
     *
     * @param clienteExistente ClienteModel contendo os dados do cliente a ser deletado.
     * @throws ConstraintException       Se o cliente (id) não existir.
     * @throws DataIntegrityException    Se ocorrer violação de integridade.
     * @throws BusinessRuleException     Se houver violação de regra de negócio.
     * @throws SQLException              Se ocorrer falha de conexão com o banco de dados..
     */
    @Transactional
    public void deletar(ClienteModel clienteExistente) {

        try {
            //Caso ocorra uma tentativa de deletar um cliente que não existe utilizando o id.
            if (!clienteRepository.existsById(clienteExistente.getId())) {
                throw new ConstraintException("Cliente inexistente na base de dados!");
            }

            //Deletar o cliente na base de dados.
            clienteRepository.delete(clienteExistente);

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível deletar o cliente " + clienteExistente.getNome() + " !");
        } catch (ConstraintException e) {
            // Relança a mensagem original ou adiciona contexto
            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao deletar o cliente " + clienteExistente.getNome() + ": Restrição de integridade de dados.");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Não foi possível deletar o cliente " + clienteExistente.getNome() + ". Violação de regra de negócio!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Não foi possível atualizar o deletar " + clienteExistente.getNome() + ". Falha na conexão com o banco de dados!");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro! Não foi possível deletar o cliente" + clienteExistente.getNome() + ". Não encontrado no banco de dados!");
        }
    }


}
