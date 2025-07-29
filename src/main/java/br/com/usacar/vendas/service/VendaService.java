package br.com.usacar.vendas.service;

import br.com.usacar.vendas.exception.*;
import br.com.usacar.vendas.model.*;
import br.com.usacar.vendas.repository.*;

import br.com.usacar.vendas.rest.dto.VendaDTO;
import br.com.usacar.vendas.rest.dto.VendaRelatorioDTO;
import org.hibernate.validator.constraintvalidators.RegexpURLValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendaService {

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private CarroRepository carroRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private VendedorRepository vendedorRepository;

    @Autowired
    private StatusCarroRepository statusCarroRepository;

    /*
    Método que converte DTO para entidade VendaModel
     */
    private VendaModel converterParaEntidade(VendaDTO dto) {
        CarroModel carro = carroRepository.findById(dto.getCarroId())
                .orElseThrow(() -> new BusinessRuleException("Carro não encontrado"));
        ClienteModel cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new BusinessRuleException("Cliente não encontrado"));
        VendedorModel vendedor = vendedorRepository.findById(dto.getVendedorId())
                .orElseThrow(() -> new BusinessRuleException("Vendedor não encontrado"));

        return VendaModel.builder()
                .id(dto.getId())
                .dataVenda(dto.getDataVenda())
                .valorVenda(dto.getValorVenda())
                .valorComissao(dto.getValorComissao())
                .carro(carro)
                .cliente(cliente)
                .vendedor(vendedor)
                .build();
    }


    @Transactional(readOnly = true)
    public VendaDTO obterPorId(int id){
        VendaModel venda = vendaRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Venda com " + id + "não encontrada"));
        return venda.toDTO();
    }

    /*
     *Aqui irá obter a lista de todas as vendas em sistema.
     * @Return irá retornar a lista dos vendas em sistema.
     */

    @Transactional(readOnly = true)
    public List<VendaDTO> obterTodas(){
        List<VendaModel> vendas = vendaRepository.findAll();
        return vendas.stream().map(venda -> venda.toDTO()).collect(Collectors.toList());
    }

    public List<VendaRelatorioDTO> gerarRelatorioComissao(LocalDate dataInicio, LocalDate dataFim) {
        if (dataFim == null) {
            dataFim = LocalDate.now();
        }

        return vendaRepository.gerarRelatorioVendasPorVendedor(dataInicio, dataFim);
    }


    /*
     *Irá salvar o todas as vendas na base de dados
     */

    @Transactional
    public VendaDTO salvar(VendaModel novaVenda) {
        try {
            if (vendaRepository.existsById(novaVenda.getId())) {
                throw new ConstraintException("Já existe uma venda com esse ID " + novaVenda.getId());
            }

            // Salva a venda
            VendaModel vendaSalva = vendaRepository.save(novaVenda);

            // Busca o carro vendido
            CarroModel carro = carroRepository.findById(novaVenda.getCarro().getId())
                    .orElseThrow(() -> new ObjectNotFoundException("Carro não encontrado com ID " + novaVenda.getCarro().getId()));

            // Busca o status "Vendido"
            StatusCarroModel statusVendido = statusCarroRepository.findByDescricaoIgnoreCase("Vendido")
                    .orElseThrow(() -> new ObjectNotFoundException("Status 'Vendido' não encontrado."));

            // Atualiza o status do carro
            carro.setStatus(statusVendido);
            carroRepository.save(carro);

            return vendaSalva.toDTO();

        } catch (DataIntegrityException e){
            throw new DataIntegrityException("Erro!! Não foi possivel salvar a venda " + novaVenda.getId());
        } catch (ConstraintException e){
            if (e.getMessage() == null || e.getMessage().isBlank()){
                throw new ConstraintException("Erro de Restrição de integridade ao salvar a venda " + novaVenda.getId());
            }
            throw e;
        } catch (BusinessRuleException e){
            throw new BusinessRuleException("Erro!! Não foi possível salvar a venda" + novaVenda.getId() );
        } catch (SQLException e){
            throw new SQLException("Erro!! Não foi possível salvar a venda " + novaVenda.getId());
        }
    }

    /*
     *Irá atualizar uma venda na base de dadas
     */

    @Transactional
    public VendaDTO atualizar(VendaModel vendaExistente){
        try {
            if(!vendaRepository.existsById(vendaExistente.getId())){
                throw new ConstraintException("A venda solicitada " + vendaExistente.getId() + " não existe na base de dados");
            }

            return vendaRepository.save(vendaExistente).toDTO();
        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro!! Não foi possível atualizar os dados da venda " + vendaExistente.getId());
        } catch (ConstraintException e) {
            //Relança a mensagem original ou adiciona contexto
            if (e.getMessage() == null || e.getMessage().isBlank()){
                throw new ConstraintException("Erro ao atualizar os dados da venda " + vendaExistente.getId());
            }
            throw e;
        }catch (BusinessRuleException e){
            throw new BusinessRuleException("Erro!! Não foi possível atualizar os dados da venda " + vendaExistente.getId() + "Retrição de regra de negócio!");
        } catch (SQLException e){
            throw new SQLException("Erro!! Não foi possível atualizar os dados da venda " + vendaExistente.getId() +  "Falha na conexão com o banco de dados");
        } catch (ObjectNotFoundException e){
            throw new ObjectNotFoundException("Erro!! Não foi possível atualizar os dados da venda" + vendaExistente.getId() +  "Não encontrado no banco de dados!");
        }
    }

    /*
     *Deletar uma venda da base de dados
     */

    @Transactional
    public void deletar(VendaModel vendaExistente){
        try {
            if(!vendaRepository.existsById(vendaExistente.getId())){
                throw new ConstraintException("Venda inexistente na base de dados " );

            }

            vendaRepository.deleteById(vendaExistente.getId());
        } catch (DataIntegrityException e){
            throw new DataIntegrityException("Erro!! Não foi possível deletar a venda " + vendaExistente.getId());
        } catch (ConstraintException e){
            if (e.getMessage() == null || e.getMessage().isBlank()){
                throw new ConstraintException("Erro!! Não foi possível deletar a venda " + vendaExistente.getId() + "Restrição de integridade de dados");
            }
            throw e;
        } catch (BusinessRuleException e){
            throw new BusinessRuleException("Erro! Não foi possível deletar a venda " + vendaExistente.getId() + "Violação da regra de negócio!");
        } catch (SQLException e){
            throw new SQLException("Erro! Não foi possível  deletar a venda " + vendaExistente.getId() + "Falha na conexão com o banco de dados");
        } catch (ObjectNotFoundException e){
            throw new ObjectNotFoundException("Erro! Não foi possível deletar a venda " + vendaExistente.getId() + "Não encontrado no banco de dados!");
        }
    }

    //Gera relatorio de Venda de inicio e fim
    @Transactional(readOnly = true)
    public List<VendaRelatorioDTO> gerarRelatorioComissoes(LocalDate dataInicio, LocalDate dataFim) {
        if (dataFim == null) {
            dataFim = LocalDate.now();
        }
        return vendaRepository.gerarRelatorioVendasPorVendedor(dataInicio, dataFim);
    }

}
