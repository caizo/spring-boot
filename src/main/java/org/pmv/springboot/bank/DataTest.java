package org.pmv.springboot.bank;

import java.math.BigDecimal;
import java.util.Optional;

public class DataTest {

    public static final Optional<Account> ACCOUNT_1 = Optional.of(new Account(1L, "Juan", new BigDecimal("1000")));
    public static final Optional<Account> ACCOUNT_2 = Optional.of(new Account(2L, "Jose", new BigDecimal("2000")));
    public static final  Optional<Bank> BANK = Optional.of(new Bank(1L, "BBVA", 0));

}
