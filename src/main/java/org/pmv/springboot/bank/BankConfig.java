package org.pmv.springboot.bank;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class BankConfig {

    @Bean("bankRunner")
    CommandLineRunner commandLineRunner(BankRepository bankRepository){
        return args -> {
            Bank bank = new Bank(1L, "BBVA", 0);
            bankRepository.save(bank);
        };
    }
}
