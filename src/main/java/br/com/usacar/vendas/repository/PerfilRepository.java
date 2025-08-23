package br.com.usacar.vendas.repository;

import br.com.usacar.vendas.model.PerfilModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PerfilRepository extends JpaRepository<PerfilModel, Integer> {
    Optional<PerfilModel> findByNomeIgnoreCase(String nome);
}
