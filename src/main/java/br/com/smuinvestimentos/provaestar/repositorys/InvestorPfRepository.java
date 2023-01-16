package br.com.smuinvestimentos.provaestar.repositorys;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.smuinvestimentos.provaestar.models.InvestorPfModel;

public interface InvestorPfRepository extends JpaRepository<InvestorPfModel, Long> {

    Optional<InvestorPfModel> findByCpf(String cpf);

    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
}
