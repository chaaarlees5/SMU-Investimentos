package br.com.smuinvestimentos.provaestar.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.smuinvestimentos.provaestar.models.InvestorPfModel;
import br.com.smuinvestimentos.provaestar.models.InvestorPjModel;
import br.com.smuinvestimentos.provaestar.services.InvestorPfService;
import br.com.smuinvestimentos.provaestar.services.InvestorPjService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@CrossOrigin("*")
@RequestMapping("/investor")
public class InvestorController {

    private @Autowired
    InvestorPfService servInvestorPf;
    private @Autowired
    InvestorPjService servInvestorPj;


    @PostMapping("/registerPf")
    public ResponseEntity<?> registerPf(@Valid @RequestBody InvestorPfModel investorPf) {
        ResponseEntity<?> resp;
        resp = servInvestorPf.register(investorPf);

        return resp;
    }

    @PostMapping("/registerPj")
    public ResponseEntity<?> registerPj(@Valid @RequestBody InvestorPjModel investorPj) {
        ResponseEntity<?> resp;
        resp = servInvestorPj.register(investorPj);

        return resp;
    }
}
