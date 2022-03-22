package org.pmv.springboot.account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    Account findById(Long accountId);
    int getTotalTransfers(Long bankId);
    BigDecimal viewBalance(Long accountId);

    void transfer(Long bankId, Long sourceAccount, Long destinationAccount, BigDecimal amount);

    List<Account> findAll();

    Account save(Account account);

    void deleteById(Long id);
}
