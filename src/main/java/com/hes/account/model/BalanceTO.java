package com.hes.account.model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;

@Component
@Scope("prototype")
public class BalanceTO {
    private String accountNumber;
    private BigDecimal changeAmount;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(BigDecimal changeAmount) {
        this.changeAmount = changeAmount;
    }

    @Override
    public String toString() {
        return "BalanceTO{" +
                "accountNumber='" + accountNumber + '\'' +
                ", changeAmount=" + changeAmount +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        BalanceTO balanceTO = (BalanceTO) object;
        return Objects.equals(accountNumber, balanceTO.accountNumber) && Objects.equals(changeAmount, balanceTO.changeAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, changeAmount);
    }
}
