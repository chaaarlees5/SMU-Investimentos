package br.com.smuinvestimentos.provaestar.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.smuinvestimentos.provaestar.models.AccountModel;
import br.com.smuinvestimentos.provaestar.models.InvestorPfModel;
import br.com.smuinvestimentos.provaestar.repositorys.AccountRepository;
import br.com.smuinvestimentos.provaestar.repositorys.InvestorPfRepository;
import br.com.smuinvestimentos.provaestar.repositorys.InvestorPjRepository;

@Service
public class UtilService {

    @Autowired
    private AccountRepository repAccount;
    @Autowired
    private InvestorPfRepository repInvestorPf;
    @Autowired
    private InvestorPjRepository repInvestorPj;

    public boolean existsEmail(String email) {
        return repInvestorPf.existsByEmail(email) || repInvestorPj.existsByEmail(email);
    }

}
