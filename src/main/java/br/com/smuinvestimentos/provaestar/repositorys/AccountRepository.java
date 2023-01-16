package br.com.smuinvestimentos.provaestar.repositorys;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.smuinvestimentos.provaestar.models.AccountModel;

public interface AccountRepository extends JpaRepository<AccountModel, Long> {
    
    Optional<AccountModel> findByNumberAccount(String numberAccount);
    
    boolean existsByNumberAccountAndAgency(String account, String agency);
    boolean existsByNumberAccount(String account);
}
