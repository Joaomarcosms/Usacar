package br.com.usacar.vendas.service;

import br.com.usacar.vendas.exception.*;
import br.com.usacar.vendas.model.StatusCarroModel;
import br.com.usacar.vendas.repository.StatusCarroRepository;
import br.com.usacar.vendas.rest.dto.StatusCarroDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatusCarroService {

    //Injeção automatica de dependencias
    @Autowired
    private StatusCarroRepository statusCarroRepository;

    @Transactional(readOnly = true)
    public StatusCarroDTO obterPorId(int id) {
        StatusCarroModel status = statusCarroRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("O status do carro com " + id + "não foi encontrado"));
        return status.toDTO();
    }

    /*
     *Aqui irá obter todos os status dos veículos
     * @Return irá retornar os status dos veículos
     */

    @Transactional
    public List<StatusCarroDTO> obterTodos() {
        List<StatusCarroModel> status = statusCarroRepository.findAll();
        return status.stream().map(statusCarro -> statusCarro.toDTO()).collect(Collectors.toList());
    }

    /*
     *Irá salvar os Status na base de dados
     */

    @Transactional
    public StatusCarroDTO salvar(StatusCarroModel novoStatus) {
        try {
            if(statusCarroRepository.existsById(novoStatus.getId())) {
                throw new ConstraintException("Já existe um status com esse ID " + novoStatus.getId());
            }
            //Salvar o status na base de dados.
            return statusCarroRepository.save(novoStatus).toDTO();
        } catch (DataIntegrityException e){
            throw new DataIntegrityException("Erro!! Não foi possivel salvar o Status " + novoStatus.getId());
        } catch (ConstraintException e){
            if (e.getMessage() == null || e.getMessage().isBlank()){
                throw new ConstraintException("Erro de Restrição de integridade ao salvar o Status" + novoStatus.getId());
            }
            throw e;
        } catch (BusinessRuleException e){
            throw new BusinessRuleException("Erro!! Não foi possível salvar o Status" + novoStatus.getId() );
        } catch (SQLException e){
            throw new SQLException("Erro!! Não foi possível salvar o Status " + novoStatus.getId());
        }
    }

    /*
     *Irá atualizar o Status na base de dadas
     */
    @Transactional
    public StatusCarroDTO atualizar(StatusCarroModel statusExistente) {
        try {
            if(!statusCarroRepository.existsById(statusExistente.getId())) {
                throw new ConstraintException("O Status com " + statusExistente.getId() + "não existe na base de dados");
            }
            //Retorna os dados atualizados
            return statusCarroRepository.save(statusExistente).toDTO();
        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro!! Não foi possível atualizar o Status " + statusExistente.getId());
        } catch (ConstraintException e) {
            //Relança a mensagem original ou adiciona contexto
            if (e.getMessage() == null || e.getMessage().isBlank()){
                throw new ConstraintException("Erro ao atualizar o Status " + statusExistente.getId());
            }
            throw e;
        }catch (BusinessRuleException e){
            throw new BusinessRuleException("Erro!! Não foi possível atualizar o Status " + statusExistente.getId() + "Retrição de regra de negócio!");
        } catch (SQLException e){
            throw new SQLException("Erro!! Não foi possível atualizar o Status " + statusExistente.getId() +  "Falha na conexão com o banco de dados");
        } catch (ObjectNotFoundException e){
            throw new ObjectNotFoundException("Erro!! Não foi possível atualizar o Status " + statusExistente.getId() +  "Não encontrado no banco de dados!");
        }
    }

    /*
     *Deletar o Status do veículo na base de dados
     */

    @Transactional
    public void deletar(StatusCarroModel statusExistente) {
        try {
            if(!statusCarroRepository.existsById(statusExistente.getId())) {
                throw new ConstraintException("Status de veículo inexistente na base de dados " );
            }
            //Deleta da base de dados
            statusCarroRepository.delete(statusExistente);

        }catch (DataIntegrityException e){
            throw new DataIntegrityException("Erro!! Não foi possível deletar o Status " + statusExistente.getId());
        } catch (ConstraintException e){
            if (e.getMessage() == null || e.getMessage().isBlank()){
                throw new ConstraintException("Erro!! Não foi possível deletar o Status " + statusExistente.getId() + "Restrição de integridade de dados");
            }
            throw e;
        } catch (BusinessRuleException e){
            throw new BusinessRuleException("Erro! Não foi possível deletar o Status " + statusExistente.getId() + "Violação da regra de negócio!");
        } catch (SQLException e){
            throw new SQLException("Erro! Não foi possível deletar o Status" + statusExistente.getId() + "Falha na conexão com o banco de dados");
        } catch (ObjectNotFoundException e){
            throw new ObjectNotFoundException("Erro! Não foi possível deletar o Status " + statusExistente.getId() + "Não encontrado no banco de dados!");
        }
    }
}
