package com.bank.account.management.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@DiscriminatorValue("SAVINGS_ACCOUNT")
public class SavingsAccount extends BankAccount {

    private double savingsInterestRate = 0;

}
