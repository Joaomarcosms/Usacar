package br.com.usacar.vendas.repository;

import br.com.usacar.vendas.model.ModeloModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModeloRepository extends JpaRepository<ModeloModel, Integer> {
    Optional<ModeloModel> findByNome(String pNome);

    boolean existsByNome(String pNome);

    List<ModeloModel> findAllByMarcaId(Integer marcaId);

    @Query("SELECT m FROM ModeloModel m JOIN FETCH m.marca")
    List<ModeloModel> findAllWithMarca();
}
