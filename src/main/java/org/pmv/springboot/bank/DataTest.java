package org.pmv.springboot.bank;

import java.math.BigDecimal;

public class DataTest {

    public static final Account ACCOUNT_1 = new Account(1L,"Juan",new BigDecimal("1000"));
    public static final Account ACCOUNT_2 = new Account(2L,"Jose",new BigDecimal("2000"));
    public static final Bank BANK = new Bank(1L, "BBVA",0);

}
