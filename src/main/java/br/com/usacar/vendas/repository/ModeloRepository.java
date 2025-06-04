package br.com.usacar.vendas.repository;

import br.com.usacar.vendas.model.ModeloModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModeloRepository {
    List<ModeloModel> findByNome (String pNome);


}
