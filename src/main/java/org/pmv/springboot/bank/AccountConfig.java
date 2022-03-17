package org.pmv.springboot.bank;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class AccountConfig {

    @Bean("accountRunner")
    CommandLineRunner commandLineRunner(AccountRepository accountRepository){
        return args -> {
            Account account_1 = new Account(1L, "Juan", new BigDecimal("1000"));
            Account account_2 = new Account(2L, "Jose", new BigDecimal("2000"));
            accountRepository.saveAll(List.of(account_1, account_2));
        };
    }
}
