package br.com.usacar.vendas.repository;

import br.com.usacar.vendas.model.CorModel;

import java.util.List;

public interface CorRepository {
    List<CorModel> findByNome (String pNome);
}
