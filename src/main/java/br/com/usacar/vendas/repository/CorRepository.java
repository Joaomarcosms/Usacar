package br.com.usacar.vendas.repository;

import br.com.usacar.vendas.model.CorModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CorRepository {
    List<CorModel> findByNome (String pNome);
}
