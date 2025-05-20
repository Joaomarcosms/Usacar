package br.com.usacar.vendas.repository;

import br.com.usacar.vendas.model.VendaModel;

import java.time.LocalDate;
import java.util.Optional;

public interface VendaRepository {
    Optional<VendaModel> findByDataVenda (LocalDate pDataVenda);

    Optional<VendaModel> findByClienteId (Integer pClienteVenda);

    Optional<VendaModel> findByVendedorId (Integer pVendedorId);
}
