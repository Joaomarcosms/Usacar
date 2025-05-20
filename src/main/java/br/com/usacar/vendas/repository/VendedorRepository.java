package br.com.usacar.vendas.repository;

import br.com.usacar.vendas.model.VendaModel;
import br.com.usacar.vendas.model.VendedorModel;

import java.util.Optional;

public interface VendedorRepository {
    Optional<VendedorModel> findByNome (String pNome);

    Optional<VendedorModel> findByCpfOrTelefoneOrEmail (String pCpf, String pTelefone, String pEmail);
}
