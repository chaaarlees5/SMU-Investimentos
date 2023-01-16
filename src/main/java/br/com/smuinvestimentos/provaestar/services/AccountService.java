package br.com.smuinvestimentos.provaestar.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.smuinvestimentos.provaestar.models.AccountModel;
import br.com.smuinvestimentos.provaestar.models.ExtractModel;
import br.com.smuinvestimentos.provaestar.models.InvestorPfModel;
import br.com.smuinvestimentos.provaestar.models.InvestorPjModel;
import br.com.smuinvestimentos.provaestar.repositorys.AccountRepository;
import br.com.smuinvestimentos.provaestar.repositorys.ExtractRepository;
import br.com.smuinvestimentos.provaestar.repositorys.InvestorPfRepository;
import br.com.smuinvestimentos.provaestar.repositorys.InvestorPjRepository;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private InvestorPjRepository repInvestorPj;
    @Autowired
    private InvestorPfRepository repInvestorPf;
    @Autowired
    private InvestorPfService servInvestorPf;
    @Autowired
    private InvestorPjService servInvestorPj;
    @Autowired
    private AccountRepository repAccount;
    @Autowired
    private ExtractRepository repExtract;
    @Autowired
    private UtilService servUtil;

    public boolean existsAccount(AccountModel account) {
        return accountRepository.existsByNumberAccountAndAgency(account.getNumberAccount(), account.getAgency());
    }

    public ResponseEntity<?> withdraw(String document, double value, String typeInvestor) {
        JSONObject jsonResponse;
        Optional<InvestorPfModel> investorPf;
        Optional<InvestorPjModel> investorPj;
        ExtractModel extract;
        double minWithdraw = 2.00;

        if (typeInvestor == null) {
            jsonResponse = new JSONObject();
            jsonResponse.put("message", "Tipo de investidor não informado!");
            return ResponseEntity.status(400).body(jsonResponse.toString());
        }

        if (document == null) {
            jsonResponse = new JSONObject();
            jsonResponse.put("message", "Número do documento(CPF/CNPJ) não informado!");
            return ResponseEntity.status(400).body(jsonResponse.toString());
        }

        if (value <= minWithdraw) {
            jsonResponse = new JSONObject();
            jsonResponse.put("message", "Valor do saque abaixo do permitido (min. R$" + minWithdraw + ")!");
            return ResponseEntity.status(400).body(jsonResponse.toString());
        }

        if (typeInvestor.equalsIgnoreCase("PF")) {
            if (!servInvestorPf.validateInvestor(document)) {
                jsonResponse = new JSONObject();
                jsonResponse.put("message", "CPF não cadastrado ou inativo!");
                return ResponseEntity.status(404).body(jsonResponse.toString());
            }

            investorPf = repInvestorPf.findByCpf(document);

            if (investorPf.get().getAccount().getBalance() < value) {
                jsonResponse = new JSONObject();
                jsonResponse.put("message", "O valor do saque é maior do que o valor existente na conta!");
                return ResponseEntity.status(400).body(jsonResponse.toString());
            }

            extract = new ExtractModel();
            extract.setOldBalance(investorPf.get().getAccount().getBalance());

            investorPf.get().getAccount().setBalance(investorPf.get().getAccount().getBalance() - value);
            repInvestorPf.save(investorPf.get());

            extract.setAccount(investorPf.get().getAccount());
            extract.setAdditionalValue(-value);
            extract.setNewBalance(investorPf.get().getAccount().getBalance());
            extract.setOperation("Saque");

            repExtract.save(extract);

            jsonResponse = new JSONObject();
            jsonResponse.put("message", "Saque realizado com sucesso!");
            jsonResponse.put("newBalance", investorPf.get().getAccount().getBalance());
            return ResponseEntity.status(200).body(jsonResponse.toString());
        } else if (typeInvestor.equalsIgnoreCase("PJ")) {
            if (!servInvestorPj.validateInvestor(document)) {
                jsonResponse = new JSONObject();
                jsonResponse.put("message", "CNPJ não cadastrado ou inativo!");
                return ResponseEntity.status(404).body(jsonResponse.toString());
            }

            investorPj = repInvestorPj.findByCnpj(document);

            if (investorPj.get().getAccount().getBalance() < value) {
                jsonResponse = new JSONObject();
                jsonResponse.put("message", "O valor do saque é maior do que o valor existente na conta!");
                return ResponseEntity.status(400).body(jsonResponse.toString());
            }

            extract = new ExtractModel();
            extract.setOldBalance(investorPj.get().getAccount().getBalance());

            investorPj.get().getAccount().setBalance(investorPj.get().getAccount().getBalance() - value);
            repInvestorPj.save(investorPj.get());

            extract.setAccount(investorPj.get().getAccount());
            extract.setAdditionalValue(-value);
            extract.setNewBalance(investorPj.get().getAccount().getBalance());
            extract.setOperation("Saque");

            repExtract.save(extract);

            jsonResponse = new JSONObject();
            jsonResponse.put("message", "Saque realizado com sucesso!");
            jsonResponse.put("newBalance", investorPj.get().getAccount().getBalance());
            return ResponseEntity.status(200).body(jsonResponse.toString());
        } else {
            jsonResponse = new JSONObject();
            jsonResponse.put("message", "Tipo de investidor inválido! Deve ser 'PF' ou 'PJ'");
            return ResponseEntity.status(400).body(jsonResponse.toString());
        }
    }

    public ResponseEntity<?> deposit(String document, double value, String typeInvestor) {
        JSONObject jsonResponse;
        Optional<InvestorPfModel> investorPf;
        Optional<InvestorPjModel> investorPj;
        ExtractModel extract;
        double minDeposit = 5.00;

        if (typeInvestor == null) {
            jsonResponse = new JSONObject();
            jsonResponse.put("message", "Tipo de investidor não informado!");
            return ResponseEntity.status(400).body(jsonResponse.toString());
        }

        if (document == null) {
            jsonResponse = new JSONObject();
            jsonResponse.put("message", "Número do documento(CPF/CNPJ) não informado!");
            return ResponseEntity.status(400).body(jsonResponse.toString());
        }

        if (value < minDeposit) {
            jsonResponse = new JSONObject();
            jsonResponse.put("message", "Valor do depósito abaixo do permitido (min. R$" + minDeposit + ")!");
            return ResponseEntity.status(400).body(jsonResponse.toString());
        }

        if (typeInvestor.equalsIgnoreCase("PF")) {
            if (!servInvestorPf.validateInvestor(document)) {
                jsonResponse = new JSONObject();
                jsonResponse.put("message", "CPF não cadastrado ou inativo!");
                return ResponseEntity.status(404).body(jsonResponse.toString());
            }

            investorPf = repInvestorPf.findByCpf(document);

            extract = new ExtractModel();
            extract.setOldBalance(investorPf.get().getAccount().getBalance());

            investorPf.get().getAccount().setBalance(investorPf.get().getAccount().getBalance() + value);
            repInvestorPf.save(investorPf.get());

            extract.setAccount(investorPf.get().getAccount());
            extract.setAdditionalValue(value);
            extract.setNewBalance(investorPf.get().getAccount().getBalance());
            extract.setOperation("Depósito");

            repExtract.save(extract);

            jsonResponse = new JSONObject();
            jsonResponse.put("message", "Depósito realizado com sucesso!");
            jsonResponse.put("newBalance", investorPf.get().getAccount().getBalance());
            return ResponseEntity.status(200).body(jsonResponse.toString());
        } else if (typeInvestor.equalsIgnoreCase("PJ")) {
            if (!servInvestorPj.validateInvestor(document)) {
                jsonResponse = new JSONObject();
                jsonResponse.put("message", "CNPJ não cadastrado ou inativo!");
                return ResponseEntity.status(404).body(jsonResponse.toString());
            }

            investorPj = repInvestorPj.findByCnpj(document);

            extract = new ExtractModel();
            extract.setOldBalance(investorPj.get().getAccount().getBalance());

            investorPj.get().getAccount().setBalance(investorPj.get().getAccount().getBalance() + value);
            repInvestorPj.save(investorPj.get());

            extract.setAccount(investorPj.get().getAccount());
            extract.setAdditionalValue(value);
            extract.setNewBalance(investorPj.get().getAccount().getBalance());
            extract.setOperation("Depósito");

            repExtract.save(extract);

            jsonResponse = new JSONObject();
            jsonResponse.put("message", "Depósito realizado com sucesso!");
            jsonResponse.put("newBalance", investorPj.get().getAccount().getBalance());
            return ResponseEntity.status(200).body(jsonResponse.toString());
        } else {
            jsonResponse = new JSONObject();
            jsonResponse.put("message", "Tipo de investidor inválido! Deve ser 'PF' ou 'PJ'");
            return ResponseEntity.status(400).body(jsonResponse.toString());
        }
    }

    public ResponseEntity<?> extract(String document, String typeInvestor) {
        JSONObject jsonResponse;
        JSONArray arrayExtract;
        JSONObject jsonExtract;
        Optional<InvestorPfModel> investorPf;
        Optional<InvestorPjModel> investorPj;
        List<ExtractModel> extractList;

        if (typeInvestor == null) {
            jsonResponse = new JSONObject();
            jsonResponse.put("message", "Tipo de investidor não informado!");
            return ResponseEntity.status(400).body(jsonResponse.toString());
        }

        if (document == null) {
            jsonResponse = new JSONObject();
            jsonResponse.put("message", "Número do documento(CPF/CNPJ) não informado!");
            return ResponseEntity.status(400).body(jsonResponse.toString());
        }

        if (typeInvestor.equalsIgnoreCase("PF")) {
            if (!servInvestorPf.validateInvestor(document)) {
                jsonResponse = new JSONObject();
                jsonResponse.put("message", "CPF não cadastrado ou inativo!");
                return ResponseEntity.status(404).body(jsonResponse.toString());
            }

            investorPf = repInvestorPf.findByCpf(document);
            extractList = repExtract.findByAccount(investorPf.get().getAccount());

            arrayExtract = new JSONArray();
            for (ExtractModel extract : extractList) {
                jsonExtract = new JSONObject();
                jsonExtract.put("idExtract", extract.getIdExtract());
                jsonExtract.put("idAccount", extract.getAccount().getIdAccount());
                jsonExtract.put("additionalValue", extract.getAdditionalValue());
                jsonExtract.put("operation", extract.getOperation());
                jsonExtract.put("newBalance", extract.getNewBalance());
                jsonExtract.put("oldBalance", extract.getOldBalance());
                arrayExtract.put(jsonExtract);
            }

            jsonResponse = new JSONObject();
            jsonResponse.put("message", "Extrato capturado com sucesso!");
            jsonResponse.put("extract", arrayExtract);
            return ResponseEntity.status(200).body(jsonResponse.toString());
        } else if (typeInvestor.equalsIgnoreCase("PJ")) {
            if (!servInvestorPj.validateInvestor(document)) {
                jsonResponse = new JSONObject();
                jsonResponse.put("message", "CNPJ não cadastrado ou inativo!");
                return ResponseEntity.status(404).body(jsonResponse.toString());
            }

            investorPj = repInvestorPj.findByCnpj(document);

            extractList = repExtract.findByAccount(investorPj.get().getAccount());
            
            arrayExtract = new JSONArray();
            for (ExtractModel extract : extractList) {
                jsonExtract = new JSONObject();
                jsonExtract.put("idExtract", extract.getIdExtract());
                jsonExtract.put("idAccount", extract.getAccount().getIdAccount());
                jsonExtract.put("additionalValue", extract.getAdditionalValue());
                jsonExtract.put("operation", extract.getOperation());
                jsonExtract.put("newBalance", extract.getNewBalance());
                jsonExtract.put("oldBalance", extract.getOldBalance());
                arrayExtract.put(jsonExtract);
            }

            jsonResponse = new JSONObject();
            jsonResponse.put("message", "Extrato capturado com sucesso!");
            jsonResponse.put("extract", arrayExtract);
            return ResponseEntity.status(200).body(jsonResponse.toString());
        } else {
            jsonResponse = new JSONObject();
            jsonResponse.put("message", "Tipo de investidor inválido! Deve ser 'PF' ou 'PJ'");
            return ResponseEntity.status(400).body(jsonResponse.toString());
        }
    }

    public ResponseEntity<?> transfer(String documentSender, String typeInvestorSender, String documentReceiver, String typeInvestorReceiver, double value) {
        JSONObject jsonResponse;
        double minTransfer = 2.00;
        double newBalanceSender;
        double newBalanceReceiver;
        ExtractModel extractSender = null;
        ExtractModel extractReceiver = null;
        Optional<InvestorPfModel> investorPfSender = null;
        Optional<InvestorPjModel> investorPjSender = null;
        Optional<InvestorPfModel> investorPfReceiver = null;
        Optional<InvestorPjModel> investorPjReceiver = null;

        if (documentSender == null) {
            jsonResponse = new JSONObject();
            jsonResponse.put("message", "Número do documento(CPF/CNPJ) do emissor não informado!");
            return ResponseEntity.status(400).body(jsonResponse.toString());
        }

        if (documentReceiver == null) {
            jsonResponse = new JSONObject();
            jsonResponse.put("message", "Número do documento(CPF/CNPJ) do receptor não informado!");
            return ResponseEntity.status(400).body(jsonResponse.toString());
        }

        if (typeInvestorSender == null) {
            jsonResponse = new JSONObject();
            jsonResponse.put("message", "Tipo de investidor emissor não informado!");
            return ResponseEntity.status(400).body(jsonResponse.toString());
        }

        if (typeInvestorReceiver == null) {
            jsonResponse = new JSONObject();
            jsonResponse.put("message", "Tipo de investidor receptor não informado!");
            return ResponseEntity.status(400).body(jsonResponse.toString());
        }

        if (value < minTransfer) {
            jsonResponse = new JSONObject();
            jsonResponse.put("message", "Valor da transferência abaixo do permitido (min. R$" + minTransfer + ")!");
            return ResponseEntity.status(400).body(jsonResponse.toString());
        }

        if (typeInvestorSender.equalsIgnoreCase("PF")) {
            if (!servInvestorPf.validateInvestor(documentSender)) {
                jsonResponse = new JSONObject();
                jsonResponse.put("message", "CPF do emissor não cadastrado ou inativo!");
                return ResponseEntity.status(404).body(jsonResponse.toString());
            }

            investorPfSender = repInvestorPf.findByCpf(documentSender);
            if (investorPfSender.get().getAccount().getBalance() < value) {
                jsonResponse = new JSONObject();
                jsonResponse.put("message", "O valor a ser tranferido é maior do que o valor existente na conta!");
                return ResponseEntity.status(400).body(jsonResponse.toString());
            }

            extractSender = new ExtractModel();
            extractSender.setOldBalance(investorPfSender.get().getAccount().getBalance());

            investorPfSender.get().getAccount().setBalance(investorPfSender.get().getAccount().getBalance() - value);

            extractSender.setAccount(investorPfSender.get().getAccount());
            extractSender.setAdditionalValue(-value);
            extractSender.setNewBalance(investorPfSender.get().getAccount().getBalance());
            extractSender.setOperation("Transferência");
        } else if (typeInvestorSender.equalsIgnoreCase("PJ")) {
            if (!servInvestorPj.validateInvestor(documentSender)) {
                jsonResponse = new JSONObject();
                jsonResponse.put("message", "CPF do emissor não cadastrado ou inativo!");
                return ResponseEntity.status(404).body(jsonResponse.toString());
            }

            investorPjSender = repInvestorPj.findByCnpj(documentSender);
            if (investorPjSender.get().getAccount().getBalance() < value) {
                jsonResponse = new JSONObject();
                jsonResponse.put("message", "O valor a ser tranferido é maior do que o valor existente na conta!");
                return ResponseEntity.status(400).body(jsonResponse.toString());
            }

            extractSender = new ExtractModel();
            extractSender.setOldBalance(investorPjSender.get().getAccount().getBalance());

            investorPjSender.get().getAccount().setBalance(investorPjSender.get().getAccount().getBalance() - value);

            extractSender.setAccount(investorPjSender.get().getAccount());
            extractSender.setAdditionalValue(-value);
            extractSender.setNewBalance(investorPjSender.get().getAccount().getBalance());
            extractSender.setOperation("Transferência");
        } else {
            jsonResponse = new JSONObject();
            jsonResponse.put("message", "Tipo de investidor emissor inválido! Deve ser 'PF' ou 'PJ'");
            return ResponseEntity.status(400).body(jsonResponse.toString());
        }

        if (typeInvestorReceiver.equalsIgnoreCase("PF")) {
            if (!servInvestorPf.validateInvestor(documentReceiver)) {
                jsonResponse = new JSONObject();
                jsonResponse.put("message", "CPF do receptor não cadastrado ou inativo!");
                return ResponseEntity.status(404).body(jsonResponse.toString());
            }

            investorPfReceiver = repInvestorPf.findByCpf(documentReceiver);

            extractReceiver = new ExtractModel();
            extractReceiver.setOldBalance(investorPfReceiver.get().getAccount().getBalance());

            investorPfReceiver.get().getAccount().setBalance(investorPfReceiver.get().getAccount().getBalance() + value);

            extractReceiver.setAccount(investorPfReceiver.get().getAccount());
            extractReceiver.setAdditionalValue(value);
            extractReceiver.setNewBalance(investorPfReceiver.get().getAccount().getBalance());
            extractReceiver.setOperation("Transferência");
        } else if (typeInvestorReceiver.equalsIgnoreCase("PJ")) {
            if (!servInvestorPj.validateInvestor(documentReceiver)) {
                jsonResponse = new JSONObject();
                jsonResponse.put("message", "CPj do receptor não cadastrado ou inativo!");
                return ResponseEntity.status(404).body(jsonResponse.toString());
            }

            investorPjReceiver = repInvestorPj.findByCnpj(documentReceiver);

            extractReceiver = new ExtractModel();
            extractReceiver.setOldBalance(investorPjReceiver.get().getAccount().getBalance());

            investorPjReceiver.get().getAccount().setBalance(investorPjReceiver.get().getAccount().getBalance() + value);

            extractReceiver.setAccount(investorPjReceiver.get().getAccount());
            extractReceiver.setAdditionalValue(value);
            extractReceiver.setNewBalance(investorPjReceiver.get().getAccount().getBalance());
            extractReceiver.setOperation("Transferência");
        } else {
            jsonResponse = new JSONObject();
            jsonResponse.put("message", "Tipo de investidor receptor inválido! Deve ser 'PF' ou 'PJ'");
            return ResponseEntity.status(400).body(jsonResponse.toString());
        }

        if (typeInvestorSender.equalsIgnoreCase("PF")) {
            repInvestorPf.save(investorPfSender.get());
            newBalanceSender = investorPfSender.get().getAccount().getBalance();
        } else {
            repInvestorPj.save(investorPjSender.get());
            newBalanceSender = investorPjSender.get().getAccount().getBalance();
        }

        if (typeInvestorReceiver.equalsIgnoreCase("PF")) {
            repInvestorPf.save(investorPfReceiver.get());
            newBalanceReceiver = investorPfReceiver.get().getAccount().getBalance();
        } else {
            repInvestorPj.save(investorPjReceiver.get());
            newBalanceReceiver = investorPjReceiver.get().getAccount().getBalance();
        }

        repExtract.save(extractSender);
        repExtract.save(extractReceiver);

        jsonResponse = new JSONObject();
        jsonResponse.put("message", "Tranferência realizada com sucesso!");
        jsonResponse.put("newBalanceSender", newBalanceSender);
        jsonResponse.put("newBalanceReceiver", newBalanceReceiver);
        return ResponseEntity.status(200).body(jsonResponse.toString());
    }
}
