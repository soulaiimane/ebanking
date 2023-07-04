package com.Soulaimane.ebankingbackent.web;

import com.Soulaimane.ebankingbackent.dtos.*;
import com.Soulaimane.ebankingbackent.entities.AccountOperation;
import com.Soulaimane.ebankingbackent.exeption.AccountNotFoundExeption;
import com.Soulaimane.ebankingbackent.exeption.BalanceNotSufficientExeption;
import com.Soulaimane.ebankingbackent.exeption.CustomerNotFoundExeption;
import com.Soulaimane.ebankingbackent.services.BankAccountServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class BankAccountRestController {
    private BankAccountServiceImpl bankAccountService;

    public BankAccountRestController(BankAccountServiceImpl bankAccountService) {
        this.bankAccountService = bankAccountService;
    }
    @GetMapping("/accounts/{bankAccountId}")
    public BankAccountDto getBankAccountRest(@PathVariable String bankAccountId) throws AccountNotFoundExeption {
        return bankAccountService.getBankAccount(bankAccountId);
    }
    @GetMapping("/accounts")
    public List<BankAccountDto> getAllBankAccountRest()  {
        return bankAccountService.ACCOUNTLIST();
    }
    /*@PostMapping("/accounts")
    public BankAccountDto saveAccountRest(Double balance,Long customerId,){
        return null;
    }*/
    @GetMapping("accounts/operations/{accountId}")
    public List<AccountOperationDto> getAllOperationRest(@PathVariable String accountId)  {
        return bankAccountService.gatAllOperationById(accountId);
    }
    @GetMapping("accounts/history/{accountId}/pageOperations")
    public AccountHistoryDto getAccountHistory(@PathVariable String accountId,
                                                     @RequestParam(name = "page",defaultValue = "0") int page,
                                                     @RequestParam(name = "size",defaultValue = "5") int size) throws AccountNotFoundExeption {
        return bankAccountService.gatAccountHistory(accountId,page,size);
    }
    @PostMapping("/accounts/debit")
    public DebitDto debitRest(@RequestBody DebitDto debitDto) throws AccountNotFoundExeption, BalanceNotSufficientExeption {
        bankAccountService.debit(debitDto.getAccountId(),debitDto.getAmount(), debitDto.getDescription());
        return debitDto;
    }
    @PostMapping("/accounts/credit")
    public CreditDto creditRest(@RequestBody CreditDto creditDto) throws AccountNotFoundExeption, BalanceNotSufficientExeption {
        bankAccountService.credit(creditDto.getAccountId(),creditDto.getAmount(), creditDto.getDescription());
        return creditDto;
    }
    @PostMapping("/accounts/transfer")
    public void TransferRest(@RequestBody TransferDto transferDto) throws AccountNotFoundExeption, BalanceNotSufficientExeption {
        bankAccountService.transfer(transferDto.getSourceId(),transferDto.getDestinationId(),transferDto.getAmount());
    }
    @GetMapping("accounts/customer/{customerId}")
    public List<BankAccountDto> findBankAccountsByCustomerIdRest(@PathVariable Long customerId) throws CustomerNotFoundExeption {
        return bankAccountService.findBankAccountsByCustomerId(customerId);
    }

}
