package br.com.usacar.vendas.service;

import br.com.usacar.vendas.model.VendaModel;
import br.com.usacar.vendas.model.VendedorModel;
import br.com.usacar.vendas.repository.VendaRepository;
import br.com.usacar.vendas.rest.dto.RelatorioVendasDTO;
import br.com.usacar.vendas.rest.dto.VendaDetalheDTO;
import br.com.usacar.vendas.rest.dto.VendedorRelatorioDTO; // Usando o DTO específico para o relatório
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RelatorioService {

    @Autowired
    private VendaRepository vendaRepository;

    @Transactional(readOnly = true)
    public List<RelatorioVendasDTO> gerarRelatorioVendasPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        // 1. Busca as vendas no período usando a query que você já tem
        List<VendaModel> vendasNoPeriodo = vendaRepository.findVendasFinalizadasByPeriodo(dataInicio, dataFim);

        // 2. Agrupa as vendas por vendedor (usando o objeto VendedorModel como chave)
        Map<VendedorModel, List<VendaModel>> vendasPorVendedor = vendasNoPeriodo.stream()
                .collect(Collectors.groupingBy(VendaModel::getVendedor));

        // 3. Transforma o mapa no formato do DTO de relatório
        return vendasPorVendedor.entrySet().stream()
                .map(entry -> {
                    VendedorModel vendedor = entry.getKey();
                    List<VendaModel> vendasDoVendedor = entry.getValue();

                    // Mapeia os detalhes de cada venda para o VendaDetalheDTO
                    List<VendaDetalheDTO> detalhesVenda = vendasDoVendedor.stream()
                            .map(venda -> {
                                VendaDetalheDTO detalhe = new VendaDetalheDTO();
                                detalhe.setId(venda.getId());
                                detalhe.setData(venda.getDataVenda());
                                detalhe.setCliente(venda.getCliente().getNome());

                                // Monta a descrição do veículo
                                String veiculo = String.format("%s %s %d",
                                        venda.getCarro().getModelo().getMarca().getNome(),
                                        venda.getCarro().getModelo().getNome(),
                                        venda.getCarro().getAnoModelo());
                                detalhe.setVeiculo(veiculo);

                                // --- CORREÇÃO APLICADA ---
                                // Converte o 'double' do seu modelo para 'BigDecimal' no DTO
                                detalhe.setValor(BigDecimal.valueOf(venda.getValorVenda()));
                                return detalhe;
                            }).collect(Collectors.toList());

                    // Calcula o valor total vendido
                    BigDecimal valorTotal = detalhesVenda.stream()
                            .map(VendaDetalheDTO::getValor)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    // Monta o DTO do vendedor
                    VendedorRelatorioDTO vendedorDTO = new VendedorRelatorioDTO();
                    vendedorDTO.setNome(vendedor.getNome());
                    vendedorDTO.setCpf(vendedor.getCpf());

                    // Monta o DTO final do relatório para este vendedor
                    RelatorioVendasDTO relatorioDTO = new RelatorioVendasDTO();
                    relatorioDTO.setVendedor(vendedorDTO);
                    relatorioDTO.setQuantidadeVendas(detalhesVenda.size());
                    relatorioDTO.setValorTotalVendido(valorTotal);
                    relatorioDTO.setVendas(detalhesVenda);

                    return relatorioDTO;
                })
                .collect(Collectors.toList());
    }
}
