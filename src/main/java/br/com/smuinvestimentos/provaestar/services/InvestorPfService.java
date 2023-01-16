package br.com.smuinvestimentos.provaestar.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import br.com.smuinvestimentos.provaestar.models.AccountModel;
import br.com.smuinvestimentos.provaestar.models.InvestorPfModel;
import br.com.smuinvestimentos.provaestar.repositorys.AccountRepository;
import br.com.smuinvestimentos.provaestar.repositorys.InvestorPfRepository;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

@Service
public class InvestorPfService {

    @Autowired
    private InvestorPfRepository repInvestorPf;

    @Autowired
    private AccountRepository repAccount;

    @Autowired
    private UtilService servUtil;

    public ResponseEntity<?> register(InvestorPfModel investorPf) {
        JSONObject jsonResponse;
        if (repInvestorPf.existsByCpf(investorPf.getCpf())) {
            jsonResponse = new JSONObject();
            jsonResponse.put("message", "CPF já cadastrado!");
            return ResponseEntity.status(409).body(jsonResponse.toString());
        }

        if (repAccount.existsByNumberAccount(investorPf.getAccount().getNumberAccount())) {
            jsonResponse = new JSONObject();
            jsonResponse.put("message", "Agência/Conta já cadastrada!");
            return ResponseEntity.status(409).body(jsonResponse.toString());
        }

        if (servUtil.existsEmail(investorPf.getEmail())) {
            jsonResponse = new JSONObject();
            jsonResponse.put("message", "Email já cadastrado!");
            return ResponseEntity.status(409).body(jsonResponse.toString());
        }

        repInvestorPf.save(investorPf);
        
        jsonResponse = new JSONObject();
        jsonResponse.put("message", "Cadastro do investidor realizado com sucesso!");
        return ResponseEntity.status(201).body(jsonResponse.toString());
    }
    
    public ResponseEntity<?> changeStatus(String cpf) {
        JSONObject jsonResponse;
        Optional<InvestorPfModel> investorPf;
        String newStatus;
                
        if (cpf == null) {
            jsonResponse = new JSONObject();
            jsonResponse.put("message", "CPF do investidor não informado!");
            return ResponseEntity.status(409).body(jsonResponse.toString());
        }
        
        if (!repInvestorPf.existsByCpf(cpf)) {
            jsonResponse = new JSONObject();
            jsonResponse.put("message", "CPF não cadastrado no sistema!");
            return ResponseEntity.status(409).body(jsonResponse.toString());
        }
        
        investorPf = repInvestorPf.findByCpf(cpf);
        investorPf.get().setActive(!investorPf.get().isActive());
        
        repInvestorPf.save(investorPf.get());
        
        newStatus = investorPf.get().isActive() ? "ativo" : "inativo";
        
        jsonResponse = new JSONObject();
        jsonResponse.put("message", "Status do investidor alterado para " + newStatus + " com sucesso!");
        return ResponseEntity.status(200).body(jsonResponse.toString());
    }

    public boolean validateInvestor(String cpf) {
        Optional<InvestorPfModel> investorPf;
                
        if (!repInvestorPf.existsByCpf(cpf)) {
            return false;
        }
        
        investorPf = repInvestorPf.findByCpf(cpf);
        return investorPf.get().isActive();
    }
}
