package br.com.usacar.vendas.repository;

import br.com.usacar.vendas.model.MarcaModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarcaRepository extends JpaRepository<MarcaModel, Integer> {
    List<MarcaModel> findByNome (String pNome);

    boolean existsByNome(@NotNull(message = "O campo é obrigatório") @NotBlank(message = "O campo não pode está vazio") String nome);
}
