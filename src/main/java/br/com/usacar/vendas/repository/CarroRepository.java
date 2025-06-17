package br.com.usacar.vendas.repository;

import br.com.usacar.vendas.model.CarroModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CarroRepository extends JpaRepository<CarroModel, Integer> {
    Optional<CarroModel> findByAnoFabricacao(int pAnoFabricacao);

    Optional<CarroModel> findByPlaca(String pPlaca);

    List<CarroModel> findByPrecoCompra(Double pPrecoCompra);

    List<CarroModel> findByModeloId(Integer pModeloId);

    boolean existsByPlaca(String pPlaca);
}
