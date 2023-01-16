package br.com.smuinvestimentos.provaestar.models.pojo;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

import lombok.Getter;
import org.hibernate.validator.constraints.br.CNPJ;

@Getter
public class InvestorPojo {

    private
    @CPF(message = "CPF inválido!")
    String cpf;

    private
    @CNPJ(message = "CNPJ inválido!")
    String cnpj;

    private
    @NotNull(message = "Tipo de investidor não informado!")
    String typeInvestor;
}