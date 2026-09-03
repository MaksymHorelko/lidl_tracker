package com.horelkomaksym.pricetracker.pricetracker.dao;

import com.horelkomaksym.pricetracker.pricetracker.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountDao extends JpaRepository<Account, Long> {
    List<Account> findAllByAccountState(String accountState);
}
