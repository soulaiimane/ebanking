package com.Soulaimane.ebankingbackent.dtos;
import com.Soulaimane.ebankingbackent.enums.OperationType;
import lombok.Data;


import java.util.Date;
@Data

public class AccountOperationDto {
    private Long id;
    private Date operationDate;
    private double amount;
    private OperationType operationType;
    private String description;
}
