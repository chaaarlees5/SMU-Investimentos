package br.com.smuinvestimentos.provaestar.models;

import jakarta.persistence.CascadeType;
import org.hibernate.validator.constraints.br.CPF;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "investor_pf")
public class InvestorPfModel {

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idUser;

    private @NotBlank(message = "CPF do usuário não informado!")
    @CPF(message = "CPF inválido!")
    String cpf;

    @NotBlank(message = "Email não informado!")
    @Email(message = "Email inválido. Operação não permitida!")
    private String email;

    @NotBlank(message = "Nome não informado!")
    private String name;

    @NotBlank(message = "Senha não informada!")
    private String password;

    private boolean active = true;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idAccount")
    private AccountModel account;
}
