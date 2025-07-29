package br.com.usacar.vendas.repository;

import br.com.usacar.vendas.rest.dto.CarroEstoqueDTO;
import br.com.usacar.vendas.rest.dto.CarroFiltroDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class CarroRepositoryImpl implements CarroRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<CarroEstoqueDTO> filtrarEstoque(CarroFiltroDTO filtro) {
        StringBuilder jpql = new StringBuilder("""
                    SELECT new br.com.usacar.vendas.rest.dto.CarroEstoqueDTO(
                        mar.nome,
                        mod.nome,
                        c.anoFabricacao,
                        c.anoModelo,
                        c.placa,
                        c.quilometragem,
                        c.precoCompra,
                        cor.nome,
                        status.descricao
                    )
                    FROM CarroModel c
                    JOIN ModeloModel mod ON c.modeloId = mod.id
                    JOIN MarcaModel mar ON mod.marcaId = mar.id
                    JOIN CorModel cor ON c.corId = cor.id
                    JOIN StatusCarroModel status ON c.statusId = status.id
                    WHERE 1=1
                """);

        if (filtro.getMarca() != null) {
            jpql.append(" AND LOWER(mar.nome) LIKE LOWER(CONCAT('%', :marca, '%')) ");
        }
        if (filtro.getModelo() != null) {
            jpql.append(" AND LOWER(mod.nome) LIKE LOWER(CONCAT('%', :modelo, '%')) ");
        }
        if (filtro.getAnoFabricacao() != null) {
            jpql.append(" AND c.anoFabricacao = :anoFabricacao ");
        }
        if (filtro.getCor() != null) {
            jpql.append(" AND LOWER(cor.nome) LIKE LOWER(CONCAT('%', :cor, '%')) ");
        }
        if (filtro.getKmMaxima() != null) {
            jpql.append(" AND c.quilometragem <= :kmMaxima ");
        }
        if (filtro.getStatus() != null) {
            jpql.append(" AND LOWER(status.nome) LIKE LOWER(CONCAT('%', :status, '%')) ");
        }

        TypedQuery<CarroEstoqueDTO> query = em.createQuery(jpql.toString(), CarroEstoqueDTO.class);

        if (filtro.getMarca() != null) query.setParameter("marca", filtro.getMarca());
        if (filtro.getModelo() != null) query.setParameter("modelo", filtro.getModelo());
        if (filtro.getAnoFabricacao() != null) query.setParameter("anoFabricacao", filtro.getAnoFabricacao());
        if (filtro.getCor() != null) query.setParameter("cor", filtro.getCor());
        if (filtro.getKmMaxima() != null) query.setParameter("kmMaxima", filtro.getKmMaxima());
        if (filtro.getStatus() != null) query.setParameter("status", filtro.getStatus());

        return query.getResultList();
    }
}
