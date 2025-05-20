package br.com.usacar.vendas.repository;

import br.com.usacar.vendas.model.StatusCarroModel;

import java.util.List;

public interface StatusCarroRepository {
    List<StatusCarroModel> findByStatusCarro (String pStatusCarro);
}
