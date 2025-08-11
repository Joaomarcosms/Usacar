package br.com.usacar.vendas.service;

import br.com.usacar.vendas.exception.*;
import br.com.usacar.vendas.model.MarcaModel;
import br.com.usacar.vendas.model.ModeloModel;
import br.com.usacar.vendas.repository.MarcaRepository;
import br.com.usacar.vendas.repository.ModeloRepository;
import br.com.usacar.vendas.rest.dto.ModeloDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ModeloService {
    @Autowired
    private ModeloRepository modeloRepository;

    @Autowired
    private MarcaRepository marcaRepository;


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

    @Transactional(readOnly = true)
    public List<ModeloDTO> obterPorMarcaId(Integer marcaId) {
        List<ModeloModel> modelos = modeloRepository.findAllByMarcaId(marcaId);
        return modelos.stream().map(modelo -> modelo.toDTO()).collect(Collectors.toList());
    }

    /*
     *Irá salvar os modelos na base de dados
     */

    @Transactional
    public ModeloDTO salvar(ModeloDTO novoModeloDTO) {
        try {
            Optional<MarcaModel> marca = marcaRepository.findById(novoModeloDTO.getMarca().getId());
            if (marca.isEmpty()) {
                throw new ObjectNotFoundException("Marca com ID " + novoModeloDTO.getMarca().getId() + " não encontrada");
            }

            ModeloModel modelo = new ModeloModel();
            modelo.setNome(novoModeloDTO.getNome());
            modelo.setMarca(marca.get());

            return modeloRepository.save(modelo).toDTO();


        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro!! Não foi possivel salvar o modelo " + novoModeloDTO.getId());
        } catch (ConstraintException e) {
            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro de Restrição de integridade ao salvar o modelo " + novoModeloDTO.getId());
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro!! Não foi possível salvar o modelo" + novoModeloDTO.getId());
        } catch (SQLException e) {
            throw new SQLException("Erro!! Não foi possível salvar o modelo " + novoModeloDTO.getId());
        }
    }

    /*
     *Irá atualizar o veículo na base de dadas
     */

    @Transactional
    public ModeloDTO atualizar(ModeloModel modeloExistente) {
        try {
            if (!modeloRepository.existsById(modeloExistente.getId())) {
                throw new ConstraintException("O modelo que deseja " + modeloExistente.getId() + "não existe na base de dados");
            }
            //Retorna os dados atualizados
            return modeloRepository.save(modeloExistente).toDTO();

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro!! Não foi possível atualizar o modelo " + modeloExistente.getId());
        } catch (ConstraintException e) {
            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao atualizar o modelo " + modeloExistente.getId());
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro!! Não foi possível atualizar o modelo " + modeloExistente.getId() + "Retrição de regra de negócio!");
        } catch (SQLException e) {
            throw new SQLException("Erro!! Não foi possível atualizar o modelo " + modeloExistente.getId() +  "Falha na conexão com o banco de dados");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro!! Não foi possível atualizar o modelo " + modeloExistente.getId() +  "Não encontrado no banco de dados!");
        }
    }

    /*
    *Deletar um veículo da base de dados
     */
    @Transactional
    public void deletar(ModeloModel modeloExistente) {
        try {
            if (!modeloRepository.existsById(modeloExistente.getId())) {
                throw new ConstraintException("Modelo inexistente na base de dados " );
            }
            //Deleta da base de dados
            modeloRepository.delete(modeloExistente);

        } catch (DataIntegrityException e){
            throw new DataIntegrityException("Erro!! Não foi possível deletar o modelo " + modeloExistente.getId());
        } catch (ConstraintException e){
            if (e.getMessage() == null || e.getMessage().isBlank()){
                throw new ConstraintException("Erro!! Não foi possível deletar o modelo " + modeloExistente.getId() + "Restrição de integridade de dados");
            }
            throw e;
        } catch (BusinessRuleException e){
            throw new BusinessRuleException("Erro! Não foi possível deletar o modelo " + modeloExistente.getId() + "Violação da regra de negócio!");
        } catch (SQLException e){
            throw new SQLException("Erro! Não foi possível deletar o modelo que deseja! " + modeloExistente.getId() + "Falha na conexão com o banco de dados");
        } catch (ObjectNotFoundException e){
            throw new ObjectNotFoundException("Erro! Não foi possível deletar o modelo " + modeloExistente.getId() + "Não encontrado no banco de dados!");
        }
    }


}

