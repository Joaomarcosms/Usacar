package br.com.usacar.vendas.repository;

import br.com.usacar.vendas.model.VendaModel;
import br.com.usacar.vendas.model.VendedorModel;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendedorRepository {
    Optional<VendedorModel> findByNome (String pNome);

    Optional<VendedorModel> findByCpfOrTelefoneOrEmail (String pCpf, String pTelefone, String pEmail);
}
