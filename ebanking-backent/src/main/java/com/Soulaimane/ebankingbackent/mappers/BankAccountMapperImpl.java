package com.Soulaimane.ebankingbackent.mappers;

import com.Soulaimane.ebankingbackent.dtos.AccountOperationDto;
import com.Soulaimane.ebankingbackent.dtos.CurrentBankAccountDto;
import com.Soulaimane.ebankingbackent.dtos.CustomerDto;
import com.Soulaimane.ebankingbackent.dtos.SavingBankAccountDto;
import com.Soulaimane.ebankingbackent.entities.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
//"Mapstruct ou jmaper :framework qui peut faire le mapping auromatique "
@Service
public class BankAccountMapperImpl {
    public CustomerDto fromCustomer(Customer customer){

        CustomerDto customerDto=new CustomerDto();
        BeanUtils.copyProperties(customer,customerDto);
        return customerDto;
    }
    public Customer  fromCustomerDto(CustomerDto customerDto){
        Customer customer=new Customer();
        BeanUtils.copyProperties(customerDto,customer);
        return customer;
    }
    public SavingAccount fromSavingBankAccountDto(SavingBankAccountDto savingBankAccountDto){
        SavingAccount savingAccount=new SavingAccount();
        BeanUtils.copyProperties(savingBankAccountDto,savingAccount);
        savingAccount.setCustomer(fromCustomerDto(savingBankAccountDto.getCustomerDto()));
        return savingAccount;
    }
    public SavingBankAccountDto fromSavingBankAccount(SavingAccount savingAccount){
        SavingBankAccountDto savingBankAccountDto=new SavingBankAccountDto();
        BeanUtils.copyProperties(savingAccount,savingBankAccountDto);
        savingBankAccountDto.setCustomerDto(fromCustomer(savingAccount.getCustomer()));
        savingBankAccountDto.setType(savingAccount.getClass().getSimpleName());
        return savingBankAccountDto;
    }
    public CurrentAccount fromCurrentBankAccountDto(CurrentBankAccountDto currentBankAccountDto){
        CurrentAccount currentAccount=new CurrentAccount();
        BeanUtils.copyProperties(currentBankAccountDto,currentAccount);
        currentAccount.setCustomer(fromCustomerDto(currentBankAccountDto.getCustomerDto()));

        return currentAccount;
    }
    public CurrentBankAccountDto fromCurrentBankAccount(CurrentAccount currentAccount){
        CurrentBankAccountDto currentBankAccountDto=new CurrentBankAccountDto();
        BeanUtils.copyProperties(currentAccount,currentBankAccountDto);
        currentBankAccountDto.setCustomerDto(fromCustomer(currentAccount.getCustomer()));
        currentBankAccountDto.setType(currentAccount.getClass().getSimpleName());
        return currentBankAccountDto;
    }
    public AccountOperationDto fromAccountOperation(AccountOperation accountOperation){
        AccountOperationDto accountOperationDto=new AccountOperationDto();
        BeanUtils.copyProperties(accountOperation,accountOperationDto);
        return accountOperationDto;
    }

}
