package com.hes.account.repository;

import com.hes.account.model.Account;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao extends ListCrudRepository<Account, Long> {

    List<Account> findByUserId(Long userId);

    Account findByNumber(String number);

    @Modifying
    @Transactional
    @Query("UPDATE account SET block=TRUE WHERE number= :accountNumber")
    void blockAccount(String accountNumber);

    @Modifying
    @Transactional
    @Query("UPDATE account SET block=FALSE WHERE number= :accountNumber")
    void unblockAccount(String accountNumber);

    @Modifying
    @Transactional
    @Query("UPDATE account SET balance= :sum WHERE number= :accountNumber")
    void changeBalance(String accountNumber, BigDecimal sum);
}
