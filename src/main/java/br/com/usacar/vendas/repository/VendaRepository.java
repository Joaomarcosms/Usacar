package br.com.usacar.vendas.repository;

import br.com.usacar.vendas.model.VendaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface VendaRepository  extends JpaRepository<VendaModel, Integer> {
    Optional<VendaModel> findByDataVenda (LocalDate pDataVenda);

    Optional<VendaModel> findByClienteId (Integer pClienteVenda);

    Optional<VendaModel> findByVendedorId (Integer pVendedorId);
}
