package com.Soulaimane.ebankingbackent.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class BankAccountDto {
    private String id;
    private double balance;
    private String type;
    private Date createdAt;

}
