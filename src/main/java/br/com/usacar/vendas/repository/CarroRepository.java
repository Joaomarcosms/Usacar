package br.com.usacar.vendas.repository;

import br.com.usacar.vendas.model.CarroModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CarroRepository extends JpaRepository<CarroModel, Integer> {
    Optional<CarroModel> findByAnoFabricacao(LocalDate pAnoFabricacao);

    Optional<CarroModel> findByPlaca(String pPlaca);

    List<CarroModel> findByPrecoCompra(Double pPrecoCompra);

    List<CarroModel> findByModeloId(Integer pModeloId);

}
