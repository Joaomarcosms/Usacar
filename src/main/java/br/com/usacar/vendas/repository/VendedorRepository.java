package br.com.usacar.vendas.repository;

import br.com.usacar.vendas.model.VendaModel;
import br.com.usacar.vendas.model.VendedorModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendedorRepository extends JpaRepository<VendedorModel, Integer> {
    Optional<VendedorModel> findByNome (String pNome);

    Optional<VendedorModel> findByCpfOrTelefoneOrEmail (String pCpf, String pTelefone, String pEmail);


    boolean existByCpf(@CPF @Length(min = 11, max = 11, message = "O CPF deve conter 11 caracteres") @NotNull(message = "O campo é obrigatório") @NotBlank(message = "O campo não pode está vazio") String cpf);
}
