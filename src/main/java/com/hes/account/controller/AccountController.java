package com.hes.account.controller;

import com.hes.account.exception.AccountException;
import com.hes.account.model.Account;
import com.hes.account.model.BalanceTO;
import com.hes.account.service.AccountService;
import com.hes.account.service.AccountServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class AccountController {
    final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
    @Autowired
    private AccountService accountService;

    @GetMapping("/accounts")
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok().body(accounts);
    }

    @GetMapping("/user/account/{id}")
    @PreAuthorize("#id == authentication.principal.userId")
    ResponseEntity<List<Account>> getAccount(@PathVariable Long id) {
        List<Account> accounts = accountService.getAccount(id);
        return ResponseEntity.ok().body(accounts);
    }

    @GetMapping("/account/block")
    @PreAuthorize("hasAuthority('ADMIN')")
    void blockAccount(@RequestParam String accountNumber) throws AccountException {
        accountService.blockAccount(accountNumber);
    }

    @GetMapping("/account/unblock")
    @PreAuthorize("hasAuthority('ADMIN')")
    void unblockAccount(@RequestParam String accountNumber) throws AccountException {
        accountService.unblockAccount(accountNumber);
    }

    @PostMapping(path = "user/account/{id}/fill", consumes = "application/json")
    @PreAuthorize("#id == authentication.principal.userId")
    ResponseEntity<Account> fillAccountBalance(@RequestBody BalanceTO balance, @PathVariable Long id) throws AccountException {
        Account account = accountService.fillBalance(balance, id);
        return ResponseEntity.ok(account);
    }

    @PostMapping(path = "user/account/{id}/cash", consumes = "application/json")
    @PreAuthorize("#id == authentication.principal.userId")
    ResponseEntity<Account> takeCashAccountBalance(@RequestBody BalanceTO balance, @PathVariable Long id) throws AccountException {
        Account account = accountService.takeCash(balance, id);
        return ResponseEntity.ok(account);
    }

    @ExceptionHandler(AccountException.class)
    public ResponseEntity<Object> getAccountException(AccountException exception) {
        logger.error(exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
