package br.com.smuinvestimentos.provaestar.models.pojo;

import lombok.Getter;

@Getter
public class OperationsPojo {

    private
    String document;

    private
    String documentSender;

    private
    String documentReceiver;

    private
    double value;

    private
    String typeInvestor;

    private
    String typeInvestorSender;

    private
    String typeInvestorReceiver;
}