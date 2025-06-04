package br.com.usacar.vendas.repository;

import br.com.usacar.vendas.model.StatusCarroModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusCarroRepository {
    List<StatusCarroModel> findByStatusCarro (String pStatusCarro);
}
