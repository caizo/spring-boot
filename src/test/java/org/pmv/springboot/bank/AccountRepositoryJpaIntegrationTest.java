package org.pmv.springboot.bank;

import org.junit.jupiter.api.Test;
import org.pmv.springboot.account.Account;
import org.pmv.springboot.account.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AccountRepositoryJpaIntegrationTest {

    @Autowired
    AccountRepository accountRepository;


    @Test
    void find_by_id_test() {
        Optional<Account> account = accountRepository.findById(1L);
        assertTrue(account.isPresent());
        assertEquals("Juan",account.orElseThrow().getHolder());
    }

    @Test
    void find_by_holder_test() {
        Optional<Account> account = accountRepository.findByHolderr("Jose");
        assertTrue(account.isPresent());
        assertEquals("Jose",account.orElseThrow().getHolder());
        assertEquals("2000.00", account.orElseThrow().getBalance().toPlainString());
    }

    @Test
    void find_by_holder_throw_exception_test() {
        Optional<Account> account = accountRepository.findByHolderr("xxx");
        assertThrows(NoSuchElementException.class, account::orElseThrow);
        assertFalse(account.isPresent());
    }    
    @Test
    void find_all_test() {
        List<Account> all = accountRepository.findAll();
        assertFalse(all.isEmpty());
        assertEquals(2,all.size());
    }


    @Test
    void save_test() {
        // GIVEN
        Account pepeAccount = new Account(3L,"Pepe",new BigDecimal("3000"));
        // WHEN
        Account account = accountRepository.save(pepeAccount);
        // THEN
        assertEquals("Pepe",account.getHolder());
        assertEquals("3000", account.getBalance().toPlainString());
    }

    @Test
    void update_test() {
        // GIVEN
        Account pepeAccount = new Account(3L,"Pepe",new BigDecimal("3000"));
        // WHEN
        Account account = accountRepository.save(pepeAccount);
        // THEN
        assertEquals("Pepe",account.getHolder());
        assertEquals("3000", account.getBalance().toPlainString());

        // WHEN
        account.setBalance(new BigDecimal("4000"));
        Account updatedAccount = accountRepository.save(account);
        // THEN
        assertEquals("4000", updatedAccount.getBalance().toPlainString());

    }


    @Test
    void delete_test() {
        Account account = accountRepository.findById(1L).orElseThrow();
        assertEquals("Juan", account.getHolder());
        accountRepository.delete(account);

        assertThrows(NoSuchElementException.class, () -> accountRepository.findByHolderr("Juan").orElseThrow());
    }
}
