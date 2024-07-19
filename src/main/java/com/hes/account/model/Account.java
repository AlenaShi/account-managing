package com.hes.account.model;

import jakarta.persistence.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;

@Entity(name = "account")
@Component
@Scope("prototype")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_id")
    @SequenceGenerator(name = "account_id", sequenceName = "account_id_seq", allocationSize = 1)
    private Long accountId;
    @Column(unique = true)
    private String number;
    @Column
    private BigDecimal balance;
    @Column
    private Boolean block;
    @Column(name = "user_id")
    private Long userId;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Boolean getBlock() {
        return block;
    }

    public void setBlock(Boolean block) {
        this.block = block;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Account account = (Account) object;
        return Objects.equals(accountId, account.accountId) && Objects.equals(number, account.number) && Objects.equals(balance, account.balance) && Objects.equals(block, account.block) && Objects.equals(userId, account.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, number, balance, block, userId);
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", number='" + number + '\'' +
                ", balance=" + balance +
                ", block=" + block +
                ", userId=" + userId +
                '}';
    }
}
