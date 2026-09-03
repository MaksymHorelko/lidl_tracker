package com.horelkomaksym.pricetracker.pricetracker.service.dao.impl;

import com.horelkomaksym.pricetracker.pricetracker.dao.AccountDao;
import com.horelkomaksym.pricetracker.pricetracker.model.Account;
import com.horelkomaksym.pricetracker.pricetracker.service.AccountState;
import com.horelkomaksym.pricetracker.pricetracker.service.dao.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {
    private final AccountDao accountDao;

    @Override
    public Optional<Account> findAccount(Long accountId) {
        return accountDao.findById(accountId);
    }

    @Override
    public List<Account> findAccount(AccountState accountState) {
        return accountDao.findAllByAccountState(accountState.toString());
    }

    @Override
    public Account saveAccount(Long chatId, String username) {
        return accountDao.save(new Account(chatId,username,AccountState.FREE.toString()));
    }

    private Account saveAccount(Account account) {
        return accountDao.save(account);
    }

    @Override
    public Account findOrSaveAccount(Long accountId, String username) {
        var account = findAccount(accountId);
        return account.orElseGet(() -> saveAccount(accountId, username));
    }


    @Override
    public boolean changeAccountState(Long accountId, String accountState) {
        Optional<Account> account = findAccount(accountId);
        if (account.isEmpty()) {
            return false;
        }
        Account acc = account.get();
        acc.setAccountState(accountState);
        saveAccount(acc);
        return true;
    }

    @Override
    public AccountState getAccountState(Long accountId) {
        return AccountState.valueOf(findAccount(accountId).orElseThrow(IllegalArgumentException::new).getAccountState());
    }


    @Override
    public boolean isPresent(Long accountId) {
        return accountDao.findById(accountId).isPresent();
    }
}
