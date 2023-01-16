package br.com.smuinvestimentos.provaestar.repositorys;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.smuinvestimentos.provaestar.models.InvestorPjModel;

public interface InvestorPjRepository extends JpaRepository<InvestorPjModel, Long> {
    
    Optional<InvestorPjModel> findByCnpj(String cnpj);
    
    boolean existsByEmail(String email);    
    boolean existsByCnpj(String cnpj);    
}
