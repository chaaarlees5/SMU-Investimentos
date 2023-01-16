package br.com.smuinvestimentos.provaestar.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.smuinvestimentos.provaestar.models.AccountModel;
import br.com.smuinvestimentos.provaestar.models.ExtractModel;
import java.util.List;

public interface ExtractRepository extends JpaRepository<ExtractModel, Long> {
    
    List<ExtractModel> findByAccount(AccountModel account);
}
