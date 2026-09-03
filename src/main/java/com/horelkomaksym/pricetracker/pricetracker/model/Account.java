package com.horelkomaksym.pricetracker.pricetracker.model;

import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account")
public class Account {
    @Id
    @NotNull
    @Column(name = "account_id")
    private Long accountId;

    @NotNull
    @Column(name = "account_name")
    private String accountName;

    @NotNull
    @Column(name = "account_state")
    private String accountState;
}
