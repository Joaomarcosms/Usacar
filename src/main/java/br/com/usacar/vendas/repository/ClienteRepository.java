package br.com.usacar.vendas.repository;

import br.com.usacar.vendas.model.ClienteModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteModel, Integer> {
    List<ClienteModel> findByNome(String pNome);
    Optional<ClienteModel> findByCpf(String pCpf);

    Optional<ClienteModel> findByEmail(String pEmail);

    Optional<ClienteModel> findByTelefone(String pTelefone);

    boolean existsByCpf(@CPF @Length(min = 11, max = 11, message = "O cpf deve conter 11 caracteres")
                        @NotNull(message = "O campo tem que ser preenchido obrigatório.")
                        @NotBlank(message = "O campo não poderá ficar vazio") String cpf);
}
