package br.com.smuinvestimentos.provaestar.models;

import jakarta.persistence.CascadeType;
import org.hibernate.validator.constraints.br.CNPJ;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "investor_pj")
public class InvestorPjModel {
    
    private 
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) 
    Long idUser;
    
    private
    @NotBlank(message = "CPF do usuário não informado!")
    @CNPJ(message = "CPF inválido!")
    String cnpj;
    
    private
    @NotBlank(message = "Email não informado!")
    @Email(message = "Email inválido. Operação não permitida!")
    String email;

    private
    @NotBlank(message = "Nome da empresa não informado!")
    String companyName;

    private
    @NotBlank(message = "Senha não informada!")
    String password;    
    
    private
    boolean active = true;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idAccount")
    private AccountModel account;
}
