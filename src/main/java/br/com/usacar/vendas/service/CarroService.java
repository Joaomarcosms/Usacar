package br.com.usacar.vendas.service;

import br.com.usacar.vendas.exception.*;
import br.com.usacar.vendas.model.CarroModel;
import br.com.usacar.vendas.model.HistoricoStatusCarroModel;
import br.com.usacar.vendas.model.StatusCarroModel;
import br.com.usacar.vendas.model.VendaModel;
import br.com.usacar.vendas.repository.CarroRepository;
import br.com.usacar.vendas.repository.HistoricoStatusCarroRepository;
import br.com.usacar.vendas.repository.StatusCarroRepository;
import br.com.usacar.vendas.repository.VendaRepository;
import br.com.usacar.vendas.rest.dto.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarroService {

    //Injeção automatica de dependencias
    @Autowired
    private CarroRepository carroRepository;

    @Autowired
    private  VendaRepository vendaRepository;

    @Autowired
    private StatusCarroRepository statusCarroRepository;

    @Autowired
    private HistoricoStatusCarroRepository historicoStatusCarroRepository;

    @Autowired
    private ModelMapper modelMapper;


    public List<CarroEstoqueDTO> consultarEstoque(CarroFiltroDTO filtro) {
        return carroRepository.filtrarEstoque(filtro);

    }

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
    public CarroDTO salvar(CarroModel novoCarro) {
        // 1. Validação inicial (seu código original)
        if (carroRepository.existsByPlaca(novoCarro.getPlaca())) {
            throw new ConstraintException("Já existe um veículo cadastrado com esta placa " + novoCarro.getPlaca());
        }
        novoCarro.setDataCadastro(LocalDate.now());

        // 2. Salva a entidade principal do carro
        CarroModel carroSalvo = carroRepository.save(novoCarro);

        // 3. --- CORREÇÃO APLICADA AQUI ---
        // Busca a entidade Status completa para garantir que a descrição não seja nula.
        StatusCarroModel statusCompleto = statusCarroRepository.findById(carroSalvo.getStatus().getId())
                .orElseThrow(() -> new ObjectNotFoundException("Status com ID " + carroSalvo.getStatus().getId() + " não encontrado."));

        // 4. Cria o registro de histórico usando a descrição do status completo
        HistoricoStatusCarroModel logInicial = HistoricoStatusCarroModel.builder()
                .carro(carroSalvo)
                .statusAnterior("Não definido") // Correto, pois é um carro novo
                .novoStatus(statusCompleto.getDescricao()) // Usa a descrição do objeto que foi buscado no banco
                .dataHora(LocalDateTime.now())
                .usuarioResponsavel("Sistema") // Ou o usuário logado
                .build();
        historicoStatusCarroRepository.save(logInicial);

        // 5. Retorna o DTO
        return modelMapper.map(carroSalvo, CarroDTO.class);

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
            throw new BusinessRuleException("Erro! Não foi possível deletar o veículo " + carroExistente.getPlaca() + "Violação da regra de negócio!");
        } catch (SQLException e){
            throw new SQLException("Erro! Não foi possível  deletar o veículo " + carroExistente.getPlaca() + "Falha na conexão com o banco de dados");
        } catch (ObjectNotFoundException e){
            throw new ObjectNotFoundException("Erro! Não foi possível deletar o veículo " + carroExistente.getPlaca() + "Não encontrado no banco de dados!");
        }
    }

    @Transactional
    public CarroModel atualizarStatusCarro(Integer id, String novoStatusStr) {
        // Regra 1: Só é permitido alterar status de carros existentes.
        CarroModel carro = carroRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Carro com ID " + id + " não encontrado."));

        // Buscando o novo status no banco de dados
        StatusCarroModel novoStatus = statusCarroRepository.findByDescricaoIgnoreCase(novoStatusStr)
                .orElseThrow(() -> new BusinessRuleException("Status inválido: " + novoStatusStr));

        StatusCarroModel statusAtual = carro.getStatus();

        // NOVO: Regra de log do histórico
        // Verifica se o status realmente mudou antes de registrar o log
        if (!statusAtual.getDescricao().equalsIgnoreCase(novoStatus.getDescricao())) {
            HistoricoStatusCarroModel log = HistoricoStatusCarroModel.builder()
                    .carro(carro)
                    .statusAnterior(statusAtual.getDescricao())
                    .novoStatus(novoStatus.getDescricao())
                    .dataHora(LocalDateTime.now())
                    .usuarioResponsavel("admin@usacar.com") // Substitua pelo usuário autenticado
                    .build();
            historicoStatusCarroRepository.save(log);
        }

        // Regra 3: Se o status atual for Vendido, o carro não pode mais retornar
        // A comparação agora é feita pela descrição do status, não por um enum
        if ("Vendido".equalsIgnoreCase(statusAtual.getDescricao()) && !"Vendido".equalsIgnoreCase(novoStatus.getDescricao())) {
            throw new BusinessRuleException("Não é possível alterar o status de um carro já vendido para um status anterior.");
        }

        // Regra 2: Se o novo status for Vendido, deve haver registro de venda.
        if ("Vendido".equalsIgnoreCase(novoStatus.getDescricao())) {
            boolean temVendaAssociada = vendaRepository.existsByCarro_Id(id);
            if (!temVendaAssociada) {
                throw new BusinessRuleException("Não é possível mudar o status para 'Vendido' sem uma venda associada.");
            }
        }

        // Regra 4: Se o status for "Em manutenção", o carro não pode ter venda futura (agendada).
        if ("Em manutenção".equalsIgnoreCase(novoStatus.getDescricao())) {
            boolean temVendaAgendada = vendaRepository.existsByCarro_IdAndDataVendaAfter(id, LocalDate.now());
            if (temVendaAgendada) {
                throw new BusinessRuleException("Não é possível colocar o carro em manutenção com uma venda agendada.");
            }
        }

        carro.setStatus(novoStatus);
        return carroRepository.save(carro);
    }

    @Transactional
    public List<HistoricoStatusCarroDTO> obterHistoricoStatus(Integer carroId) {
        CarroModel carro = carroRepository.findById(carroId)
                .orElseThrow(() -> new ObjectNotFoundException("Carro com ID " + carroId + " não encontrado."));

        List<HistoricoStatusCarroModel> historico = historicoStatusCarroRepository.findAllByCarroIdOrderByDataHoraDesc(carroId);

        return historico.stream()
                .map(log -> modelMapper.map(log, HistoricoStatusCarroDTO.class))
                .collect(Collectors.toList());
    }
}


