package br.com.smuinvestimentos.provaestar.services;

import br.com.smuinvestimentos.provaestar.models.InvestorPfModel;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import br.com.smuinvestimentos.provaestar.models.AccountModel;
import br.com.smuinvestimentos.provaestar.models.InvestorPjModel;
import br.com.smuinvestimentos.provaestar.repositorys.AccountRepository;
import br.com.smuinvestimentos.provaestar.repositorys.InvestorPjRepository;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

@Service
public class InvestorPjService {

    @Autowired
    private InvestorPjRepository repInvestorPj;

    @Autowired
    private AccountRepository repAccount;

    @Autowired
    private UtilService servUtil;

    public ResponseEntity<?> register(InvestorPjModel investorPj) {
        JSONObject jsonResponse;
        if (repInvestorPj.existsByCnpj(investorPj.getCnpj())) {
            jsonResponse = new JSONObject();
            jsonResponse.put("message", "CPF já cadastrado!");
            return ResponseEntity.status(409).body(jsonResponse.toString());
        }

        if (repAccount.existsByNumberAccount(investorPj.getAccount().getNumberAccount())) {
            jsonResponse = new JSONObject();
            jsonResponse.put("message", "Agência/Conta já cadastrada!");
            return ResponseEntity.status(409).body(jsonResponse.toString());
        }

        if (servUtil.existsEmail(investorPj.getEmail())) {
            jsonResponse = new JSONObject();
            jsonResponse.put("message", "Email já cadastrado!");
            return ResponseEntity.status(409).body(jsonResponse.toString());
        }
        
        repInvestorPj.save(investorPj);

        jsonResponse = new JSONObject();
        jsonResponse.put("message", "Cadastro do investidor realizado com sucesso!");
        return ResponseEntity.status(201).body(jsonResponse.toString());
    }

    public ResponseEntity<?> changeStatus(String cnpj) {
        JSONObject jsonResponse;
        Optional<InvestorPjModel> investorPj;
        String newStatus;

        if (cnpj == null) {
            jsonResponse = new JSONObject();
            jsonResponse.put("message", "CNPJ do investidor não informado!");
            return ResponseEntity.status(409).body(jsonResponse.toString());
        }

        if (!repInvestorPj.existsByCnpj(cnpj)) {
            jsonResponse = new JSONObject();
            jsonResponse.put("message", "CNPJ não cadastrado no sistema!");
            return ResponseEntity.status(409).body(jsonResponse.toString());
        }

        investorPj = repInvestorPj.findByCnpj(cnpj);
        investorPj.get().setActive(!investorPj.get().isActive());

        repInvestorPj.save(investorPj.get());

        newStatus = investorPj.get().isActive() ? "ativo" : "inativo";

        jsonResponse = new JSONObject();
        jsonResponse.put("message", "Status do investidor alterado para " + newStatus + " com sucesso!");
        return ResponseEntity.status(200).body(jsonResponse.toString());
    }

    public boolean validateInvestor(String cnpj) {
        Optional<InvestorPjModel> investorPj;

        if (!repInvestorPj.existsByCnpj(cnpj)) {
            return false;
        }

        investorPj = repInvestorPj.findByCnpj(cnpj);
        return investorPj.get().isActive();
    }
}
