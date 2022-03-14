package org.pmv.springboot.bank;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService{

    private final AccountRepository accountRepository;
    private final BankRepository bankRepository;

    public AccountServiceImpl(AccountRepository accountRepository, BankRepository bankRepository) {
        this.accountRepository = accountRepository;
        this.bankRepository = bankRepository;
    }

    @Override
    public Account findById(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow();
    }

    @Override
    public int getTotalTransfers(Long bankId) {
        Bank bank = bankRepository.findById(bankId).orElseThrow();
        return bank.getTransfers();
    }

    @Override
    public BigDecimal viewBalance(Long accountId) {
        //Account account = accountRepository.findById(accountId);
        Optional<Account> account = accountRepository.findById(accountId);
        return account.orElseThrow().getBalance();
    }

    @Override
    public void transfer(Long bankId, Long sourceAccount, Long destinationAccount, BigDecimal amount) {
        Bank bank = bankRepository.findById(bankId).orElseThrow();
        int transfers = bank.getTransfers();
        bank.setTransfers(++transfers);
        bankRepository.save(bank);

        Account srcAccount = accountRepository.findById(sourceAccount).orElseThrow();
        srcAccount.debit(amount);
        accountRepository.save(srcAccount);

        Account dstDccount = accountRepository.findById(destinationAccount).orElseThrow();
        dstDccount.credit(amount);
        accountRepository.save(dstDccount);

    }
}
