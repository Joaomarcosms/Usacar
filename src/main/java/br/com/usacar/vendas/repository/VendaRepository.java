package br.com.usacar.vendas.repository;

import br.com.usacar.vendas.model.VendaModel;
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

    @Query("SELECT new br.com.usacar.vendas.rest.dto.VendaRelatorioDTO(" +
            "v.vendedor.nome, COUNT(v), SUM(v.valorVenda), SUM(v.valorComissao)) " +
            "FROM VendaModel v " +
            "WHERE v.dataVenda >= :dataInicio AND v.dataVenda <= :dataFim " +
            "GROUP BY v.vendedor.nome")
    List<VendaRelatorioDTO> gerarRelatorioVendasPorVendedor(
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim);
}
