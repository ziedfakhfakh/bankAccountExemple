package com.bank.account.management.model;

import com.bank.account.management.model.type.BankAccountStatus;
import com.bank.account.management.model.type.BankAccountType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"accountNumber"})
@SuperBuilder
@DiscriminatorColumn(name = "Bank_account_type", discriminatorType = DiscriminatorType.STRING)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(indexes = {@Index(name = "idx_account_number", columnList = "account_number")})
public class BankAccount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected UUID id;

    @CreationTimestamp
    @Column(updatable = false)
    protected LocalDateTime creationDate;

    @UpdateTimestamp
    protected LocalDateTime updateDate;

    @Column(nullable = false, unique = true)
    protected String accountNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    protected BankAccountStatus status;

    protected double balance;

    @OneToMany(mappedBy = "bankAccount", cascade = CascadeType.MERGE)
    protected List<BankAccountTransaction> bankAccountTransactions;

    @ManyToOne(fetch = FetchType.LAZY)
    private Client client;

    public BankAccountType getBankAccountType() {
        if (this instanceof CurrentAccount) {
            return BankAccountType.CURRENT_ACCOUNT;
        } else if (this instanceof SavingsAccount) {
            return BankAccountType.SAVINGS_ACCOUNT;
        } else {
            return null;
        }
    }
}
