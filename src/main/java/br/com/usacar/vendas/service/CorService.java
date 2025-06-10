package br.com.usacar.vendas.service;

import br.com.usacar.vendas.exception.*;
import br.com.usacar.vendas.model.CorModel;
import br.com.usacar.vendas.repository.CorRepository;
import br.com.usacar.vendas.rest.dto.CorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CorService {
    @Autowired
    private CorRepository corRepository;

    @Transactional(readOnly = true)
    public CorDTO obterPorId(int id){
        CorModel cor = corRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Cor com " + id + "não encontrado"));
        return cor.toDTO();
    }

    /*
     *Aqui irá obter a lista de todos os carros cadastrados
     * @Return irá retornar a lista dos carros cadastrados
     */

    @Transactional(readOnly = true)
    public List<CorDTO> obterTodos(){
        List<CorModel> cores = corRepository.findAll();
        return cores.stream().map(cor -> cor.toDTO() ).collect(Collectors.toList());
    }

    /*
     *Irá salvar o veículo na base de dados
     */

    @Transactional
    public CorDTO salvar(CorModel novaCor){
        try {
            if (corRepository.existsByNome(novaCor.getNome())) {
                throw new ConstraintException("Já existe um carro cadastrado com esta placa " + novaCor.getNome());
            }

            //Salvar a cor na base de dados.
            return corRepository.save(novaCor).toDTO();

        } catch (DataIntegrityException e){
            throw new DataIntegrityException("Erro!! Não foi possivel salvar o veículo " + novaCor.getNome());
        } catch (ConstraintException e){
            if(e.getMessage() == null || e.getMessage().isBlank()){
                throw new ConstraintException("Erro de Restrição de integridade ao salvar o veículo " + novaCor.getNome());
            }
            throw e;
        } catch (BusinessRuleException e){
            throw new BusinessRuleException("Erro!! Não foi possível salvar o veículo" + novaCor.getNome());
        } catch (SQLException e){
            throw new SQLException("Erro!! Não foi possível salvar o veículo " + novaCor.getNome());
        }
    }

    /*
    *Irá atualizar a cor na base de dados
     */

    @Transactional
    public CorDTO atualizar(CorModel corExistente){
        try {
            //Caso ocorra uma tentativa de salvar uma cor utilizando o nome.
            if(!corRepository.existsByNome(corExistente.getNome())){
                throw new ConstraintException("O veículo com essa placa " + corExistente.getNome() + "não existe na base de dados");
            }
            return corRepository.save(corExistente).toDTO();
        } catch (DataIntegrityException e){
            throw new DataIntegrityException("Erro!! Não foi possível atualizar o veículo " + corExistente.getNome());
        } catch (ConstraintException e){
            //Relança a mensagem original ou adiciona context
            if(e.getMessage() == null || e.getMessage().isBlank()){
                throw new ConstraintException("Erro ao atualizar o veículo " + corExistente.getNome());
            }
            throw e;
        } catch (BusinessRuleException e){
            throw new BusinessRuleException("Erro!! Não foi possível atualizar o veículo " + corExistente.getNome() + "Retrição de regra de negócio!");
        } catch (SQLException e){
            throw new SQLException("Erro!! Não foi possível atualizar o veículo " + corExistente.getNome() +  "Falha na conexão com o banco de dados");
        } catch (ObjectNotFoundException e){
            throw new ObjectNotFoundException("Erro!! Não foi possível atualizar o veículo " + corExistente.getNome() +  "Não encontrado no banco de dados!");
        }

    }

    /*
    *Deletar uma cor da base de dados.
     */

    @Transactional
    public void deletar(CorModel corExistente){
        try {
            //Caso ocorra uma tentativa de deletar uma cor utilizando o nome dela.
            if(!corRepository.existsByNome(corExistente.getNome())){
                throw new ConstraintException("Cor inexistente na base de dados " );
            }
            corRepository.delete(corExistente);
        } catch (DataIntegrityException e){
            throw new DataIntegrityException("Erro!! Não foi possível deletar o veículo " + corExistente.getNome());
        } catch (ConstraintException e){
            if(e.getMessage() == null || e.getMessage().isBlank()){
                throw new ConstraintException("Erro!! Não foi possível deletar o veículo " + corExistente.getNome() + "Restrição de integridade de dados");
            }
            throw e;
        } catch (BusinessRuleException e){
            throw new BusinessRuleException("Erro! Não foi possível deletar o veículo " + corExistente.getNome() + "Violão da regra de negócio!");
        } catch(SQLException e){
            throw new SQLException("Erro! Não foi possível atualizar o deletar " + corExistente.getNome() + "Falha na conexão com o banco de dados");
        } catch (ObjectNotFoundException e){
            throw new ObjectNotFoundException("Erro! Não foi possível deletar o veículo " + corExistente.getNome() + "Não encontrado no banco de dados!");
        }
    }
}
