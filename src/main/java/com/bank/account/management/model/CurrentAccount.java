package com.bank.account.management.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@DiscriminatorValue("CURRENT_ACCOUNT")
public class CurrentAccount extends BankAccount {

    private double overdraft = 0;

}
