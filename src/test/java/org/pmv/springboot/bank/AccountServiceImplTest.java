package org.pmv.springboot.bank;

import org.junit.jupiter.api.*;
import org.pmv.springboot.account.Account;
import org.pmv.springboot.account.AccountRepository;
import org.pmv.springboot.account.AccountService;
import org.pmv.springboot.exceptions.InsufficientBalanceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.pmv.springboot.bank.DataTest.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountServiceImplTest {

    @MockBean
    AccountRepository accountRepository;
    @MockBean BankRepository bankRepository;
    @Autowired
    AccountService accountService;


    @Test
    @DisplayName("Do transfer test")
    @Order(1)
    void account_service_transfer_test() {
        // GIVEN
        when(accountRepository.findById(1L)).thenReturn(ACCOUNT_1);
        when(accountRepository.findById(2L)).thenReturn(ACCOUNT_2);
        when(bankRepository.findById(1L)).thenReturn(BANK);
        // WHEN
        BigDecimal balanceAccount1 = accountService.viewBalance(1L);
        BigDecimal balanceAccount2 = accountService.viewBalance(2L);
        // THEN
        assertEquals("1000", balanceAccount1.toPlainString());
        assertEquals("2000", balanceAccount2 .toPlainString());

        // WHEN
        BigDecimal amount = new BigDecimal("100");
        accountService.transfer(1L,1L,2L, amount);
        balanceAccount1 = accountService.viewBalance(1L);
        balanceAccount2 = accountService.viewBalance(2L);
        assertTrue(amount.doubleValue() <= balanceAccount1.doubleValue());

        int totalTransfers = accountService.getTotalTransfers(1L);
        assertEquals(1, totalTransfers); // It needs order annotation


        // THEN
        assertEquals("900", balanceAccount1.toPlainString());
        assertEquals("2100", balanceAccount2.toPlainString());
        verify(bankRepository, times(2)).save(any(Bank.class));
        verify(accountRepository,times(3)).findById(1L);
        verify(accountRepository,times(3)).findById(2L);
        verify(accountRepository,times(2)).save(any(Account.class));
        verify(bankRepository, times(2)).findById(anyLong());
    }

    @Test
    @Order(2)
    void account_service_exception_transfer_test() {
        // GIVEN
        when(accountRepository.findById(1L)).thenReturn(ACCOUNT_1);
        //when(accountRepository.findById(2L)).thenReturn(ACCOUNT_2); unnecesary stub
        when(bankRepository.findById(1L)).thenReturn(BANK);

        // WHEN & THEN
        BigDecimal amount = new BigDecimal("5000");
        assertThrows(InsufficientBalanceException.class, ()->{
            accountService.transfer(1L,1L,2L, amount);
        });

        // THEN... we can check verifys
    }


    @Test
    void same_account_Test() {
        when(accountRepository.findById(1L)).thenReturn(ACCOUNT_1);

        Account account1 = accountService.findById(1L);
        Account account2 = accountService.findById(1L);
        assertSame(account1,account2);
    }
}