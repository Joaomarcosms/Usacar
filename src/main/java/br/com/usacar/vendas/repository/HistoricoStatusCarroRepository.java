package br.com.usacar.vendas.repository;

import br.com.usacar.vendas.model.HistoricoStatusCarroModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricoStatusCarroRepository extends JpaRepository<HistoricoStatusCarroModel, Integer> {
    List<HistoricoStatusCarroModel> findAllByCarroIdOrderByDataHoraDesc(Integer carroId);
}
