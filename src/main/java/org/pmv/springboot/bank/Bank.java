package org.pmv.springboot.bank;

import javax.persistence.*;

@Entity
@Table(name = "tbl_banks")
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int transfers;

    public Bank() {
    }

    public Bank(Long id, String name, int transfers) {
        this.id = id;
        this.name = name;
        this.transfers = transfers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTransfers() {
        return transfers;
    }

    public void setTransfers(int transfers) {
        this.transfers = transfers;
    }
}
