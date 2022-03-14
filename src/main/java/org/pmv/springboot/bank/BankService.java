package org.pmv.springboot.bank;

import java.util.List;

public interface BankService {
    Bank findById(Long id);
    List<Bank> findAll();
    void update(Bank bank);
}
