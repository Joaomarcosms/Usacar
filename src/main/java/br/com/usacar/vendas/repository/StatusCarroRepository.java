package br.com.usacar.vendas.repository;

import br.com.usacar.vendas.model.StatusCarroModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusCarroRepository  extends JpaRepository<StatusCarroModel, Integer> {
    List<StatusCarroModel> findByStatusCarro (String pStatusCarro);
}
