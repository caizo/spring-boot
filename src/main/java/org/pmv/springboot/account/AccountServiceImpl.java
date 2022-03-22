package org.pmv.springboot.account;

import org.pmv.springboot.bank.Bank;
import org.pmv.springboot.bank.BankRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
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
    @Transactional(readOnly = true)
    public Account findById(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public int getTotalTransfers(Long bankId) {
        Bank bank = bankRepository.findById(bankId).orElseThrow();
        return bank.getTransfers();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal viewBalance(Long accountId) {
        //Account account = accountRepository.findById(accountId);
        Optional<Account> account = accountRepository.findById(accountId);
        return account.orElseThrow().getBalance();
    }

    @Override
    @Transactional()
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

    @Override
    @Transactional(readOnly = true)
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    @Transactional
    public Account save(Account account) {
        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        accountRepository.deleteById(id);
    }
}
