package com.hes.account.service;

import com.hes.account.exception.AccountException;
import com.hes.account.model.Account;
import com.hes.account.model.BalanceTO;
import com.hes.account.repository.AccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Implementation for account service
 */
@Service
public class AccountServiceImpl implements AccountService {
    public static final String REGEX = "[0-9]{4}-[0-9]{4}-[0-9]{4}";
    @Autowired
    private AccountDao accountDao;

    @Override
    public List<Account> getAllAccounts() {
        return accountDao.findAll();
    }

    @Override
    public List<Account> getAccount(Long userId) {
        return accountDao.findByUserId(userId);
    }

    @Override
    public void blockAccount(String accountNumber) throws AccountException {
        validAccountNumber(accountNumber);
        accountDao.blockAccount(accountNumber);
    }

    private void validAccountNumber(String accountNumber) throws AccountException {
        if (!accountNumber.matches(REGEX)) {
            throw new AccountException("Not valid account number");
        }
    }

    @Override
    public void unblockAccount(String accountNumber) throws AccountException {
        validAccountNumber(accountNumber);
        accountDao.unblockAccount(accountNumber);
    }

    @Override
    @Transactional
    public Account fillBalance(BalanceTO balance, Long id) throws AccountException {
        validateBalanceTO(balance);
        String accountNumber = balance.getAccountNumber();
        Account account = accountDao.findByNumber(accountNumber);

        validateAccount(account, id);

        BigDecimal currentBalance = account.getBalance();
        BigDecimal newBalance = currentBalance.add(balance.getChangeAmount());

        accountDao.changeBalance(accountNumber, newBalance);
        account.setBalance(newBalance);
        return account;

    }

    @Override
    @Transactional
    public Account takeCash(BalanceTO balance, Long id) throws AccountException {
        validateBalanceTO(balance);
        String accountNumber = balance.getAccountNumber();
        Account account = accountDao.findByNumber(accountNumber);

        validateAccount(account, id);

        BigDecimal currentBalance = account.getBalance();
        BigDecimal changeAmount = balance.getChangeAmount();

        if (currentBalance.compareTo(changeAmount) >= 0) {
            BigDecimal newAmount = currentBalance.subtract(changeAmount);
            accountDao.changeBalance(accountNumber, newAmount);
            account.setBalance(newAmount);
        } else {
            throw new AccountException("Your balance is less than the requested amount");
        }
        return account;
    }

    /**
     * Validate balanceTO object
     *
     * @param balance Balance TO object
     * @throws AccountException if balance null or account number in balance is null or change amount in balance is null, 0, or negative
     */
    private static void validateBalanceTO(BalanceTO balance) throws AccountException {
        if (balance == null || balance.getAccountNumber() == null || balance.getChangeAmount() == null || balance.getChangeAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new AccountException("Not valid account balance change request");
        }
    }

    /**
     * Validate account not null, user's account, not blocked
     *
     * @param account account for validation
     * @param id      user's id for checking if it is user's account
     * @throws AccountException if account is null, not user's account or blocked
     */
    private void validateAccount(Account account, Long id) throws AccountException {
        if (account == null) {
            throw new AccountException("There is no such account.");
        }
        if (!account.getUserId().equals(id)) {
            throw new AccountException("There is no such account for you. Please check account number.");
        }
        if (account.getBlock()) {
            throw new AccountException("Your account is blocked. Please contact support.");
        }
    }
}
