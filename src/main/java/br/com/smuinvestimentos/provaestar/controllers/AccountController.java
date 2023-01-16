/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.smuinvestimentos.provaestar.controllers;

import br.com.smuinvestimentos.provaestar.models.pojo.OperationsPojo;
import br.com.smuinvestimentos.provaestar.services.AccountService;
import jakarta.validation.Valid;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/account")
public class AccountController {
    
    private @Autowired
    AccountService servAccount;
    
    @PutMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody OperationsPojo operations) {
        ResponseEntity<?> resp;
        
        resp = servAccount.withdraw(operations.getDocument(), operations.getValue(), operations.getTypeInvestor());
        return resp;
    }
    
    @PutMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody OperationsPojo operations) {
        ResponseEntity<?> resp;
        
        resp = servAccount.deposit(operations.getDocument(), operations.getValue(), operations.getTypeInvestor());
        return resp;
    }
    
    @PutMapping("/extract")
    public ResponseEntity<?> extract(@RequestBody OperationsPojo operations) {
        ResponseEntity<?> resp;
        
        resp = servAccount.extract(operations.getDocument(), operations.getTypeInvestor());
        return resp;
    }
    
    @PutMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody OperationsPojo operations) {
        ResponseEntity<?> resp;
        
        resp = servAccount.transfer(operations.getDocumentSender(), operations.getTypeInvestorSender()
                , operations.getDocumentReceiver(), operations.getTypeInvestorReceiver()
                , operations.getValue());
        return resp;
    }
}
