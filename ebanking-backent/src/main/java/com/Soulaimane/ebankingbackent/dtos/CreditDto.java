package com.Soulaimane.ebankingbackent.dtos;

import lombok.Data;

@Data
public class CreditDto {
    private String accountId;
    private double amount;
    private String description;
}
