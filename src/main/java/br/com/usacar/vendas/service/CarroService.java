package br.com.usacar.vendas.service;

import br.com.usacar.vendas.exception.*;
import br.com.usacar.vendas.model.CarroModel;
import br.com.usacar.vendas.repository.CarroRepository;
import br.com.usacar.vendas.rest.dto.CarroDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarroService {

    //Injeção automatica de dependencias
    @Autowired
    private CarroRepository carroRepository;

    //Irá obter por ID
    @Transactional(readOnly = true)
    public CarroDTO obterPorId(int id){
        CarroModel carro = carroRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Carro com " + id + "não encontrado"));
                return carro.toDTO();
    }

    /*
    *Aqui irá obter a lista de todos os carros cadastrados
    * @Return irá retornar a lista dos carros cadastrados
     */

    @Transactional(readOnly = true)
    public List<CarroDTO> obterTodos(){
        List<CarroModel> carros = carroRepository.findAll();
        return carros.stream().map(carro -> carro.toDTO()).collect(Collectors.toList());
    }

    /*
    *Irá salvar o veículo na base de dados com as devidas exceptions
     */

    @Transactional
    public CarroDTO salvar(CarroModel novoCarro){
        try {
            if(carroRepository.existsByPlaca(novoCarro.getPlaca())){
                throw new ConstraintException("Já existe um veículo cadastrado com esta placa " + novoCarro.getPlaca());
            }
            //Salvar o carro na base de dados.
            return carroRepository.save(novoCarro).toDTO();

        } catch (DataIntegrityException e){
            throw new DataIntegrityException("Erro!! Não foi possivel salvar o veículo " + novoCarro.getPlaca());
        } catch (ConstraintException e){
            if (e.getMessage() == null || e.getMessage().isBlank()){
                throw new ConstraintException("Erro de Restrição de integridade ao salvar o veículo " + novoCarro.getPlaca());
            }
            throw e;
        } catch (BusinessRuleException e){
            throw new BusinessRuleException("Erro!! Não foi possível salvar o veículo" + novoCarro.getPlaca() );
        } catch (SQLException e){
            throw new SQLException("Erro!! Não foi possível salvar o veículo " + novoCarro.getPlaca());
        }
    }

    /*
    *Irá atualizar o veículo na base de dadas com as devidas exceptions
    *
     */

    @Transactional
    public CarroDTO atualizar(CarroModel carroExistente){
        try {
            //Caso ocorra uma tentativa de salvar um veículo que não existe utilizando a placa.
            if(!carroRepository.existsByPlaca(carroExistente.getPlaca())){
                throw new ConstraintException("O veículo com essa placa " + carroExistente.getPlaca() + "não existe na base de dados");
            }

            return carroRepository.save(carroExistente).toDTO();

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro!! Não foi possível atualizar o veículo " + carroExistente.getPlaca());
        } catch (ConstraintException e) {
            //Relança a mensagem original ou adiciona contexto
            if (e.getMessage() == null || e.getMessage().isBlank()){
                throw new ConstraintException("Erro ao atualizar o veículo " + carroExistente.getPlaca());
            }
            throw e;
        }catch (BusinessRuleException e){
            throw new BusinessRuleException("Erro!! Não foi possível atualizar o veículo " + carroExistente.getPlaca() + "Retrição de regra de negócio!");
        } catch (SQLException e){
            throw new SQLException("Erro!! Não foi possível atualizar o veículo " + carroExistente.getPlaca() +  "Falha na conexão com o banco de dados");
        } catch (ObjectNotFoundException e){
            throw new ObjectNotFoundException("Erro!! Não foi possível atualizar o veículo " + carroExistente.getPlaca() +  "Não encontrado no banco de dados!");
        }
    }

    /*
    *Deletar um veículo da base de dados com as devidas exceptions
     */

    @Transactional
    public void deletar(CarroModel carroExistente){
        try {
            //Caso ocorra uma tentativa de deletar um veículo que não existe utilizando a placa
            if(!carroRepository.existsByPlaca(carroExistente.getPlaca())){
                throw new ConstraintException("Veículo inexistente na base de dados " );
            }
            carroRepository.delete(carroExistente);

        }catch (DataIntegrityException e){
            throw new DataIntegrityException("Erro!! Não foi possível deletar o veículo " + carroExistente.getPlaca());
        } catch (ConstraintException e){
            if (e.getMessage() == null || e.getMessage().isBlank()){
                throw new ConstraintException("Erro!! Não foi possível deletar o veículo " + carroExistente.getPlaca() + "Restrição de integridade de dados");
            }
            throw e;
        } catch (BusinessRuleException e){
            throw new BusinessRuleException("Erro! Não foi possível deletar o veículo " + carroExistente.getPlaca() + "Violãção da regra de negócio!");
        } catch (SQLException e){
            throw new SQLException("Erro! Não foi possível  deletar o veículo " + carroExistente.getPlaca() + "Falha na conexão com o banco de dados");
        } catch (ObjectNotFoundException e){
            throw new ObjectNotFoundException("Erro! Não foi possível deletar o veículo " + carroExistente.getPlaca() + "Não encontrado no banco de dados!");
        }
    }

}
