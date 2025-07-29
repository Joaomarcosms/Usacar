package br.com.usacar.vendas.repository;

import br.com.usacar.vendas.rest.dto.CarroEstoqueDTO;
import br.com.usacar.vendas.rest.dto.CarroFiltroDTO;


import java.util.List;

public interface CarroRepositoryCustom {
    List<CarroEstoqueDTO> filtrarEstoque(CarroFiltroDTO filtro);
}
