/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.smuinvestimentos.provaestar.exceptionhandler;

/**
 *
 * @author charlô
 */
public class MessageExceptionHandler {
    
    private String message;

    public MessageExceptionHandler(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }    
}
