package org.pmv.springboot.bank;

import java.math.BigDecimal;

public interface AccountService {
    Account findById(Long accountId);
    int getTotalTransfers(Long bankId);
    BigDecimal viewBalance(Long accountId);

    void transfer(Long bankId, Long sourceAccount, Long destinationAccount, BigDecimal amount);
}
