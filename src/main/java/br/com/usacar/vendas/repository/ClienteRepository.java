package br.com.usacar.vendas.repository;

import br.com.usacar.vendas.model.ClienteModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<ClienteModel, Integer> {
    List<ClienteModel> findByNome(String pNome);
    Optional<ClienteModel> findByCpf(String pCpf);

    Optional<ClienteModel> findByEmail(String pEmail);

    Optional<ClienteModel> findByTelefone(String pTelefone);

}
