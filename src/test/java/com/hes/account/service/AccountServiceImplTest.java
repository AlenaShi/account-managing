package com.hes.account.service;

import com.hes.account.exception.AccountException;
import com.hes.account.model.Account;
import com.hes.account.model.BalanceTO;
import com.hes.account.repository.AccountDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class AccountServiceImplTest {
    @Mock
    private AccountDao accountDao;
    @InjectMocks
    private AccountService accountService = new AccountServiceImpl();


    @Test
    void getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        Account account = new Account();
        account.setAccountId(1L);
        account.setBalance(BigDecimal.valueOf(100));
        account.setBlock(false);
        account.setNumber("1234-1234-1234");
        account.setUserId(3L);
        accounts.add(account);
        when(accountDao.findAll()).thenReturn(accounts);
        List<Account> actual = accountService.getAllAccounts();
        assertEquals(actual, accounts);
        verify(accountDao, times(1)).findAll();
    }

    @Test
    void getAccount() {
        List<Account> accounts = new ArrayList<>();
        Account account = new Account();
        account.setAccountId(1L);
        account.setBalance(BigDecimal.valueOf(100));
        account.setBlock(false);
        account.setNumber("1234-1234-1234");
        account.setUserId(3L);
        accounts.add(account);
        long userId = 3L;
        when(accountDao.findByUserId(userId)).thenReturn(accounts);
        List<Account> actual = accountService.getAccount(userId);
        assertEquals(actual, accounts);
        verify(accountDao, times(1)).findByUserId(userId);
    }

    @Test
    void blockAccount() throws AccountException {
        String accountNumber = "1113-2223-3333";
        accountService.blockAccount(accountNumber);
        verify(accountDao, times(1)).blockAccount(accountNumber);
    }

    @Test
    void unblockAccount() throws AccountException {
        String accountNumber = "1113-2223-3333";
        accountService.unblockAccount(accountNumber);
        verify(accountDao, times(1)).unblockAccount(accountNumber);
    }

    @Test
    void fillBalance() throws AccountException {
        long userId = 5l;
        String accountNumber = "123-123-111";
        BalanceTO balance = new BalanceTO();
        balance.setAccountNumber(accountNumber);
        balance.setChangeAmount(BigDecimal.valueOf(15));
        Account account = new Account();
        BigDecimal newBalance = BigDecimal.valueOf(115);
        account.setUserId(userId);
        account.setBalance(BigDecimal.valueOf(100));
        account.setAccountId(1L);
        account.setBlock(false);
        account.setNumber(accountNumber);
        when(accountDao.findByNumber(accountNumber)).thenReturn(account);
        Account actual = accountService.fillBalance(balance, userId);
        account.setBalance(newBalance);
        assertEquals(actual, account);
        verify(accountDao, times(1)).findByNumber(accountNumber);
        verify(accountDao, times(1)).changeBalance(accountNumber, newBalance);
    }

    @Test
    void fillBalanceBlockUser() {
        long userId = 5l;
        String accountNumber = "123-123-111";
        BalanceTO balance = new BalanceTO();
        balance.setAccountNumber(accountNumber);
        balance.setChangeAmount(BigDecimal.valueOf(15));
        Account account = new Account();
        BigDecimal newBalance = BigDecimal.valueOf(115);
        account.setUserId(userId);
        account.setBalance(BigDecimal.valueOf(100));
        account.setAccountId(1L);
        account.setBlock(true);
        account.setNumber(accountNumber);
        when(accountDao.findByNumber(accountNumber)).thenReturn(account);
        assertThrows(AccountException.class, () -> accountService.fillBalance(balance, userId));
        verify(accountDao, times(1)).findByNumber(accountNumber);
        verify(accountDao, times(0)).changeBalance(accountNumber, newBalance);
    }

    @Test
    void fillBalanceNull() {
        long userId = 5l;
        String accountNumber = "123-123-111";
        BalanceTO balance = null;
        Account account = new Account();
        BigDecimal newBalance = BigDecimal.valueOf(115);
        account.setUserId(userId);
        account.setBalance(BigDecimal.valueOf(100));
        account.setAccountId(1L);
        account.setBlock(true);
        account.setNumber(accountNumber);
        when(accountDao.findByNumber(accountNumber)).thenReturn(account);
        assertThrows(AccountException.class, () -> accountService.fillBalance(balance, userId));
        verify(accountDao, times(0)).findByNumber(accountNumber);
        verify(accountDao, times(0)).changeBalance(accountNumber, newBalance);
    }

    @Test
    void fillBalanceNullCount() {
        long userId = 5l;
        String accountNumber = "123-123-111";
        BalanceTO balance = new BalanceTO();
        balance.setAccountNumber(null);
        balance.setChangeAmount(BigDecimal.valueOf(15));
        Account account = new Account();
        BigDecimal newBalance = BigDecimal.valueOf(115);
        account.setUserId(userId);
        account.setBalance(BigDecimal.valueOf(100));
        account.setAccountId(1L);
        account.setBlock(true);
        account.setNumber(accountNumber);
        when(accountDao.findByNumber(accountNumber)).thenReturn(account);
        assertThrows(AccountException.class, () -> accountService.fillBalance(balance, userId));
        verify(accountDao, times(0)).findByNumber(accountNumber);
        verify(accountDao, times(0)).changeBalance(accountNumber, newBalance);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -15, -300, -3})
    void fillBalanceNotValidAmountUser(int i) {
        long userId = 5l;
        String accountNumber = "123-123-111";
        BalanceTO balance = new BalanceTO();
        balance.setAccountNumber(null);
        balance.setChangeAmount(BigDecimal.valueOf(i));
        Account account = new Account();
        BigDecimal newBalance = BigDecimal.valueOf(115);
        account.setUserId(userId);
        account.setBalance(BigDecimal.valueOf(100));
        account.setAccountId(1L);
        account.setBlock(true);
        account.setNumber(accountNumber);
        when(accountDao.findByNumber(accountNumber)).thenReturn(account);
        assertThrows(AccountException.class, () -> accountService.fillBalance(balance, userId));
        verify(accountDao, times(0)).findByNumber(accountNumber);
        verify(accountDao, times(0)).changeBalance(accountNumber, newBalance);
    }

    @Test
    void fillAccountNullUser() {
        long userId = 5l;
        String accountNumber = "123-123-111";
        BalanceTO balance = new BalanceTO();
        balance.setAccountNumber(null);
        balance.setChangeAmount(BigDecimal.valueOf(15));
        Account account = null;
        when(accountDao.findByNumber(accountNumber)).thenReturn(account);
        assertThrows(AccountException.class, () -> accountService.fillBalance(balance, userId));
        verify(accountDao, times(0)).findByNumber(accountNumber);
    }

    @Test
    void fillNotUserAccount() {
        long userId = 5l;
        String accountNumber = "123-123-111";
        BalanceTO balance = new BalanceTO();
        balance.setAccountNumber(null);
        balance.setChangeAmount(BigDecimal.valueOf(15));
        Account account = new Account();
        BigDecimal newBalance = BigDecimal.valueOf(115);
        account.setUserId(2L);
        account.setBalance(BigDecimal.valueOf(100));
        account.setAccountId(1L);
        account.setBlock(true);
        account.setNumber(accountNumber);
        when(accountDao.findByNumber(accountNumber)).thenReturn(account);
        assertThrows(AccountException.class, () -> accountService.fillBalance(balance, userId));
        verify(accountDao, times(0)).findByNumber(accountNumber);
        verify(accountDao, times(0)).changeBalance(accountNumber, newBalance);
    }

    @Test
    void takeCash() throws AccountException {
        long userId = 5l;
        String accountNumber = "123-123-111";
        BalanceTO balance = new BalanceTO();
        balance.setAccountNumber(accountNumber);
        balance.setChangeAmount(BigDecimal.valueOf(15));
        Account account = new Account();
        BigDecimal newBalance = BigDecimal.valueOf(85);
        account.setUserId(userId);
        account.setBalance(BigDecimal.valueOf(100));
        account.setAccountId(1L);
        account.setBlock(false);
        account.setNumber(accountNumber);
        when(accountDao.findByNumber(accountNumber)).thenReturn(account);
        Account actual = accountService.takeCash(balance, userId);
        account.setBalance(newBalance);
        assertEquals(actual, account);
        verify(accountDao, times(1)).findByNumber(accountNumber);
        verify(accountDao, times(1)).changeBalance(accountNumber, newBalance);
    }

    @Test
    void takeCashBlockUser() {
        long userId = 5l;
        String accountNumber = "123-123-111";
        BalanceTO balance = new BalanceTO();
        balance.setAccountNumber(accountNumber);
        balance.setChangeAmount(BigDecimal.valueOf(15));
        Account account = new Account();
        BigDecimal newBalance = BigDecimal.valueOf(85);
        account.setUserId(userId);
        account.setBalance(BigDecimal.valueOf(100));
        account.setAccountId(1L);
        account.setBlock(true);
        account.setNumber(accountNumber);
        when(accountDao.findByNumber(accountNumber)).thenReturn(account);
        assertThrows(AccountException.class, () -> accountService.takeCash(balance, userId));
        verify(accountDao, times(1)).findByNumber(accountNumber);
        verify(accountDao, times(0)).changeBalance(accountNumber, newBalance);
    }
@Test
   void takeCashBalanceNull() {
        long userId = 5l;
        String accountNumber = "123-123-111";
        BalanceTO balance = null;
        Account account = new Account();
        BigDecimal newBalance = BigDecimal.valueOf(85);
        account.setUserId(userId);
        account.setBalance(BigDecimal.valueOf(100));
        account.setAccountId(1L);
        account.setBlock(true);
        account.setNumber(accountNumber);
        when(accountDao.findByNumber(accountNumber)).thenReturn(account);
        assertThrows(AccountException.class, () -> accountService.takeCash(balance, userId));
        verify(accountDao, times(0)).findByNumber(accountNumber);
        verify(accountDao, times(0)).changeBalance(accountNumber, newBalance);
    }
    @Test
    void takeCashBalanceNullCount() {
        long userId = 5l;
        String accountNumber = "123-123-111";
        BalanceTO balance = new BalanceTO();
        balance.setAccountNumber(null);
        balance.setChangeAmount(BigDecimal.valueOf(15));
        Account account = new Account();
        BigDecimal newBalance = BigDecimal.valueOf(85);
        account.setUserId(userId);
        account.setBalance(BigDecimal.valueOf(100));
        account.setAccountId(1L);
        account.setBlock(true);
        account.setNumber(accountNumber);
        when(accountDao.findByNumber(accountNumber)).thenReturn(account);
        assertThrows(AccountException.class, () -> accountService.takeCash(balance, userId));
        verify(accountDao, times(0)).findByNumber(accountNumber);
        verify(accountDao, times(0)).changeBalance(accountNumber, newBalance);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -15, -300, -3, 110, 200, 1000})
    void takeCashBalanceNotValidAmountUser(int i) {
        long userId = 5l;
        String accountNumber = "123-123-111";
        BalanceTO balance = new BalanceTO();
        balance.setAccountNumber(null);
        balance.setChangeAmount(BigDecimal.valueOf(i));
        Account account = new Account();
        BigDecimal newBalance = BigDecimal.valueOf(85);
        account.setUserId(userId);
        account.setBalance(BigDecimal.valueOf(100));
        account.setAccountId(1L);
        account.setBlock(true);
        account.setNumber(accountNumber);
        when(accountDao.findByNumber(accountNumber)).thenReturn(account);
        assertThrows(AccountException.class, () -> accountService.takeCash(balance, userId));
        verify(accountDao, times(0)).findByNumber(accountNumber);
        verify(accountDao, times(0)).changeBalance(accountNumber, newBalance);
    }

    @Test
    void takeCashAccountNullUser() {
        long userId = 5l;
        String accountNumber = "123-123-111";
        BalanceTO balance = new BalanceTO();
        balance.setAccountNumber(null);
        balance.setChangeAmount(BigDecimal.valueOf(15));
        Account account = null;
        when(accountDao.findByNumber(accountNumber)).thenReturn(account);
        assertThrows(AccountException.class, () -> accountService.takeCash(balance, userId));
        verify(accountDao, times(0)).findByNumber(accountNumber);
    }

    @Test
    void takeCashNotUserAccount() {
        long userId = 5l;
        String accountNumber = "123-123-111";
        BalanceTO balance = new BalanceTO();
        balance.setAccountNumber(null);
        balance.setChangeAmount(BigDecimal.valueOf(15));
        Account account = new Account();
        BigDecimal newBalance = BigDecimal.valueOf(115);
        account.setUserId(2L);
        account.setBalance(BigDecimal.valueOf(100));
        account.setAccountId(1L);
        account.setBlock(true);
        account.setNumber(accountNumber);
        when(accountDao.findByNumber(accountNumber)).thenReturn(account);
        assertThrows(AccountException.class, () -> accountService.takeCash(balance, userId));
        verify(accountDao, times(0)).findByNumber(accountNumber);
        verify(accountDao, times(0)).changeBalance(accountNumber, newBalance);
    }
}

