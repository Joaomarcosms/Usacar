package br.com.usacar.vendas.repository;

import br.com.usacar.vendas.model.ModeloModel;

import java.util.List;

public interface ModeloRepository {
    List<ModeloModel> findByNome (String pNome);


}
