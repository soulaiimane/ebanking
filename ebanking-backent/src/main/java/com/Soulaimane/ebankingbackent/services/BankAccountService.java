package com.Soulaimane.ebankingbackent.services;

import com.Soulaimane.ebankingbackent.dtos.*;
import com.Soulaimane.ebankingbackent.entities.*;
import com.Soulaimane.ebankingbackent.exeption.AccountNotFoundExeption;
import com.Soulaimane.ebankingbackent.exeption.BalanceNotSufficientExeption;
import com.Soulaimane.ebankingbackent.exeption.CustomerNotFoundExeption;

import java.util.List;

public interface BankAccountService {
    CustomerDto saveCustomer(CustomerDto customerDto);
    CustomerDto updateCustomer(CustomerDto customerDto) throws CustomerNotFoundExeption;
    void deleteCustomer(Long id) throws CustomerNotFoundExeption;

    CurrentBankAccountDto currentBankAccount(double balance, double overDraft, Long customerId) throws CustomerNotFoundExeption;
    SavingBankAccountDto savingBankAccount(double balance, double interestRate, Long customerId) throws CustomerNotFoundExeption;

    List<CustomerDto> customerList();
    BankAccountDto getBankAccount(String AccountId) throws AccountNotFoundExeption;
    void debit(String accountId,double amount,String description) throws AccountNotFoundExeption, BalanceNotSufficientExeption;
    void credit(String accountId,double amount,String description) throws AccountNotFoundExeption, BalanceNotSufficientExeption;
    void transfer(String accountIdSource,String accountIdDestination,double amount) throws AccountNotFoundExeption, BalanceNotSufficientExeption;
    CustomerDto getCustomer(Long customerId) throws CustomerNotFoundExeption;
    List<CustomerDto> searchCustomerList(String keyword);
    List<BankAccountDto> ACCOUNTLIST();


    AccountHistoryDto gatAccountHistory(String accountId, int page, int size) throws AccountNotFoundExeption;
}
