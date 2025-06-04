package br.com.usacar.vendas.repository;

import br.com.usacar.vendas.model.MarcaModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarcaRepository {
    List<MarcaModel> findByNome (String pNome);
}
