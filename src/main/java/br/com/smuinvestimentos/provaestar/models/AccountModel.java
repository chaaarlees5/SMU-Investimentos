package br.com.smuinvestimentos.provaestar.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "account")
public class AccountModel {
    private 
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) 
    Long idAccount;

    private
    @NotBlank(message = "Agência não informada!")
    String agency;

    private
    @NotBlank(message = "Número da conta não informada!")
    String numberAccount;

    private
    @NotBlank(message = "Tipo da conta não informada!")
    String typeAccount;
    
    private
    double balance;

    private
    @NotBlank(message = "Senha da conta não informada!")
    String password;

    @OneToOne(mappedBy = "account")
    private InvestorPfModel userPf;

    @OneToOne(mappedBy = "account")
    private InvestorPjModel userPj;

    @Transient
    List<ExtractModel> extract;
}
