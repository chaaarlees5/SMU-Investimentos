package br.com.smuinvestimentos.provaestar.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import org.hibernate.validator.constraints.br.CNPJ;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "extract")
public class ExtractModel {
    
    private 
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) 
    Long idExtract;
    
    private
    @NotNull(message = "Saldo antigo não informado!")
    double oldBalance;
    
    private
    @NotNull(message = "Saldo novo não informado!")
    double newBalance;
    
    private
    @NotNull(message = "Valor adicional não informado!")
    double additionalValue;
    
    private
    @NotBlank(message = "Operação não informada!")
    String operation;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idAccount")
    private AccountModel account;
    
}

