package br.com.usacar.vendas.repository;

import br.com.usacar.vendas.model.CorModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CorRepository extends JpaRepository<CorModel, Integer> {
    List<CorModel> findByNome (String pNome);

    boolean existsByNome(String pNome);
}
