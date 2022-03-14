package org.pmv.springboot.bank;

import java.math.BigDecimal;

public class AccountServiceImpl implements AccountService{

    private final AccountRepository accountRepository;
    private final BankRepository bankRepository;

    public AccountServiceImpl(AccountRepository accountRepository, BankRepository bankRepository) {
        this.accountRepository = accountRepository;
        this.bankRepository = bankRepository;
    }

    @Override
    public Account findById(Long accountId) {
        return accountRepository.findById(accountId);
    }

    @Override
    public int getTotalTransfers(Long bankId) {
        Bank bank = bankRepository.findById(bankId);
        return bank.getTransfers();
    }

    @Override
    public BigDecimal viewBalance(Long accountId) {
        Account account = accountRepository.findById(accountId);
        return account.getBalance();
    }

    @Override
    public void transfer(Long bankId, Long sourceAccount, Long destinationAccount, BigDecimal amount) {
        Bank bank = bankRepository.findById(bankId);
        int transfers = bank.getTransfers();
        bank.setTransfers(++transfers);
        bankRepository.update(bank);

        Account srcAccount = accountRepository.findById(sourceAccount);
        srcAccount.debit(amount);
        accountRepository.update(srcAccount);

        Account dstDccount = accountRepository.findById(destinationAccount);
        dstDccount.credit(amount);
        accountRepository.update(dstDccount);

    }
}
