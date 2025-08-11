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
public interface VendaRepository extends JpaRepository<VendaModel, Integer> {
    Optional<VendaModel> findByDataVenda(LocalDate pDataVenda);

    Optional<VendaModel> findByClienteId(Integer pClienteVenda);

    Optional<VendaModel> findByVendedorId(Integer pVendedorId);

    List<VendaModel> findAllByClienteId(Integer clienteId);

    // CORRIGIDO: Adicionado aliases na query JPQL para corresponder ao DTO
    @Query("SELECT new br.com.usacar.vendas.rest.dto.VendaRelatorioDTO(" +
            "v.vendedor.nome, " +
            "COUNT(v), " +
            "SUM(v.valorVenda), " +
            "SUM(v.valorComissao)) " +
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
            "WHERE c.status.descricao = 'Vendido' " +
            "AND (:dataInicio IS NULL OR v.dataVenda >= :dataInicio) " +
            "AND (:dataFim IS NULL OR v.dataVenda <= :dataFim) " +
            "GROUP BY c.modelo.marca.nome " +
            "ORDER BY COUNT(v.id) DESC, SUM(v.valorVenda) DESC")
    List<MarcaRankingDTO> gerarRankingMarcasMaisVendidas(
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim
    );


    boolean existsByCarro_Id(Integer carroId);


    boolean existsByCarro_IdAndDataVendaAfter(Integer carroId, LocalDate dataAComparar);

    @Query("SELECT v.vendedor.id, v.vendedor.nome, SUM(v.valorVenda), SUM(v.valorComissao) " +
            "FROM VendaModel v WHERE v.dataVenda BETWEEN :dataInicio AND :dataFim " +
            "GROUP BY v.vendedor.id, v.vendedor.nome " +
            "HAVING SUM(v.valorVenda) >= :valorMinimo")
    List<Object[]> findVendasTotaisParaReajuste(@Param("dataInicio") LocalDate dataInicio,
                                                @Param("dataFim") LocalDate dataFim,
                                                @Param("valorMinimo") Double valorMinimo);

    /**
     * Encontra todas as vendas de um vendedor em um período específico.
     */
    @Query("SELECT v FROM VendaModel v WHERE v.vendedor.id = :vendedorId AND v.dataVenda BETWEEN :dataInicio AND :dataFim")
    List<VendaModel> findVendasByVendedorIdAndPeriodo(@Param("vendedorId") Integer vendedorId,
                                                      @Param("dataInicio") LocalDate dataInicio,
                                                      @Param("dataFim") LocalDate dataFim);
}
