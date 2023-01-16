/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.smuinvestimentos.provaestar.controllers;

import br.com.smuinvestimentos.provaestar.models.InvestorPfModel;
import br.com.smuinvestimentos.provaestar.models.pojo.InvestorPojo;
import br.com.smuinvestimentos.provaestar.services.AccountService;
import br.com.smuinvestimentos.provaestar.services.InvestorPfService;
import br.com.smuinvestimentos.provaestar.services.InvestorPjService;
import br.com.smuinvestimentos.provaestar.services.UtilService;
import jakarta.validation.Valid;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Charl
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/security")
public class SecurityController {

    private @Autowired
    UtilService servUtil;
    private @Autowired
    AccountService servAccount;
    private @Autowired
    InvestorPfService servInvestorPf;
    private @Autowired
    InvestorPjService servInvestorPj;

    @PutMapping("/status")
    public ResponseEntity<?> status(@Valid @RequestBody InvestorPojo investor) {
        ResponseEntity<?> resp;

        if (investor.getTypeInvestor().equalsIgnoreCase("PF")) {
            resp = servInvestorPf.changeStatus(investor.getCpf());

            return resp;
        } else if (investor.getTypeInvestor().equalsIgnoreCase("PJ")) {
            resp = servInvestorPj.changeStatus(investor.getCnpj());

            return resp;
        } else {
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "Tipo de investidor inválido! Deve ser 'PF' ou 'PJ'");
            return ResponseEntity.status(200).body(jsonResponse.toString());
        }
    }

}
