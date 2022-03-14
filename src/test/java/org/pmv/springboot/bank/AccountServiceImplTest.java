package org.pmv.springboot.bank;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.pmv.springboot.bank.DataTest.*;

//@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock AccountRepository accountRepository;
    @Mock BankRepository bankRepository;
    @InjectMocks AccountServiceImpl accountService;


    @Test
    void accounts_operations_test() {
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
        accountService.transfer(1L,1L,2L,new BigDecimal("100"));
        balanceAccount1 = accountService.viewBalance(1L);
        balanceAccount2 = accountService.viewBalance(2L);

        // THEN
        assertEquals("900", balanceAccount1.toPlainString());
        assertEquals("2100", balanceAccount2.toPlainString());
        verify(bankRepository).update(BANK);
        verify(accountRepository,times(3)).findById(1L);
        verify(accountRepository,times(3)).findById(2L);
        verify(accountRepository).update(ACCOUNT_1);
        verify(accountRepository).update(ACCOUNT_2);

    }
}