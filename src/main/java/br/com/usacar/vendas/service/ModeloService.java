package br.com.usacar.vendas.service;

import br.com.usacar.vendas.exception.*;
import br.com.usacar.vendas.model.ModeloModel;
import br.com.usacar.vendas.repository.ModeloRepository;
import br.com.usacar.vendas.rest.dto.ModeloDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModeloService {
    @Autowired
    private ModeloRepository modeloRepository;

    @Transactional(readOnly = true)
    public ModeloDTO obterPorId(int id) {
        ModeloModel modelo = modeloRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Modelo com " + id + "não encontrado"));
        return modelo.toDTO();
    }

    /*
     *Aqui irá obter a lista de todos os modelos cadastrados
     * @Return irá retornar a lista dos modelos cadastrados
     */

    @Transactional
    public List<ModeloDTO> obterTodos() {
        List<ModeloModel> modelos = modeloRepository.findAll();
        return modelos.stream().map(modelo -> modelo.toDTO()).collect(Collectors.toList());
    }

    /*
     *Irá salvar os modelos na base de dados
     */

    @Transactional
    public ModeloDTO salvar(ModeloModel novoModelo) {
        try {
            if (modeloRepository.existsById(novoModelo.getId())) {
                throw new ConstraintException("Já existe um carro cadastrado com esta placa " + novoModelo.getId());
            }
            //Salvar o modelo na base de dados
            return modeloRepository.save(novoModelo).toDTO();

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro!! Não foi possivel salvar o veículo " + novoModelo.getId());
        } catch (ConstraintException e) {
            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro de Restrição de integridade ao salvar o veículo " + novoModelo.getId());
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro!! Não foi possível salvar o veículo" + novoModelo.getId());
        } catch (SQLException e) {
            throw new SQLException("Erro!! Não foi possível salvar o veículo " + novoModelo.getId());
        }
    }

    /*
     *Irá atualizar o veículo na base de dadas
     */

    @Transactional
    public ModeloDTO atualizar(ModeloModel modeloExistente) {
        try {
            if (!modeloRepository.existsById(modeloExistente.getId())) {
                throw new ConstraintException("O veículo com essa placa " + modeloExistente.getId() + "não existe na base de dados");
            }
            return modeloRepository.save(modeloExistente).toDTO();

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro!! Não foi possível atualizar o veículo " + modeloExistente.getId());
        } catch (ConstraintException e) {
            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao atualizar o veículo " + modeloExistente.getId());
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro!! Não foi possível atualizar o veículo " + modeloExistente.getId() + "Retrição de regra de negócio!");
        } catch (SQLException e) {
            throw new SQLException("Erro!! Não foi possível atualizar o veículo " + modeloExistente.getId() +  "Falha na conexão com o banco de dados");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro!! Não foi possível atualizar o veículo " + modeloExistente.getId() +  "Não encontrado no banco de dados!");
        }
    }

    /*
    *Deletar um veículo da base de dados
     */

    @Transactional
    public void deletar(ModeloModel modeloExistente) {
        try {
            if (!modeloRepository.existsById(modeloExistente.getId())) {
                throw new ConstraintException("Veículo inexistente na base de dados " );
            }
            modeloRepository.delete(modeloExistente);

        } catch (DataIntegrityException e){
            throw new DataIntegrityException("Erro!! Não foi possível deletar o veículo " + modeloExistente.getId());
        } catch (ConstraintException e){
            if (e.getMessage() == null || e.getMessage().isBlank()){
                throw new ConstraintException("Erro!! Não foi possível deletar o veículo " + modeloExistente.getId() + "Restrição de integridade de dados");
            }
            throw e;
        } catch (BusinessRuleException e){
            throw new BusinessRuleException("Erro! Não foi possível deletar o veículo " + modeloExistente.getId() + "Violão da regra de negócio!");
        } catch (SQLException e){
            throw new SQLException("Erro! Não foi possível atualizar o deletar " + modeloExistente.getId() + "Falha na conexão com o banco de dados");
        } catch (ObjectNotFoundException e){
            throw new ObjectNotFoundException("Erro! Não foi possível deletar o veículo " + modeloExistente.getId() + "Não encontrado no banco de dados!");
        }
    }
}

