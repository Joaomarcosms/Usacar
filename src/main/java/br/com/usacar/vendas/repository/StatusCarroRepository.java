package br.com.usacar.vendas.repository;

import br.com.usacar.vendas.model.StatusCarroModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusCarroRepository extends JpaRepository<StatusCarroModel, Integer> {
    Optional<StatusCarroModel> findByDescricaoIgnoreCase(String descricao);
}

