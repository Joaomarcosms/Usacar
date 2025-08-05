package br.com.usacar.vendas.repository;

import br.com.usacar.vendas.model.VendaModel;
import br.com.usacar.vendas.rest.dto.MarcaRankingDTO;
import br.com.usacar.vendas.rest.dto.VendaRelatorioDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VendaRepository  extends JpaRepository<VendaModel, Integer> {
    Optional<VendaModel> findByDataVenda (LocalDate pDataVenda);

    Optional<VendaModel> findByClienteId (Integer pClienteVenda);

    Optional<VendaModel> findByVendedorId (Integer pVendedorId);

    List<VendaModel> findAllByClienteId(Integer clienteId);

    @Query("SELECT new br.com.usacar.vendas.rest.dto.VendaRelatorioDTO(" +
            "v.vendedor.nome, COUNT(v), SUM(v.valorVenda), SUM(v.valorComissao)) " +
            "FROM VendaModel v " +
            "WHERE v.dataVenda >= :dataInicio AND v.dataVenda <= :dataFim " +
            "GROUP BY v.vendedor.nome")
    List<VendaRelatorioDTO> gerarRelatorioVendasPorVendedor(
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim);

    @Query("SELECT new br.com.usacar.vendas.rest.dto.MarcaRankingDTO(" +
            "c.modelo.marca.nome, " +
            "COUNT(v.id), " +
            "SUM(v.valorVenda)) " +
            "FROM VendaModel v " +
            "JOIN v.carro c " +
            "WHERE c.status.descricao = 'Vendido' " + // Regra 1: Somente vendas efetivadas
            "AND (:dataInicio IS NULL OR v.dataVenda >= :dataInicio) " + // Regra 2: Filtro por dataInicio
            "AND (:dataFim IS NULL OR v.dataVenda <= :dataFim) " +     // Regra 2: Filtro por dataFim
            "GROUP BY c.modelo.marca.nome " +
            "ORDER BY COUNT(v.id) DESC, SUM(v.valorVenda) DESC") // Ordenar por quantidade e valor
    List<MarcaRankingDTO> gerarRankingMarcasMaisVendidas(
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim
    );


    boolean existsByCarro_Id(Integer carroId);


    boolean existsByCarro_IdAndDataVendaAfter(Integer carroId, LocalDate dataAComparar);
}