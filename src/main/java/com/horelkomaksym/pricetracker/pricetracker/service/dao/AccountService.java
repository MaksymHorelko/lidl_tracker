package com.horelkomaksym.pricetracker.pricetracker.service.dao;

import com.horelkomaksym.pricetracker.pricetracker.model.Account;
import com.horelkomaksym.pricetracker.pricetracker.service.AccountState;
import java.util.List;
import java.util.Optional;

public interface AccountService {
    Optional<Account> findAccount(Long accountId);

    List<Account> findAccount(AccountState accountState);

    Account saveAccount(Long chatId, String username);

    Account findOrSaveAccount(Long chatId, String username);

    boolean changeAccountState(Long accountId, String accountState);

    AccountState getAccountState(Long accountId);

    boolean isPresent(Long accountId);
}