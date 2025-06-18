package br.com.usacar.vendas.service;

import br.com.usacar.vendas.exception.*;
import br.com.usacar.vendas.model.MarcaModel;
import br.com.usacar.vendas.repository.MarcaRepository;
import br.com.usacar.vendas.rest.dto.MarcaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarcaService {

    //Injeção automatica de dependencias
    @Autowired
    private MarcaRepository marcaRepository;

    //Obtem por ID
    @Transactional(readOnly = true)
    public MarcaDTO obterPorId(int id) {
        MarcaModel marca = marcaRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Marca com " + id + "não encontrado"));
        return marca.toDTO();
    }

    /*
    *Aqui irá obter a lista de marcas cadastradas em sistema
     */
    @Transactional(readOnly = true)
    public List<MarcaDTO> obterTodas() {
        List<MarcaModel> marcas = marcaRepository.findAll();
        return marcas.stream().map(marca -> marca.toDTO()).collect(Collectors.toList());
    }

    /*
    Irá salvar a marca de veículo na base de dados
     */
    @Transactional
    public MarcaDTO salvar(MarcaModel novaMarca) {
        try {
            if(marcaRepository.existsByNome(novaMarca.getNome())){
                throw new ConstraintException("Já existe uma marca cadastrada em sistema " + novaMarca.getNome());
            }
            //Retorna os dados que foram salvos
            return marcaRepository.save(novaMarca).toDTO();

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro!! Não foi possivel salvar a marca " + novaMarca.getNome());

        } catch (ConstraintException e) {
            if(e.getMessage() == null || e.getMessage().isBlank()){
                throw new ConstraintException("Erro de Restrição de integridade ao salvar  a marca " + novaMarca.getNome());
            }
            throw e;
        } catch (BusinessRuleException e){
            throw new BusinessRuleException("Erro!! Não foi possível salvar  a marca" + novaMarca.getNome() );
        } catch (SQLException e){
            throw new SQLException("Erro!! Não foi possível salvar  a marca " + novaMarca.getNome());
        }


        }

    /*
     **Irá atualizar a marca na base de dados
     */
        @Transactional
        public MarcaDTO atualizar(MarcaModel marcaExistente) {
            //Caso ocorra uma tentativa de salvar uma marca que não existe utilizando o nome.
            try {
                if(marcaRepository.existsByNome(marcaExistente.getNome())){
                    throw new ConstraintException("A marca informada" + marcaExistente.getNome() + " não existe na base de dados");
                }
                //Retorna a lista atualizada
                return marcaRepository.save(marcaExistente).toDTO();

            } catch (DataIntegrityException e) {
                throw new DataIntegrityException("Erro!! Não foi possível atualizar  a marca" + marcaExistente.getNome());
            } catch (ConstraintException e) {
                if(e.getMessage() == null || e.getMessage().isBlank()){
                    throw new ConstraintException("Erro ao atualizar  a marca " + marcaExistente.getNome());
                }
                throw e;
            } catch (BusinessRuleException e){
                throw new BusinessRuleException("Erro!! Não foi possível atualizar  a marca " + marcaExistente.getNome() + "Retrição de regra de negócio!");
            } catch (SQLException e) {
                throw new SQLException("Erro!! Não foi possível atualizar  a marca" + marcaExistente.getNome() +  "Falha na conexão com o banco de dados");
            } catch (ObjectNotFoundException e) {
                throw new ObjectNotFoundException("Erro!! Não foi possível atualizar  a marca " + marcaExistente.getNome() +  "Não encontrado no banco de dados!");
            }


    }

    /*
     *Deletar uma marca da base de dados
     */

    @Transactional
    public void deletar(MarcaModel marcaExistente) {
        try {
            if(!marcaRepository.existsById(marcaExistente.getId())){
                throw new ConstraintException("Marca inexistente na base de dados " );
            }
            //Deleta da base de dados
            marcaRepository.delete(marcaExistente);

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro!! Não foi possível deletar  a marca" + marcaExistente.getNome());
        } catch (ConstraintException e) {
            if(e.getMessage() == null || e.getMessage().isBlank()){
                throw new ConstraintException("Erro!! Não foi possível deletar  a marca " + marcaExistente.getNome() + "Restrição de integridade de dados");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Não foi possível deletar  a marca " + marcaExistente.getNome() + "Violação da regra de negócio!");

        } catch (SQLException e){
            throw new SQLException("Erro! Não foi possível deletar  a marca " + marcaExistente.getNome() + "Falha na conexão com o banco de dados");

        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro! Não foi possível deletar  a marca " + marcaExistente.getNome() + "Não encontrado no banco de dados!");

        }
    }

}
