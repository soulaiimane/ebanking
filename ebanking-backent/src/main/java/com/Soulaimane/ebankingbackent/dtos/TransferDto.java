package com.Soulaimane.ebankingbackent.dtos;

import lombok.Data;

@Data
public class TransferDto {
    private String sourceId;
    private String destinationId;
    private double amount;
}
