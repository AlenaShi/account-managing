package com.hes.account.service;

import com.hes.account.exception.AccountException;
import com.hes.account.model.Account;
import com.hes.account.model.BalanceTO;

import java.util.List;

/**
 * Use for work with accounts
 */
public interface AccountService {
    /**
     * Used for getting all accounts
     *
     * @return List of all accounts
     */
    List<Account> getAllAccounts();

    /**
     * Used to get users accounts
     *
     * @param userId - user identification number
     * @return List of user's accounts
     */
    List<Account> getAccount(Long userId);

    /**
     * Used for blocking account
     *
     * @param accountNumber number of  account [0-9]{4}-[0-9]{4}-[0-9]{4}
     * @throws AccountException if wrong account number format
     */
    void blockAccount(String accountNumber) throws AccountException;

    /**
     * Used to unblock account by account number
     *
     * @param accountNumber number of  account [0-9]{4}-[0-9]{4}-[0-9]{4}
     * @throws AccountException if wrong account number format
     */
    void unblockAccount(String accountNumber) throws AccountException;

    /**
     * Used to fill account with help of balanceTO
     *
     * @param balance contain data about account number and amount to add
     * @param id      user identification number
     * @return account object with new balance
     * @throws AccountException if not valid balance or blocked account, or no such user's account
     */
    Account fillBalance(BalanceTO balance, Long id) throws AccountException;

    /**
     * Used to take money from account
     *
     * @param balance contain data about account number and amount to take
     * @param id      user identification number
     * @return account object with new balance
     * @throws AccountException if not valid balance or blocked account, or no such user's account
     */
    Account takeCash(BalanceTO balance, Long id) throws AccountException;

}
