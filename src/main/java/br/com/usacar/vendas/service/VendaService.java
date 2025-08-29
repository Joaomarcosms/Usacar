package br.com.usacar.vendas.service;

import br.com.usacar.vendas.exception.*;
import br.com.usacar.vendas.model.*;
import br.com.usacar.vendas.repository.*;
import br.com.usacar.vendas.rest.dto.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Converte um VendaDTO (com IDs) para a entidade VendaModel.
     */
    public VendaModel converterParaEntidade(VendaDTO dto) {
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

    /**
     * Busca uma única venda pelo ID e retorna os dados completos.
     */
    @Transactional(readOnly = true)
    public VendaResponseDTO obterPorId(int id) {
        VendaModel venda = vendaRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Venda com " + id + " não encontrada"));
        return converterParaResponseDTO(venda);
    }

    /**
     * Busca todas as vendas e retorna uma lista com os dados completos de cada uma.
     */
    @Transactional(readOnly = true)
    public List<VendaResponseDTO> obterTodas() {
        List<VendaModel> vendas = vendaRepository.findAll();
        return vendas.stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Salva uma nova venda na base de dados.
     */
    @Transactional
    public VendaDTO salvar(VendaModel novaVenda) {
        if (novaVenda.getId() != null && novaVenda.getId().equals(0)) {
            novaVenda.setId(null);
        }

        if (novaVenda.getId() != null && vendaRepository.existsById(novaVenda.getId())) {
            throw new ConstraintException("Já existe uma venda com esse ID " + novaVenda.getId());
        }

        novaVenda.setStatusVenda("FINALIZADA");

        VendaModel vendaSalva = vendaRepository.save(novaVenda);

        CarroModel carro = carroRepository.findById(novaVenda.getCarro().getId())
                .orElseThrow(() -> new ObjectNotFoundException("Carro não encontrado com ID " + novaVenda.getCarro().getId()));

        StatusCarroModel statusVendido = statusCarroRepository.findByDescricaoIgnoreCase("Vendido")
                .orElseThrow(() -> new ObjectNotFoundException("Status 'Vendido' não encontrado."));

        carro.setStatus(statusVendido);
        carroRepository.save(carro);

        return vendaSalva.toDTO();
    }

    /**
     * Atualiza uma venda existente na base de dados.
     */
    @Transactional
    public VendaDTO atualizar(VendaModel vendaExistente) {
        if (!vendaRepository.existsById(vendaExistente.getId())) {
            throw new ConstraintException("A venda solicitada " + vendaExistente.getId() + " não existe na base de dados");
        }
        return vendaRepository.save(vendaExistente).toDTO();
    }

    /**
     * Cancela uma venda e reverte o status do carro.
     */
    @Transactional
    public VendaCancelamentoResponseDTO cancelarVenda(Integer vendaId) {
        VendaModel venda = vendaRepository.findById(vendaId)
                .orElseThrow(() -> new ObjectNotFoundException("Venda com ID " + vendaId + " não encontrada."));

        long diasDesdeVenda = ChronoUnit.DAYS.between(venda.getDataVenda(), LocalDate.now());
        if (diasDesdeVenda > 7) {
            throw new BusinessRuleException("A venda só pode ser cancelada até 7 dias após a data da venda.");
        }

        CarroModel carro = venda.getCarro();
        if (carro == null) {
            throw new BusinessRuleException("Carro associado à venda não encontrado.");
        }

        StatusCarroModel statusDisponivel = statusCarroRepository.findByDescricaoIgnoreCase("Disponível")
                .orElseThrow(() -> new ObjectNotFoundException("Status 'Disponível' não encontrado."));

        carro.setStatus(statusDisponivel);
        carroRepository.save(carro);

        vendaRepository.delete(venda);

        return new VendaCancelamentoResponseDTO(
                "Venda cancelada com sucesso.",
                carro.getId(),
                statusDisponivel.getDescricao()
        );
    }

    /**
     * Deleta uma venda da base de dados.
     */
    @Transactional
    public void deletar(Integer id) {
        if (!vendaRepository.existsById(id)) {
            throw new ConstraintException("Venda inexistente na base de dados com o ID: " + id);
        }
        vendaRepository.deleteById(id);
    }

    // --- MÉTODOS DE RELATÓRIO E OUTROS ---

    @Transactional(readOnly = true)
    public List<VendaRelatorioDTO> gerarRelatorioComissoes(LocalDate dataInicio, LocalDate dataFim) {
        if (dataFim == null) {
            dataFim = LocalDate.now();
        }
        return vendaRepository.gerarRelatorioVendasPorVendedor(dataInicio, dataFim);
    }

    @Transactional(readOnly = true)
    public List<VendaHistoricoDTO> obterHistoricoVendasPorCliente(Integer clienteId) {
        clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ObjectNotFoundException("Cliente com ID " + clienteId + " não encontrado"));

        List<VendaModel> vendas = vendaRepository.findAllByClienteId(clienteId);

        return vendas.stream().map(venda -> {
            CarroModel carro = venda.getCarro();
            String nomeModelo = (carro.getModelo() != null) ? carro.getModelo().getNome() : "N/A";
            String nomeMarca = (carro.getModelo() != null && carro.getModelo().getMarca() != null) ? carro.getModelo().getMarca().getNome() : "N/A";
            String nomeCor = (carro.getCor() != null) ? carro.getCor().getNome() : "N/A";

            return new VendaHistoricoDTO(
                    venda.getDataVenda(),
                    venda.getValorVenda(),
                    venda.getValorComissao(),
                    new VendaHistoricoDTO.CarroDTO(
                            nomeMarca, nomeModelo, carro.getAnoFabricacao(),
                            carro.getAnoModelo(), carro.getPlaca(), nomeCor
                    ),
                    venda.getVendedor().getNome()
            );
        }).collect(Collectors.toList());
    }

    @Transactional
    public List<ComissaoReajusteResponseDTO> reajustarComissao(ComissaoReajusteDTO dto) {
        List<ComissaoReajusteResponseDTO> resultados = new ArrayList<>();
        List<Object[]> vendedoresQualificados = vendaRepository.findVendasTotaisParaReajuste(
                dto.getDataInicio(), dto.getDataFim(), dto.getValorMinimoTotalVendas());

        for (Object[] result : vendedoresQualificados) {
            Integer vendedorId = (Integer) result[0];
            String vendedorNome = (String) result[1];
            Double valorTotalVendas = (Double) result[2];
            Double comissaoAnterior = (Double) result[3];
            Double comissaoReajustada = comissaoAnterior * (1 + dto.getPercentualReajuste() / 100.0);

            resultados.add(new ComissaoReajusteResponseDTO(
                    vendedorNome, valorTotalVendas, comissaoAnterior, comissaoReajustada
            ));

            if (!dto.isSimulacao()) {
                List<VendaModel> vendasDoVendedor = vendaRepository.findVendasByVendedorIdAndPeriodo(
                        vendedorId, dto.getDataInicio(), dto.getDataFim());
                vendasDoVendedor.forEach(venda -> {
                    Double novaComissao = venda.getValorComissao() * (1 + dto.getPercentualReajuste() / 100.0);
                    venda.setValorComissao(novaComissao);
                    vendaRepository.save(venda);
                });
            }
        }
        return resultados;
    }

    @Transactional(readOnly = true)
    public List<MarcaRankingDTO> obterRankingMarcasMaisVendidas(LocalDate dataInicio, LocalDate dataFim) {
        return vendaRepository.gerarRankingMarcasMaisVendidas(dataInicio, dataFim);
    }

    /**
     * Método auxiliar privado para converter VendaModel para VendaResponseDTO.
     */
    private VendaResponseDTO converterParaResponseDTO(VendaModel venda) {
        VendaResponseDTO dto = modelMapper.map(venda, VendaResponseDTO.class);
        if (venda.getCarro() != null) {
            dto.setCarro(modelMapper.map(venda.getCarro(), CarroDTO.class));
        }
        if (venda.getCliente() != null) {
            dto.setCliente(modelMapper.map(venda.getCliente(), ClienteDTO.class));
        }
        if (venda.getVendedor() != null) {
            dto.setVendedor(modelMapper.map(venda.getVendedor(), VendedorResponseDTO.class));
        }
        return dto;
    }
}
