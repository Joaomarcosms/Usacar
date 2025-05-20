package br.com.usacar.vendas.repository;

import br.com.usacar.vendas.model.MarcaModel;

import java.util.List;

public interface MarcaRepository {
    List<MarcaModel> findByNome (String pNome);
}
